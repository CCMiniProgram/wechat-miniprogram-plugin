package com.zxy.ijplugin.wechat_miniprogram.reference

import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.*
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReferenceSet
import com.intellij.psi.tree.TokenSet
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ProcessingContext
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.WXMLMetadata
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.*
import com.zxy.ijplugin.wechat_miniprogram.utils.ComponentWxmlUtils
import com.zxy.ijplugin.wechat_miniprogram.utils.toTextRange

class WXMLReferenceContributor : PsiReferenceContributor() {

    companion object {
        // 描述可以被解析为路径的元素的属性
        private val PATH_ATTRIBUTES = arrayOf(
                PathAttribute("wxs", "src"),
                PathAttribute("image", "src"),
                PathAttribute("import", "src"),
                PathAttribute("include", "src")
        )

        private fun matchPathAttribute(tagName: String, attributeName: String): Boolean {
            return PATH_ATTRIBUTES.any {
                it.tagName == tagName && it.attributeName == attributeName
            }
        }
    }

    override fun registerReferenceProviders(psiReferenceRegistrar: PsiReferenceRegistrar) {
        // 解析wxml中的id
        psiReferenceRegistrar.registerReferenceProvider(
                PlatformPatterns.psiElement(WXMLStringText::class.java),
                object : PsiReferenceProvider() {
                    override fun getReferencesByElement(
                            psiElement: PsiElement, p1: ProcessingContext
                    ): Array<PsiReference> {
                        if (psiElement is WXMLStringText) {
                            val attribute = PsiTreeUtil.getParentOfType(psiElement, WXMLAttribute::class.java)
                            if (attribute != null && attribute.name == "id"
                                    && psiElement.nextSibling.node.elementType == WXMLTypes.STRING_END
                                    && psiElement.prevSibling.node.elementType == WXMLTypes.STRING_START) {
                                // 这个字符串内容必须在 id中
                                // 并且没有整个字符串没有表达式
                                return arrayOf(WXMLIdReference(psiElement))
                            }
                        }

                        return PsiReference.EMPTY_ARRAY
                    }
                }
        )

        // 解析wxml中的class
        psiReferenceRegistrar.registerReferenceProvider(
                PlatformPatterns.psiElement(WXMLStringText::class.java),
                object : PsiReferenceProvider() {
                    override fun getReferencesByElement(
                            psiElement: PsiElement, p1: ProcessingContext
                    ): Array<PsiReference> {
                        val attribute = PsiTreeUtil.getParentOfType(psiElement, WXMLAttribute::class.java)
                        if (attribute != null && (attribute.name == "class" || ComponentWxmlUtils.isExternalClassesAttribute(
                                        attribute
                                ))) {
                            val stringContentNodes = psiElement.node.getChildren(
                                    TokenSet.create(WXMLTypes.STRING_CONTENT)
                            )
                            return stringContentNodes.flatMap {
                                val findResults = Regex("[_\\-a-zA-Z][_\\-a-zA-Z0-9]+").findAll(it.text)
                                findResults.map { matchResult ->
                                    WXMLClassReference(
                                            psiElement, matchResult.range.toTextRange()
                                    )
                                }.toList()
                            }.toTypedArray()
                        }

                        return PsiReference.EMPTY_ARRAY
                    }

                }
        )

        // 解析wxml中的路径属性
        psiReferenceRegistrar.registerReferenceProvider(
                PlatformPatterns.psiElement(WXMLStringText::class.java),
                object : PsiReferenceProvider() {
                    override fun getReferencesByElement(
                            psiElement: PsiElement, processingContext: ProcessingContext
                    ): Array<out PsiReference> {
                        val wxmlElement = PsiTreeUtil.getParentOfType(psiElement, WXMLElement::class.java)
                        val wxmlAttribute = PsiTreeUtil.getParentOfType(psiElement, WXMLAttribute::class.java)
                        if (wxmlElement != null && wxmlAttribute != null && matchPathAttribute(
                                        wxmlElement.tagName!!, wxmlAttribute.name
                                )) {
                            // 这个属性是可解析为路径的
                            return FileReferenceSet(psiElement).allReferences
                        }
                        return PsiReference.EMPTY_ARRAY
                    }
                }
        )

        // 解析wxml中的template.is属性
        psiReferenceRegistrar.registerReferenceProvider(
                PlatformPatterns.psiElement(WXMLStringText::class.java),
                object : PsiReferenceProvider() {
                    override fun getReferencesByElement(
                            psiElement: PsiElement, p1: ProcessingContext
                    ): Array<PsiReference> {
                        psiElement as WXMLStringText
                        val wxmlAttribute = PsiTreeUtil.getParentOfType(psiElement, WXMLAttribute::class.java)
                        if (wxmlAttribute != null && wxmlAttribute.name == "is") {
                            val wxmlElement = PsiTreeUtil.getParentOfType(wxmlAttribute, WXMLElement::class.java)
                            if (wxmlElement != null && wxmlElement.tagName == "template") {
                                return arrayOf(WXMLTemplateIsAttributeReference(psiElement))
                            }
                        }
                        return PsiReference.EMPTY_ARRAY
                    }
                }
        )

        // 解析wxml tag
        // 如果tag在json文件中注册
        psiReferenceRegistrar.registerReferenceProvider(
                PlatformPatterns.psiElement(WXMLTag::class.java),
                object : PsiReferenceProvider() {
                    override fun getReferencesByElement(
                            psiElement: PsiElement, p1: ProcessingContext
                    ): Array<PsiReference> {
                        psiElement as WXMLTag
                        val tagNameNode = psiElement.getTagNameNode()
                        if (tagNameNode != null && WXMLMetadata.ELEMENT_DESCRIPTORS.none {
                                    tagNameNode.text == it.name
                                }) {
                            return arrayOf(WXMLTagReference(psiElement))
                        }
                        return PsiReference.EMPTY_ARRAY
                    }
                }
        )

        // 解析自定义组件标签上的属性
        // 引用js文件中的properties配置
        psiReferenceRegistrar.registerReferenceProvider(
                PlatformPatterns.psiElement(WXMLAttribute::class.java),
                object : PsiReferenceProvider() {
                    override fun getReferencesByElement(
                            psiElement: PsiElement, p1: ProcessingContext
                    ): Array<PsiReference> {
                        psiElement as WXMLAttribute
                        val wxmlTag = PsiTreeUtil.getParentOfType(psiElement, WXMLTag::class.java)
                        if (wxmlTag != null && wxmlTag.reference is WXMLTagReference) {
                            return arrayOf(WXMLAttributeReference(psiElement))
                        }
                        return PsiReference.EMPTY_ARRAY
                    }
                }
        )

    }

}