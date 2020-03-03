/*
 *    Copyright (c) [2019] [zxy]
 *    [wechat-miniprogram-plugin] is licensed under the Mulan PSL v1.
 *    You can use this software according to the terms and conditions of the Mulan PSL v1.
 *    You may obtain a copy of Mulan PSL v1 at:
 *       http://license.coscl.org.cn/MulanPSL
 *    THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR
 *    IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR
 *    PURPOSE.
 *    See the Mulan PSL v1 for more details.
 *
 *
 *                      Mulan Permissive Software License，Version 1
 *
 *    Mulan Permissive Software License，Version 1 (Mulan PSL v1)
 *    August 2019 http://license.coscl.org.cn/MulanPSL
 *
 *    Your reproduction, use, modification and distribution of the Software shall be subject to Mulan PSL v1 (this License) with following terms and conditions:
 *
 *    0. Definition
 *
 *       Software means the program and related documents which are comprised of those Contribution and licensed under this License.
 *
 *       Contributor means the Individual or Legal Entity who licenses its copyrightable work under this License.
 *
 *       Legal Entity means the entity making a Contribution and all its Affiliates.
 *
 *       Affiliates means entities that control, or are controlled by, or are under common control with a party to this License, ‘control’ means direct or indirect ownership of at least fifty percent (50%) of the voting power, capital or other securities of controlled or commonly controlled entity.
 *
 *    Contribution means the copyrightable work licensed by a particular Contributor under this License.
 *
 *    1. Grant of Copyright License
 *
 *       Subject to the terms and conditions of this License, each Contributor hereby grants to you a perpetual, worldwide, royalty-free, non-exclusive, irrevocable copyright license to reproduce, use, modify, or distribute its Contribution, with modification or not.
 *
 *    2. Grant of Patent License
 *
 *       Subject to the terms and conditions of this License, each Contributor hereby grants to you a perpetual, worldwide, royalty-free, non-exclusive, irrevocable (except for revocation under this Section) patent license to make, have made, use, offer for sale, sell, import or otherwise transfer its Contribution where such patent license is only limited to the patent claims owned or controlled by such Contributor now or in future which will be necessarily infringed by its Contribution alone, or by combination of the Contribution with the Software to which the Contribution was contributed, excluding of any patent claims solely be infringed by your or others’ modification or other combinations. If you or your Affiliates directly or indirectly (including through an agent, patent licensee or assignee）, institute patent litigation (including a cross claim or counterclaim in a litigation) or other patent enforcement activities against any individual or entity by alleging that the Software or any Contribution in it infringes patents, then any patent license granted to you under this License for the Software shall terminate as of the date such litigation or activity is filed or taken.
 *
 *    3. No Trademark License
 *
 *       No trademark license is granted to use the trade names, trademarks, service marks, or product names of Contributor, except as required to fulfill notice requirements in section 4.
 *
 *    4. Distribution Restriction
 *
 *       You may distribute the Software in any medium with or without modification, whether in source or executable forms, provided that you provide recipients with a copy of this License and retain copyright, patent, trademark and disclaimer statements in the Software.
 *
 *    5. Disclaimer of Warranty and Limitation of Liability
 *
 *       The Software and Contribution in it are provided without warranties of any kind, either express or implied. In no event shall any Contributor or copyright holder be liable to you for any damages, including, but not limited to any direct, or indirect, special or consequential damages arising from your use or inability to use the Software or the Contribution in it, no matter how it’s caused or based on which legal theory, even if advised of the possibility of such damages.
 *
 *    End of the Terms and Conditions
 *
 *    How to apply the Mulan Permissive Software License，Version 1 (Mulan PSL v1) to your software
 *
 *       To apply the Mulan PSL v1 to your work, for easy identification by recipients, you are suggested to complete following three steps:
 *
 *       i. Fill in the blanks in following statement, including insert your software name, the year of the first publication of your software, and your name identified as the copyright owner;
 *       ii. Create a file named “LICENSE” which contains the whole context of this License in the first directory of your software package;
 *       iii. Attach the statement to the appropriate annotated syntax at the beginning of each source file.
 *
 *    Copyright (c) [2019] [name of copyright holder]
 *    [Software Name] is licensed under the Mulan PSL v1.
 *    You can use this software according to the terms and conditions of the Mulan PSL v1.
 *    You may obtain a copy of Mulan PSL v1 at:
 *       http://license.coscl.org.cn/MulanPSL
 *    THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR
 *    IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR
 *    PURPOSE.
 *
 *    See the Mulan PSL v1 for more details.
 */

package com.zxy.ijplugin.wechat_miniprogram.inspections

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.json.psi.JsonFile
import com.intellij.json.psi.JsonProperty
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiManager
import com.intellij.psi.XmlElementVisitor
import com.intellij.psi.xml.XmlTag
import com.zxy.ijplugin.wechat_miniprogram.context.RelateFileType
import com.zxy.ijplugin.wechat_miniprogram.context.findRelateFile
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.WXMLMetadata
import com.zxy.ijplugin.wechat_miniprogram.lang.wxml.utils.nameTextRangeInSelf
import com.zxy.ijplugin.wechat_miniprogram.utils.AppJsonUtils
import com.zxy.ijplugin.wechat_miniprogram.utils.ComponentJsonUtils
import com.zxy.ijplugin.wechat_miniprogram.utils.getPathRelativeToRootRemoveExt

class WXMLUnknownTagInspection : WXMLInspectionBase() {

    companion object {
        const val QUICK_FIX_FAMILY_NAME = "WXML标签"
    }

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : XmlElementVisitor() {

            override fun visitXmlTag(wxmlTag: XmlTag) {
                val tagName = wxmlTag.name
                if (WXMLMetadata.getElementDescriptions(wxmlTag.project).any {
                            it.name == tagName
                        }) {
                    // 是自带组件
                    return
                }

                val project = wxmlTag.project

                val currentJsonFile = findRelateFile(wxmlTag.containingFile.virtualFile, RelateFileType.JSON)
                val psiManager = PsiManager.getInstance(wxmlTag.project)
                val currentJsonPsiFile = currentJsonFile?.let {
                    psiManager.findFile(it)
                } as? JsonFile
                val usingComponentItems = mutableListOf<JsonProperty>().apply {
                    currentJsonPsiFile?.let {
                        ComponentJsonUtils.getUsingComponentItems(it)
                    }?.let {
                        this.addAll(it)
                    }
                    AppJsonUtils.findUsingComponentItems(project)?.let {
                        this.addAll(it)
                    }
                }
                if (!usingComponentItems.any {
                            it.name == tagName
                        }) {
                    // 没有注册的标签
                    val jsonConfigurationFiles = ComponentJsonUtils.getAllComponentConfigurationFile(project).filter {
                        it != currentJsonPsiFile
                    }
                    val quickFixList = if (currentJsonPsiFile == null) {
                        emptyArray()
                    } else {
                        jsonConfigurationFiles.filter {
                            it.virtualFile.nameWithoutExtension == tagName
                        }.let { list ->
                            list.map {
                                object : LocalQuickFix {

                                    override fun getFamilyName(): String {
                                        return QUICK_FIX_FAMILY_NAME
                                    }

                                    override fun getName(): String {
                                        // 如果有多个组件匹配
                                        // 则显示他们相对于根目录的路径
                                        val componentName = if (list.size > 1) it.virtualFile.getPathRelativeToRootRemoveExt(
                                                it.project
                                        ) else {
                                            it.virtualFile.nameWithoutExtension
                                        }
                                        return "在${currentJsonPsiFile.name}中注册$componentName"
                                    }

                                    override fun applyFix(p0: Project, problemDescriptor: ProblemDescriptor) {
                                        ComponentJsonUtils.registerComponent(currentJsonPsiFile, it)
                                    }
                                }

                            }.toTypedArray<LocalQuickFix>()
                        }
                    }
                    holder.registerProblem(
                            wxmlTag, wxmlTag.nameTextRangeInSelf(), "未知的标签：$tagName",
                            *quickFixList
                    )

                }
            }
        }
    }

}

