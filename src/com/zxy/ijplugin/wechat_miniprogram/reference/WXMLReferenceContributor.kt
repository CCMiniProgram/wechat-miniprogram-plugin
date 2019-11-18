package com.zxy.ijplugin.wechat_miniprogram.reference

import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.*
import com.intellij.psi.tree.TokenSet
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ProcessingContext
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLAttribute
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLStringText
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLTypes
import com.zxy.ijplugin.wechat_miniprogram.utils.toTextRange

class WXMLReferenceContributor : PsiReferenceContributor() {
    override fun registerReferenceProviders(psiReferenceRegistrar: PsiReferenceRegistrar) {
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

        psiReferenceRegistrar.registerReferenceProvider(
                PlatformPatterns.psiElement(WXMLStringText::class.java),
                object : PsiReferenceProvider() {
                    override fun getReferencesByElement(
                            psiElement: PsiElement, p1: ProcessingContext
                    ): Array<PsiReference> {
                        val attribute = PsiTreeUtil.getParentOfType(psiElement, WXMLAttribute::class.java)
                        if (attribute != null && attribute.name == "class") {
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
    }
}