package com.HeheJuice.OneUISpoof

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam
import de.robv.android.xposed.XC_MethodHook

class HeheJuiceSpoof : IXposedHookLoadPackage {

    override fun handleLoadPackage(lpparam: LoadPackageParam) {
        // Guard rails: Only run if the app package is valid
        if (lpparam.packageName == null) return

        // ---- LAYER 1: Package Manager Feature Spoofing (Using local classloader) ----
        try {
            XposedHelpers.findAndHookMethod(
                "android.app.ApplicationPackageManager",
                lpparam.classLoader, // Back to using the local app classloader that worked!
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
                lpparam.classLoader,
                "hasSystemFeature",
                String::class.java,
                Int::class.java, // Standard integer primitive signature mapping
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

        // ---- LAYER 2: System Properties String Spoofing ----
        try {
            val systemPropertiesClass = XposedHelpers.findClass("android.os.SystemProperties", lpparam.classLoader)

            XposedHelpers.findAndHookMethod(
                systemPropertiesClass,
                "get",
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
    }
}
