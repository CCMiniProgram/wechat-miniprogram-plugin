package com.zxy.ijplugin.wechat_miniprogram.lang.wxss.utils

import com.intellij.lang.ASTNode
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.util.PsiTreeUtil
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.WXSSFileType
import com.zxy.ijplugin.wechat_miniprogram.lang.wxss.psi.*

object WXSSElementFactory {

    fun createClass(project: Project,className:String):ASTNode{
        val file = createDummyFile(project,"""
            .$className{
                
            }
        """.trimIndent())
        return PsiTreeUtil.findChildOfType(file, WXSSClassSelector::class.java)!!.node.findChildByType(WXSSTypes.CLASS)!!
    }

    fun createId(project: Project, id:String): ASTNode {
        val file = createDummyFile(project,"""
            #$id{
                
            }
        """.trimIndent())
        return PsiTreeUtil.findChildOfType(file,WXSSIdSelector::class.java)!!.node.findChildByType(WXSSTypes.ID)!!
    }

    fun createValue(project:Project, value:String): WXSSValue {
        val file = createDummyFile(project,"""
            .a{
                key: $value
            }
        """.trimIndent())
        return PsiTreeUtil.findChildOfType(file,WXSSValue::class.java)!!
    }

    fun createStringText(project: Project, text: String): WXSSStringText {
        val file = createDummyFile(
                project, """
            .a{
                key: "$text"
            }
        """.trimIndent()
        )
        return PsiTreeUtil.findChildOfType(file, WXSSStringText::class.java)!!
    }

    private fun createDummyFile(project: Project, fileContent:String): PsiFile {
        val name = "dummy.wxss"
        return PsiFileFactory.getInstance(project).createFileFromText(name,WXSSFileType.INSTANCE,fileContent)
    }

}