package com.zxy.ijplugin.wechat_miniprogram.editor

import com.intellij.codeInsight.editorActions.TypedHandlerDelegate
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.zxy.ijplugin.wechat_miniprogram.lang.utils.findNextSibling
import com.zxy.ijplugin.wechat_miniprogram.lang.utils.findPrevSibling
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.WXMLLanguage
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.psi.WXMLTypes

class WXMLTagCloseTypedHandler : TypedHandlerDelegate() {

    override fun beforeCharTyped(c: Char, project: Project, editor: Editor, file: PsiFile, fileType: FileType): Result {
        // 当键入一个斜杠时
        // 根据上下文，完成未闭合的标签
        if (c == '/' && file.language === WXMLLanguage.INSTANCE) {
            val offset = editor.caretModel.offset
            val prevNode = file.viewProvider.findElementAt(offset - 1)?.node ?: return Result.CONTINUE
            if (prevNode.elementType === WXMLTypes.TAG_NAME && prevNode.treePrev.elementType === WXMLTypes.START_TAG_START && prevNode.treeNext?.elementType !== WXMLTypes.START_TAG_END) {
                // 单标签
                editor.document.insertString(offset, "/>")
                editor.caretModel.moveToOffset(offset + 2)
                return Result.STOP
            }
        }
        return Result.CONTINUE
    }

    override fun charTyped(c: Char, project: Project, editor: Editor, file: PsiFile): Result {

        if (file.language === WXMLLanguage.INSTANCE) {
            if (c == '/') {
                // 当键入一个斜杠时
                // 根据上下文，完成未闭合的标签
                val psiFile = TypeHandlerDelegateUtils.commitDocumentAndGetPsiFile(project, editor)
                        ?: return Result.CONTINUE
                val viewProvider = psiFile.viewProvider
                val offset = editor.caretModel.offset
                val inputCharElement = viewProvider.findElementAt(offset - 1) ?: return Result.CONTINUE
                if (inputCharElement.node.elementType === WXMLTypes.END_TAG_START) {
                    // 键入的结束标签的头部 </
                    val prevStartTagName = getPrevStartTagName(inputCharElement)
                    if (prevStartTagName != null) {
                        editor.document.insertString(offset, "$prevStartTagName>")
                        editor.caretModel.moveToOffset(offset + prevStartTagName.length + 1)
                    }
                }
            } else if (c == '>') {
                // 当键入一个标签结束符号
                // 如果此时正好构成一个标签开始
            }
        }
        return Result.CONTINUE
    }

    private fun getPrevStartTagName(psiElement: PsiElement): String? {
        val prev1 = psiElement.findPrevSibling {
            it.node.elementType == WXMLTypes.START_TAG_END
        }
        if (prev1 != null) {
            val prev2 = psiElement.findPrevSibling { it.node.elementType == WXMLTypes.TAG_NAME && it.findNextSibling { p -> p.node.elementType == WXMLTypes.START_TAG_END } == prev1 }
            if (prev2 != null) {
                val prev3 = prev2.prevSibling ?: return null
                if (prev3.node.elementType == WXMLTypes.START_TAG_START) {
                    return prev2.text
                }
            }
        }
        return null
    }

}