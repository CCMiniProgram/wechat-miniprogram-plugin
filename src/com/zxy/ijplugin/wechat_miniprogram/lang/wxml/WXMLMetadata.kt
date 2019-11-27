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

package com.zxy.ijplugin.wechat_miniprogram.lang.wxml

data class WXMLElementDescriptor(
        val name: String,
        val attributeDescriptors: Array<WXMLElementAttributeDescriptor> = emptyArray(),
        val events: Array<String> = emptyArray(),
        val canOpen: Boolean = true,
        val canClose: Boolean = false
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WXMLElementDescriptor

        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}

data class WXMLElementAttributeDescriptor(
        val key: String,
        val types: Array<ValueType>,
        val default: Any? = null,
        val required: Boolean = false,
        val enums: Array<String> = emptyArray(),
        val requiredInEnums: Boolean = true
) {

    enum class ValueType {
        STRING, NUMBER, BOOLEAN,
        COLOR, ARRAY, OBJECT
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WXMLElementAttributeDescriptor

        if (key != other.key) return false

        return true
    }

    override fun hashCode(): Int {
        return key.hashCode()
    }
}

typealias E = WXMLElementDescriptor
typealias A = WXMLElementAttributeDescriptor
typealias T = WXMLElementAttributeDescriptor.ValueType

object WXMLMetadata {
    val ELEMENT_DESCRIPTORS = arrayOf(
            // 特殊的标签
            E("block"),
            E(
                    "template",
                    arrayOf(
                            A("is", arrayOf(T.STRING)),
                            A("name", arrayOf(T.STRING)),
                            A("data", arrayOf(T.OBJECT))
                    )
            ),
            E("import", arrayOf(A("src", arrayOf(T.STRING), null, true))),
            E("include", arrayOf(A("src", arrayOf(T.STRING), null, true))),
            E("wxs", arrayOf(A("module", arrayOf(T.STRING)), A("src", arrayOf(T.STRING)))),
            E(
                    "slot",
                    arrayOf(
                            A("name", arrayOf(T.STRING))
                    )
            ),

            E(
                    "view",
                    arrayOf(
                            A(
                                    "hover-class", arrayOf(T.STRING), "none"
                            ),
                            A(
                                    "hover-stop-propagation", arrayOf(T.BOOLEAN),
                                    false
                            ),
                            A(
                                    "hover-start-time", arrayOf(T.NUMBER), 50
                            ),
                            A(
                                    "hover-stay-time", arrayOf(T.NUMBER), 400
                            )
                    )
            ),
            E(
                    "cover-image",
                    arrayOf(
                            A(
                                    "src",
                                    arrayOf(T.STRING)
                            )
                    ),
                    arrayOf("load", "error")
            ),
            E(
                    "cover-view",
                    arrayOf(
                            A(
                                    "scroll-top",
                                    arrayOf(
                                            T.STRING,
                                            T.NUMBER
                                    )
                            )
                    )
            ),
            E(
                    "movable-area",
                    arrayOf(
                            A(
                                    "scale-area", arrayOf(T.BOOLEAN), false
                            )
                    )
            ),
            E(
                    "movable-view",
                    arrayOf(
                            A(
                                    "direction", arrayOf(T.STRING), "none"
                            ),
                            A(
                                    "inertia", arrayOf(T.BOOLEAN), false
                            ),
                            A(
                                    "out-of-bounds", arrayOf(T.BOOLEAN), false
                            ),
                            A(
                                    "x", arrayOf(T.NUMBER)
                            ),
                            A(
                                    "y", arrayOf(T.NUMBER)
                            ),
                            A(
                                    "damping", arrayOf(T.NUMBER), 20
                            ),
                            A(
                                    "friction", arrayOf(T.NUMBER), 2
                            ),
                            A(
                                    "disabled", arrayOf(T.BOOLEAN), false
                            ),
                            A(
                                    "scale", arrayOf(T.BOOLEAN), false
                            ),
                            A(
                                    "scale-min", arrayOf(T.NUMBER), 0.5
                            ),
                            A(
                                    "scale-max", arrayOf(T.NUMBER), 10
                            ),
                            A(
                                    "scale-value", arrayOf(T.NUMBER), 1
                            ),
                            A(
                                    "animation", arrayOf(T.BOOLEAN), true
                            )
                    ),
                    events = arrayOf("change", "scale", "htouchmove", "vtouchmove")
            ),
            E(
                    "scroll-view",
                    arrayOf(
                            A(
                                    "scroll-view", arrayOf(T.BOOLEAN), false
                            ),
                            A(
                                    "scroll-y", arrayOf(T.BOOLEAN), false
                            ),
                            A(
                                    "upper-threshold",
                                    arrayOf(
                                            T.NUMBER,
                                            T.STRING
                                    ), 50
                            ),
                            A(
                                    "lower-threshold",
                                    arrayOf(
                                            T.NUMBER,
                                            T.STRING
                                    ), 50
                            ),
                            A(
                                    "scroll-top",
                                    arrayOf(
                                            T.NUMBER,
                                            T.STRING
                                    )
                            ),
                            A(
                                    "scroll-bottom",
                                    arrayOf(
                                            T.NUMBER,
                                            T.STRING
                                    )
                            ),
                            A(
                                    "scroll-into-view", arrayOf(T.STRING)
                            ),
                            A(
                                    "scroll-with-animation", arrayOf(T.BOOLEAN),
                                    false
                            ),
                            A(
                                    "enable-back-to-top", arrayOf(T.BOOLEAN),
                                    false
                            ),
                            A(
                                    "enable-flex", arrayOf(T.BOOLEAN), false
                            ),
                            A(
                                    "scroll-anchoring", arrayOf(T.BOOLEAN), false
                            )
                    ),
                    arrayOf("scrolltoupper", "scrolltolower", "scroll")
            ),
            E(
                    "swiper",
                    arrayOf(
                            A("indicator-dots", arrayOf(T.BOOLEAN), false),
                            A("indicator-color", arrayOf(T.COLOR), "rgba(0, 0, 0, .3)"),
                            A("indicator-active-color", arrayOf(T.COLOR), "#000000"),
                            A("autoplay", arrayOf(T.BOOLEAN), false),
                            A("current", arrayOf(T.NUMBER), 0),
                            A("interval", arrayOf(T.NUMBER), 0),
                            A("interval", arrayOf(T.NUMBER), 5000),
                            A("duration", arrayOf(T.NUMBER), 500),
                            A("circular", arrayOf(T.BOOLEAN), false),
                            A("vertical", arrayOf(T.BOOLEAN), false),
                            A("previous-margin", arrayOf(T.STRING), "0px"),
                            A("next-margin", arrayOf(T.STRING), "0px"),
                            A("display-multiple-items", arrayOf(T.NUMBER), 1),
                            A("skip-hidden-item-layout", arrayOf(T.BOOLEAN), false),
                            A(
                                    "easing-function", arrayOf(T.STRING), "default", false,
                                    arrayOf("default", "linear", "easeInCubic", "easeOutCubic", "easeInOutCubic")
                            )
                    ),
                    arrayOf("change", "transition", "animationfinish")
            ),
            E("swiper-item", arrayOf(A("item-id", arrayOf(T.STRING)))),
            E(
                    "icon",
                    arrayOf(
                            A(
                                    "type", arrayOf(T.STRING), null, true,
                                    arrayOf(
                                            "success", "success_no_circle", "info", "warn", "waiting", "cancel",
                                            "download",
                                            "search",
                                            "clear"
                                    )
                            ),
                            A("size", arrayOf(T.STRING, T.NUMBER), 23),
                            A("color", arrayOf(T.STRING))
                    )
            ),
            E(
                    "progress",
                    arrayOf(
                            A("percent", arrayOf(T.NUMBER)),
                            A("show-info", arrayOf(T.BOOLEAN), false),
                            A("border-radius", arrayOf(T.NUMBER, T.STRING), 0),
                            A("font-size", arrayOf(T.NUMBER, T.STRING), 16),
                            A("stroke-width", arrayOf(T.NUMBER, T.STRING), 6),
                            A("color", arrayOf(T.STRING), "#09BB07"),
                            A("activeColor", arrayOf(T.STRING), "#09BB07"),
                            A("backgroundColor", arrayOf(T.STRING), "#EBEBEB"),
                            A("active", arrayOf(T.BOOLEAN), false),
                            A("active-mode", arrayOf(T.STRING), "backwards", false, arrayOf("forwards", "backwards")),
                            A("duration", arrayOf(T.NUMBER, T.STRING), 30)
                    ),
                    arrayOf("activeend")
            ),
            E(
                    "rich-text",
                    arrayOf(
                            A("nodes", arrayOf(T.ARRAY, T.STRING), emptyArray<Any>()),
                            A("space", arrayOf(T.STRING), null, false, arrayOf("ensp", "emsp", "nbsp"))
                    )
            ),
            E(
                    "text",
                    arrayOf(
                            A("selectable", arrayOf(T.BOOLEAN), false),
                            A("space", arrayOf(T.STRING), null, false, arrayOf("ensp", "emsp", "nbsp")),
                            A("decode", arrayOf(T.BOOLEAN), false)
                    )
            ),
            E(
                    "button",
                    arrayOf(
                            A("size", arrayOf(T.STRING), "default", false, arrayOf("default", "mini")),
                            A("type", arrayOf(T.STRING), "default", false, arrayOf("primary", "default", "warn")),
                            A("plain", arrayOf(T.BOOLEAN), false),
                            A("disabled", arrayOf(T.BOOLEAN), false),
                            A("loading", arrayOf(T.BOOLEAN), false),
                            A("from-type", arrayOf(T.STRING), null, false, arrayOf("submit", "reset")),
                            A(
                                    "open-type", arrayOf(T.STRING), null, false,
                                    arrayOf(
                                            "contact", "share", "getPhoneNumber", "getUserInfo", "launchApp",
                                            "openSetting",
                                            "feedback"
                                    )
                            ),
                            A("hover-class", arrayOf(T.STRING), "button-hover"),
                            A("hover-stop-propagation", arrayOf(T.BOOLEAN), false),
                            A("hover-start-time", arrayOf(T.NUMBER), 20),
                            A("hover-stay-time", arrayOf(T.NUMBER), 70),
                            A("lang", arrayOf(T.STRING), "en", false, arrayOf("en", "zh_CN", "zh_TW")),
                            A("session-from", arrayOf(T.STRING)),
                            A("send-message-title", arrayOf(T.STRING)),
                            A("send-message-img", arrayOf(T.STRING)),
                            A("send-message-path", arrayOf(T.STRING)),
                            A("app-parameter", arrayOf(T.STRING)),
                            A("show-message-card", arrayOf(T.BOOLEAN), false)
                    ),
                    arrayOf("getuserinfo", "contact", "getphonenumber", "error", "opensetting", "launchapp")
            ),
            E(
                    "checkbox",
                    arrayOf(
                            A("value", arrayOf(T.STRING)),
                            A("disabled", arrayOf(T.BOOLEAN), false),
                            A("checked", arrayOf(T.BOOLEAN), false),
                            A("color", arrayOf(T.COLOR), "#09BB07")
                    )
            ),
            E("checkbox-group", emptyArray(), arrayOf("change")),
            E(
                    "editor",
                    arrayOf(
                            A("read-only", arrayOf(T.BOOLEAN), false),
                            A("placeholder", arrayOf(T.STRING)),
                            A("show-img-size", arrayOf(T.BOOLEAN), false),
                            A("show-img-toolbar", arrayOf(T.BOOLEAN), false),
                            A("show-img-resize", arrayOf(T.BOOLEAN), false)
                    ),
                    arrayOf("ready", "focus", "blur", "input", "statuschange")
            ),
            E(
                    "form",
                    arrayOf(
                            A("report-submit", arrayOf(T.BOOLEAN), false),
                            A("report-submit-timeout", arrayOf(T.NUMBER))
                    ),
                    arrayOf("submit", "reset")
            ),
            E(
                    "input",
                    arrayOf(
                            A("value", arrayOf(T.STRING), null, true),
                            A("type", arrayOf(T.STRING), "text", false, arrayOf("text", "number", "idcard", "digit")),
                            A("password", arrayOf(T.BOOLEAN), false),
                            // 文档上是必填
                            A("placeholder", arrayOf(T.STRING), null, false),
                            // 文档上是必填
                            A("placeholder-style", arrayOf(T.STRING), null, false),
                            A("placeholder-class", arrayOf(T.STRING), "input-placeholder"),
                            A("disabled", arrayOf(T.BOOLEAN), false),
                            A("maxlength", arrayOf(T.NUMBER), 140),
                            A("cursor-spacing", arrayOf(T.NUMBER), 0),
                            A("focus", arrayOf(T.BOOLEAN), false),
                            A(
                                    "confirm-type", arrayOf(T.STRING), "done", false,
                                    arrayOf("send", "search", "next", "go", "done")
                            ),
                            A("confirm-hold", arrayOf(T.BOOLEAN), false),
                            // 文档上是必填
                            A("cursor", arrayOf(T.NUMBER), null, false),
                            A("selection-end", arrayOf(T.NUMBER), -1),
                            A("adjust-position", arrayOf(T.BOOLEAN), true),
                            A("hold-keyboard", arrayOf(T.BOOLEAN), false)
                    ),
                    arrayOf("input", "focus", "blur", "confirm", "keyboardheightchange")
            ),
            E("label", arrayOf(A("for", arrayOf(T.STRING)))),
            E(
                    "picker",
                    arrayOf(
                            A(
                                    "mode", arrayOf(T.STRING), "selector", false,
                                    arrayOf("selector", "multiSelector", "time", "date", "region")
                            ),
                            A("disabled", arrayOf(T.BOOLEAN), false),
                            // mode = selector
                            A("range", arrayOf(T.ARRAY, T.OBJECT), emptyArray<Any>()),
                            A("range-key", arrayOf(T.STRING)),
                            A("value", arrayOf(T.NUMBER, T.ARRAY)),
                            A("start", arrayOf(T.STRING)),
                            A("fields", arrayOf(T.STRING), "day", false, arrayOf("year", "month", "day")),
                            A("start", arrayOf(T.STRING)),
                            A("custom-item", arrayOf(T.STRING))
                    ),
                    arrayOf("cancel", "change", "columnchange")
            ),
            E(
                    "picker-view",
                    arrayOf(
                            A(
                                    "value", arrayOf(T.ARRAY)
                            ),
                            A("indicator-style", arrayOf(T.STRING)),
                            A("indicator-class", arrayOf(T.STRING)),
                            A("mask-style", arrayOf(T.STRING)),
                            A("mask-class", arrayOf(T.STRING))
                    ),
                    arrayOf("change", "pickstart", "pickend")
            ),
            E("picker-view-column"),
            E(
                    "radio",
                    arrayOf(
                            A("value", arrayOf(T.STRING)),
                            A("checked", arrayOf(T.BOOLEAN), false),
                            A("disabled", arrayOf(T.BOOLEAN), false),
                            A("color", arrayOf(T.COLOR), "#09BB07")
                    )
            ),
            E("radio-group", emptyArray(), arrayOf("change")),
            E(
                    "slider",
                    arrayOf(
                            A("min", arrayOf(T.NUMBER), 0),
                            A("max", arrayOf(T.NUMBER), 100),
                            A("step", arrayOf(T.NUMBER), 1),
                            A("disabled", arrayOf(T.BOOLEAN), false),
                            A("value", arrayOf(T.NUMBER), 1),
                            A("color", arrayOf(T.COLOR), "#e9e9e9"),
                            A("selected-color", arrayOf(T.COLOR), "#1aad19"),
                            A("activeColor", arrayOf(T.COLOR), "#1aad19"),
                            A("backgroundColor", arrayOf(T.COLOR), "#e9e9e9"),
                            A("block-size", arrayOf(T.NUMBER), 1),
                            A("block-color", arrayOf(T.COLOR), "#FFFFFF"),
                            A("show-value", arrayOf(T.BOOLEAN), false)
                    ),
                    arrayOf("change", "changing")
            ),
            E(
                    "switch",
                    arrayOf(
                            A("checked", arrayOf(T.BOOLEAN), false),
                            A("disabled", arrayOf(T.BOOLEAN), false),
                            A("type", arrayOf(T.STRING), "switch", false, arrayOf("switch", "checkbox")),
                            A("color", arrayOf(T.COLOR), "#04BE02")
                    ),
                    arrayOf("change")
            ),
            E(
                    "textarea",
                    arrayOf(
                            A("value", arrayOf(T.STRING)),
                            A("placeholder", arrayOf(T.STRING)),
                            A("placeholder-style", arrayOf(T.STRING)),
                            A("placeholder-class", arrayOf(T.STRING)),
                            A("checked", arrayOf(T.BOOLEAN), false),
                            A("maxlength", arrayOf(T.NUMBER), 140),
                            A("focus", arrayOf(T.BOOLEAN), false),
                            A("auto-height", arrayOf(T.BOOLEAN), false),
                            A("fixed", arrayOf(T.BOOLEAN), false),
                            A("cursor-spacing", arrayOf(T.NUMBER), 140),
                            A("cursor", arrayOf(T.NUMBER), -140),
                            A("show-confirm-bar", arrayOf(T.BOOLEAN), true),
                            A("selection-start", arrayOf(T.NUMBER), -1),
                            A("selection-end", arrayOf(T.NUMBER), -1),
                            A("adjust-position", arrayOf(T.BOOLEAN), true),
                            A("hold-keyboard", arrayOf(T.BOOLEAN), false)
                    ),
                    arrayOf("focus", "blur", "linechange", "input", "confirm", "keyboardheightchange")
            ),
            E(
                    "functional-page-navigator",
                    arrayOf(
                            A("version", arrayOf(T.STRING), "release", false, arrayOf("release", "develop", "trial")),
                            A(
                                    "name", arrayOf(T.STRING), null, true,
                                    arrayOf("loginAndGetUserInfo", "requestPayment", "chooseAddress")
                            ),
                            A("args", arrayOf(T.OBJECT))
                    ),
                    arrayOf("success", "fail", "cancel")
            ),
            E(
                    "navigator",
                    arrayOf(
                            A("target", arrayOf(T.STRING), "self", false, arrayOf("miniProgram", "self")),
                            A("url", arrayOf(T.STRING), null),
                            A(
                                    "open-type", arrayOf(T.STRING), "navigate", false,
                                    arrayOf("redirect", "navigate", "switchTab", "reLaunch", "navigateBack", "exit")
                            ),
                            A("url", arrayOf(T.NUMBER), 1),
                            A("app-id", arrayOf(T.STRING)),
                            A("path", arrayOf(T.STRING)),
                            A("extra-data", arrayOf(T.OBJECT)),
                            A("version", arrayOf(T.STRING), "release", false, arrayOf("release", "develop", "trial")),
                            A("hover-class", arrayOf(T.STRING), "navigator-hover"),
                            A("hover-stop-propagation", arrayOf(T.BOOLEAN), false),
                            A("hover-start-time", arrayOf(T.NUMBER), 50),
                            A("hover-stay-time", arrayOf(T.NUMBER), 600)
                    ),
                    arrayOf("success", "fail", "complete")
            ),
            E(
                    "audio",
                    arrayOf(
                            A("id", arrayOf(T.STRING)),
                            A("src", arrayOf(T.STRING)),
                            A("loop", arrayOf(T.BOOLEAN), false),
                            A("controls", arrayOf(T.BOOLEAN), false),
                            A("poster", arrayOf(T.STRING), "未知音频"),
                            A("name", arrayOf(T.STRING)),
                            A("author", arrayOf(T.STRING), "未知作者")
                    ),
                    arrayOf("error", "play", "pause", "timeupdate", "ended")
            ),
            E(
                    "camera",
                    arrayOf(
                            A("mode", arrayOf(T.STRING), "normal", false, arrayOf("scanCode", "normal")),
                            A("device-position", arrayOf(T.STRING), "back", false, arrayOf("front", "back")),
                            A("flash", arrayOf(T.STRING), "auto", false, arrayOf("auto", "on", "off", "torch")),
                            A("frame-size", arrayOf(T.STRING), "medium", false, arrayOf("small", "large", "medium"))
                    ),
                    arrayOf("stop", "error", "initdone", "scancode")
            ),
            E(
                    "image",
                    arrayOf(
                            A("src", arrayOf(T.STRING), null, true),
                            A(
                                    "mode", arrayOf(T.STRING), "back", false,
                                    arrayOf(
                                            "scaleToFill", "aspectFit", "widthFix", "aspectFill", "top", "bottom",
                                            "center",
                                            "left", "right", "top left", "top right", "bottom left", "bottom right"
                                    )
                            ),
                            A("webp", arrayOf(T.BOOLEAN), false),
                            A("lazy-load", arrayOf(T.BOOLEAN), false),
                            A("show-menu-by-longpress", arrayOf(T.BOOLEAN), false)
                    ),
                    arrayOf("error", "load")
            ),
            E(
                    "live-player",
                    arrayOf(
                            A("src", arrayOf(T.STRING), null, true),
                            A("mode", arrayOf(T.STRING), "live", false, arrayOf("mode", "RTC")),
                            A("autoplay", arrayOf(T.BOOLEAN), false),
                            A("muted", arrayOf(T.BOOLEAN), false),
                            A("orientation", arrayOf(T.STRING), "vertical", false, arrayOf("horizontal", "vertical")),
                            A("object-fit", arrayOf(T.STRING), "contain", false, arrayOf("fillCrop", "contain")),
                            A("background-mute", arrayOf(T.BOOLEAN), false),
                            A("min-cache", arrayOf(T.NUMBER), 1),
                            A("max-cache", arrayOf(T.NUMBER), 3),
                            A("sound-mode", arrayOf(T.STRING), "speaker", false, arrayOf("ear", "speaker")),
                            A("auto-pause-if-navigate", arrayOf(T.BOOLEAN), true),
                            A("auto-pause-if-open-native", arrayOf(T.BOOLEAN), true)
                    ),
                    arrayOf("fullscreenchange", "statechange", "netstatus")
            ),
            E(
                    "live-pusher",
                    arrayOf(
                            A("url", arrayOf(T.STRING), null, true),
                            A("mode", arrayOf(T.STRING), "RTC", false, arrayOf("SD", "HD", "FHD", "RTC")),
                            A("autopush", arrayOf(T.BOOLEAN), false),
                            A("enable-camera", arrayOf(T.BOOLEAN), true),
                            A("auto-focus", arrayOf(T.BOOLEAN), true),
                            A("orientation", arrayOf(T.STRING), "vertical", false, arrayOf("horizontal", "vertical")),
                            A("beauty", arrayOf(T.NUMBER), 0),
                            A("whiteness", arrayOf(T.NUMBER), 0),
                            A("aspect", arrayOf(T.STRING), "9:16", false, arrayOf("3:4", "9:16")),
                            A("background-mute", arrayOf(T.BOOLEAN), false),
                            A("min-bitrate", arrayOf(T.NUMBER), 200),
                            A("max-bitrate", arrayOf(T.NUMBER), 1000),
                            A("audio-quality", arrayOf(T.STRING), "high", false, arrayOf("high", "low")),
                            A("waiting-image", arrayOf(T.STRING)),
                            A("waiting-image-hash", arrayOf(T.STRING)),
                            A("zoom", arrayOf(T.BOOLEAN), false),
                            A("device-position", arrayOf(T.STRING), "front", false, arrayOf("back", "front")),
                            A("background-mute", arrayOf(T.BOOLEAN), false),
                            A("mirror", arrayOf(T.BOOLEAN), false)
                    ),
                    arrayOf("statechange", "netstatus", "error", "bgmstart", "bgmprogress", "bgmcomplete")
            ),
            E(
                    "video",
                    arrayOf(
                            A("src", arrayOf(T.STRING), null, true),
                            A("duration", arrayOf(T.NUMBER)),
                            A("controls", arrayOf(T.BOOLEAN), true),
                            A("danmu-list", arrayOf(T.ARRAY, T.OBJECT), true),
                            A("danmu-btn", arrayOf(T.BOOLEAN), false),
                            A("enable-danmu", arrayOf(T.BOOLEAN), false),
                            A("autoplay", arrayOf(T.BOOLEAN), false),
                            A("loop", arrayOf(T.BOOLEAN), false),
                            A("muted", arrayOf(T.BOOLEAN), false),
                            A("initial-time", arrayOf(T.NUMBER), 0),
                            A("page-gesture", arrayOf(T.BOOLEAN), false),
                            A("direction", arrayOf(T.NUMBER), null, false, arrayOf("0", "90", "-90")),
                            A("show-progress", arrayOf(T.BOOLEAN), true),
                            A("show-fullscreen-btn", arrayOf(T.BOOLEAN), true),
                            A("show-play-btn", arrayOf(T.BOOLEAN), true),
                            A("show-center-play-btn", arrayOf(T.BOOLEAN), true),
                            A("enable-progress-gesture", arrayOf(T.BOOLEAN), true),
                            A("object-fit", arrayOf(T.STRING), "9:16", false, arrayOf("contain", "fill", "cover")),
                            A("poster", arrayOf(T.STRING)),
                            A("show-mute-btn", arrayOf(T.BOOLEAN), true),
                            A("title", arrayOf(T.STRING), false),
                            A("play-btn-position", arrayOf(T.STRING), "bottom", false, arrayOf("center", "bottom")),
                            A("enable-play-gesture", arrayOf(T.BOOLEAN), false),
                            A("auto-pause-if-navigate", arrayOf(T.BOOLEAN), true),
                            A("auto-pause-if-open-native", arrayOf(T.BOOLEAN), true),
                            A("vslide-gesture", arrayOf(T.BOOLEAN), false),
                            A("vslide-gesture-in-fullscreen", arrayOf(T.BOOLEAN), true),
                            A("ad-unit-id", arrayOf(T.STRING))
                    ),
                    arrayOf(
                            "play", "pause", "ended", "timeupdate", "fullscreenchange", "waiting", "error", "progress",
                            "loadedmetadata"
                    )
            ),
            E(
                    "map",
                    arrayOf(
                            A("longitude", arrayOf(T.NUMBER), null, true),
                            A("latitude", arrayOf(T.NUMBER), null, true),
                            A("scale", arrayOf(T.NUMBER), 16),
                            A("markers", arrayOf(T.ARRAY)),
                            A("polyline", arrayOf(T.ARRAY)),
                            A("circles", arrayOf(T.ARRAY)),
                            A("include-points", arrayOf(T.ARRAY)),
                            A("show-location", arrayOf(T.BOOLEAN), false),
                            A("polygons", arrayOf(T.ARRAY)),
                            A("subkey", arrayOf(T.STRING)),
                            A("layer-style", arrayOf(T.NUMBER), 1),
                            A("rotate", arrayOf(T.NUMBER), 0),
                            A("skew", arrayOf(T.NUMBER), 0),
                            A("enable-3D", arrayOf(T.BOOLEAN), false),
                            A("show-compass", arrayOf(T.BOOLEAN), false),
                            A("show-scale", arrayOf(T.BOOLEAN), false),
                            A("enable-overlooking", arrayOf(T.BOOLEAN), false),
                            A("enable-zoom", arrayOf(T.BOOLEAN), true),
                            A("enable-scroll", arrayOf(T.BOOLEAN), true),
                            A("enable-rotate", arrayOf(T.BOOLEAN), true),
                            A("enable-satellite", arrayOf(T.BOOLEAN), true),
                            A("enable-traffic", arrayOf(T.BOOLEAN), true),
                            A("setting", arrayOf(T.OBJECT))
                    ),
                    arrayOf(
                            "markertap",
                            "labeltap",
                            "controltap",
                            "callouttap",
                            "updated",
                            "regionchange",
                            "poitap"
                    )
            ),
            E(
                    "canvas",
                    arrayOf(
                            A("type", arrayOf(T.STRING)),
                            A("canvas-id", arrayOf(T.STRING)),
                            A("disable-scroll", arrayOf(T.STRING))
                    ),
                    arrayOf("error")
            ),
            E(
                    "ad",
                    arrayOf(
                            A("unit-id", arrayOf(T.STRING), null, true),
                            A("ad-intervals", arrayOf(T.NUMBER))
                    ),
                    arrayOf("load", "error", "close")
            ),
            E(
                    "official-account",
                    events = arrayOf("load", "error")
            ),
            E(
                    "open-data",
                    arrayOf(
                            // 文档中type不是必填属性
                            A(
                                    "type", arrayOf(T.STRING), null, true,
                                    arrayOf(
                                            "groupName", "userNickName", "userAvatarUrl", "userGender", "userCity",
                                            "userProvince", "userCountry", "userLanguage"
                                    )
                            ),
                            A("open-gid", arrayOf(T.STRING)),
                            A("lang", arrayOf(T.STRING), "en", false, arrayOf("zh_CN", "zh_TW", "en")),
                            A("default-text", arrayOf(T.STRING)),
                            A("default-avatar", arrayOf(T.STRING))
                    ),
                    arrayOf("error")
            ),
            E(
                    "web-view",
                    arrayOf(A("src", arrayOf(T.STRING))),
                    arrayOf("message", "load", "error")
            ),
            E(
                    "navigation-bar",
                    arrayOf(
                            A("title", arrayOf(T.STRING)),
                            A("loading", arrayOf(T.BOOLEAN), false),
                            A("front-color", arrayOf(T.STRING)),
                            A("background-color", arrayOf(T.STRING)),
                            A("color-animation-duration", arrayOf(T.NUMBER), 0),
                            A("color-animation-timing-func", arrayOf(T.STRING, T.NUMBER), "linear")
                    )
            ),
            E(
                    "page-meta",
                    arrayOf(
                            A("background-text-style",arrayOf(T.STRING)),
                            A("background-color",arrayOf(T.STRING)),
                            A("background-color-top",arrayOf(T.STRING)),
                            A("background-color-bottom",arrayOf(T.STRING)),
                            A("scroll-top",arrayOf(T.STRING)),
                            A("scroll-duration", arrayOf(T.NUMBER), 300),
                            A("page-style",arrayOf(T.STRING)),
                            A("root-font-size",arrayOf(T.STRING))
                    ),
                    arrayOf("resize","scroll","scrollone")
            )
    )

    val COMMON_ELEMENT_ATTRIBUTE_DESCRIPTORS = arrayOf(
            A("id", arrayOf(T.STRING)),
            A("class", arrayOf(T.STRING)),
            A("style", arrayOf(T.STRING)),
            A("hidden", arrayOf(T.BOOLEAN), false)
    )

    val COMMON_ELEMENT_EVENTS = arrayOf(
            "touchstart", "touchmove", "touchcancel", "touchend", "tap", "longpress", "longtap", "transitionend",
            "animationstart", "animationiteration", "animationend", "touchforcechange"
    )

    val INNER_ELEMENT_NAMES = arrayOf("text")

    val ARIA_ATTRIBUTE = arrayOf(
            "aria-hidden", "aria-role", "aria-label", "aria-checked", "aria-disabled",
            "aria-describedby", "aria-expanded", "aria-haspopup", "aria-selected", "aria-required",
            "aria-orientation", "aria-valuemin", "aria-valuemax", "aria-valuenow", "aria-readonly",
            "aria-multiselectable", "aria-controls", "tabindex", "aria-labelledby", "aria-orientation",
            "aria-multiselectable", "aria-labelledby"
    )

    val NATIVE_COMPONENTS = arrayOf(
            "camera", "canvas", "input", "live-player", "live-pusher", "map", "textarea", "video"
    )
}