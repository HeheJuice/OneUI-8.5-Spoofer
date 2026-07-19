package com.HeheJuice.OneUISpoof

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam
import de.robv.android.xposed.XC_MethodHook

class HeheJuiceSpoof : IXposedHookLoadPackage {

    override fun handleLoadPackage(lpparam: LoadPackageParam) {
        if (lpparam.packageName == null) return

        // ---- LAYER 1: System Properties Spoofing (The Props) ----
        try {
            val systemPropertiesClass = XposedHelpers.findClass("android.os.SystemProperties", null)

            // Hook String get(String key)
            XposedHelpers.findAndHookMethod(
                systemPropertiesClass,
                "get",
                String::class.java,
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        val key = param.args[0] as? String ?: return
                        if (key == "ro.build.version.oneui") {
                            param.result = "80500" // Spoofs OneUI 8.5
                        } else if (key == "ro.build.version.sep") {
                            param.result = "160500" // Matches corresponding Samsung Extension Platform
                        }
                    }
                }
            )

            // Hook String get(String key, String def)
            XposedHelpers.findAndHookMethod(
                systemPropertiesClass,
                "get",
                String::class.java,
                String::class.java,
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        val key = param.args[0] as? String ?: return
                        if (key == "ro.build.version.oneui") {
                            param.result = "80500"
                        } else if (key == "ro.build.version.sep") {
                            param.result = "160500"
                        }
                    }
                }
            )
        } catch (t: Throwable) {}

        // ---- LAYER 2: Package Manager Feature Spoofing ----
        try {
            XposedHelpers.findAndHookMethod(
                "android.app.ApplicationPackageManager",
                null, 
                "hasSystemFeature",
                String::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        val featureName = param.args[0] as? String ?: return
                        if (featureName.startsWith("com.samsung.android.oneui.version")) {
                            param.result = true
                        }
                    }
                }
            )

            XposedHelpers.findAndHookMethod(
                "android.app.ApplicationPackageManager",
                null,
                "hasSystemFeature",
                String::class.java,
                Int::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        val featureName = param.args[0] as? String ?: return
                        if (featureName.startsWith("com.samsung.android.oneui.version")) {
                            param.result = true
                        }
                    }
                }
            )
        } catch (t: Throwable) {}
    }
}
