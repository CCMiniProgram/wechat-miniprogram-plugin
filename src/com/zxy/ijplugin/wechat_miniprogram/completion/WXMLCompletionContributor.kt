package com.zxy.ijplugin.wechat_miniprogram.completion

import com.intellij.codeInsight.AutoPopupController
import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ProcessingContext
import com.intellij.util.containers.stream
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.WXMLElementAttributeDescriptor
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.WXMLMetadata
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLAttribute
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLElement
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLTypes

class WXMLCompletionContributor : CompletionContributor() {

    init {
        this.extend(
                CompletionType.BASIC, PlatformPatterns.psiElement(WXMLTypes.ATTRIBUTE_NAME),
                WXMLAttributeCompletionProvider()
        )
        this.extend(
                CompletionType.BASIC, PlatformPatterns.psiElement(WXMLTypes.STRING_CONTENT),
                /**
                 * 对WXML的属性值提供完成
                 */
                object : CompletionProvider<CompletionParameters>() {
                    override fun addCompletions(
                            completionParameters: CompletionParameters, processingContext: ProcessingContext,
                            completionResultSet: CompletionResultSet
                    ) {
                        val stringContentElement = completionParameters.originalFile.findElementAt(
                                completionParameters.offset
                        )
                        // 对WXML的枚举属性值提供完成
                        val wxmlElement = PsiTreeUtil.getParentOfType(
                                stringContentElement, WXMLElement::class.java
                        )!!
                        val wxmlAttribute = PsiTreeUtil.findChildOfType(wxmlElement, WXMLAttribute::class.java)!!
                        val tagName = wxmlElement.tagName
                        val attribute = WXMLMetadata.ELEMENT_DESCRIPTORS.stream().filter { it.name == tagName }
                                .findFirst()
                                .map { it.attributeDescriptors }
                                .orElse(emptyArray())
                                .stream()
                                .filter { it.key == wxmlAttribute.name }
                                .findFirst()
                                .orElse(null) ?: return
                        if (wxmlAttributeIsEnumerable(attribute)) {
                            completionResultSet.addAllElements(attribute.enums.map {
                                LookupElementBuilder.create(it)
                                        .withInsertHandler { insertionContext, lookupElement ->
                                            val editor = insertionContext.editor
                                            val range = completionParameters.position.textRange
                                            editor.document.replaceString(
                                                    range.startOffset, range.endOffset,
                                                    lookupElement.lookupString
                                            )
                                        }
                            })
                        }

                    }
                }
        )
    }

}

private fun wxmlAttributeIsEnumerable(
        attribute: WXMLElementAttributeDescriptor
) =
        attribute.enums.isNotEmpty() && attribute.types.size == 1 && attribute.types[0] == WXMLElementAttributeDescriptor.ValueType.STRING


class WXMLAttributeCompletionProvider : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(
            completionParameters: CompletionParameters, processingContext: ProcessingContext,
            completionResultSet: CompletionResultSet
    ) {
        // 根据组件名称进行完成
        val wxmlElement = PsiTreeUtil.getParentOfType(
                completionParameters.position, WXMLElement::class.java
        )
        val tagName = wxmlElement!!.tagName

        val wxmlAttributeNames = PsiTreeUtil.findChildrenOfType(wxmlElement, WXMLAttribute::class.java)
                .map(WXMLAttribute::getName)

        val attributes = WXMLMetadata.ELEMENT_DESCRIPTORS.stream().filter { it.name == tagName }
                .findFirst()
                .map { it.attributeDescriptors }
                .orElse(emptyArray())
                .toMutableList().apply {
                    // 全局属性
                    this.addAll(WXMLMetadata.COMMON_ELEMENT_ATTRIBUTE_DESCRIPTORS)
                }
                // 过滤掉元素上已存在的类型
                .filter { !wxmlAttributeNames.contains(it.key) }
        completionResultSet.addAllElements(attributes.map {
            createLookupElement(it)
        })
    }

    private fun createLookupElement(it: WXMLElementAttributeDescriptor): LookupElementBuilder {
        return when {
            it.types.contains(
                    WXMLElementAttributeDescriptor.ValueType.BOOLEAN
            ) && it.default == false -> {
                // 属性的默认值为false
                // 只插入属性名称
                return LookupElementBuilder.create(it.key)
            }
            it.types.contains(
                    WXMLElementAttributeDescriptor.ValueType.NUMBER
            ) && !it.types.contains(
                    WXMLElementAttributeDescriptor.ValueType.STRING
            ) -> LookupElementBuilder.create(it.key).withInsertHandler { insertionContext, _ ->
                // 额外插入 [="{{}}"]
                val editor = insertionContext.editor
                val offset = editor.caretModel.offset
                insertionContext.document.insertString(offset, "=\"{{}}\"")
                editor.caretModel.moveToOffset(offset + 4)
            }
            else -> LookupElementBuilder.create(it.key).withInsertHandler { insertionContext, _ ->
                // 额外插入 [=""]
                val editor = insertionContext.editor
                val offset = editor.caretModel.offset
                insertionContext.document.insertString(offset, "=\"\"")
                editor.caretModel.moveToOffset(offset + 2)
                if (wxmlAttributeIsEnumerable(it)) {
                    AutoPopupController.getInstance(insertionContext.project)
                            .autoPopupMemberLookup(insertionContext.editor, null)
                }
            }
        }
    }
}
