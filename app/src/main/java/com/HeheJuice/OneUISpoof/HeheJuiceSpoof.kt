package com.HeheJuice.OneUISpoof

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam

class HeheJuiceSpoof : IXposedHookLoadPackage {

    // Spoofed values
    private val SPOOFED_SEP = "170500"
    private val SPOOFED_ONEUI = "80500"

    override fun handleLoadPackage(lpparam: LoadPackageParam) {
        // No filtering – LSPosed already ensures this module is only loaded into selected apps

        // Hook SystemProperties.get(String)
        XposedHelpers.findAndHookMethod(
            "android.os.SystemProperties",
            lpparam.classLoader,
            "get",
            String::class.java,
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    val key = param.args[0] as String
                    when (key) {
                        "ro.build.version.sep" -> param.result = SPOOFED_SEP
                        "ro.build.version.oneui" -> param.result = SPOOFED_ONEUI
                    }
                }
            }
        )

        // Hook SystemProperties.get(String, String) – with default value
        XposedHelpers.findAndHookMethod(
            "android.os.SystemProperties",
            lpparam.classLoader,
            "get",
            String::class.java,
            String::class.java,
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    val key = param.args[0] as String
                    when (key) {
                        "ro.build.version.sep" -> param.result = SPOOFED_SEP
                        "ro.build.version.oneui" -> param.result = SPOOFED_ONEUI
                    }
                }
            }
        )

        // Hook SystemProperties.getInt(String, Int)
        XposedHelpers.findAndHookMethod(
            "android.os.SystemProperties",
            lpparam.classLoader,
            "getInt",
            String::class.java,
            Int::class.javaPrimitiveType,
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    val key = param.args[0] as String
                    when (key) {
                        "ro.build.version.sep" -> param.result = SPOOFED_SEP.toInt()
                        "ro.build.version.oneui" -> param.result = SPOOFED_ONEUI.toInt()
                    }
                }
            }
        )

        // Hook SystemProperties.getLong(String, Long)
        XposedHelpers.findAndHookMethod(
            "android.os.SystemProperties",
            lpparam.classLoader,
            "getLong",
            String::class.java,
            Long::class.javaPrimitiveType,
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    val key = param.args[0] as String
                    when (key) {
                        "ro.build.version.sep" -> param.result = SPOOFED_SEP.toLong()
                        "ro.build.version.oneui" -> param.result = SPOOFED_ONEUI.toLong()
                    }
                }
            }
        )

        // Hook SystemProperties.getBoolean(String, Boolean) – if needed
        XposedHelpers.findAndHookMethod(
            "android.os.SystemProperties",
            lpparam.classLoader,
            "getBoolean",
            String::class.java,
            Boolean::class.javaPrimitiveType,
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    // Not needed for SEP/ONEUI, but safe to ignore
                }
            }
        )
    }
}