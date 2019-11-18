package com.zxy.ijplugin.wechat_miniprogram.lang.wxs

import com.intellij.lang.javascript.JavaScriptSupportLoader
import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

class WXSFileType : LanguageFileType(JavaScriptSupportLoader.JAVASCRIPT_1_5) {
    companion object{
        @JvmField
        val INSTANCE = WXSFileType()
    }

    override fun getIcon(): Icon? {
        return WXSIcons.FILE
    }

    override fun getName(): String {
        return "WXS"
    }

    override fun getDefaultExtension(): String {
        return "wxs"
    }

    override fun getDescription(): String {
        return "Wechat mini program style file"
    }
}