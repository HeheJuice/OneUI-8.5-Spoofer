package com.HeheJuice.OneUISpoof

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam

class MyHookImplementation : IXposedHookLoadPackage {
    
    override fun handleLoadPackage(lpparam: LoadPackageParam) {
        // You can filter for specific apps here if you don't want it running everywhere.
        // For example, to only target a specific app:
        // if (lpparam.packageName != "com.target.app") return

        XposedBridge.log("LSPosed: Injecting Build.prop spoofer into ${lpparam.packageName}")

        try {
            val systemPropertiesClass = XposedHelpers.findClass("android.os.SystemProperties", lpparam.classLoader)

            // 1. Hook SystemProperties.get(String key)
            XposedHelpers.findAndHookMethod(
                systemPropertiesClass,
                "get",
                String::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        val key = param.args[0] as String
                        when (key) {
                            "ro.build.version.sep" -> param.result = "170500"
                            "ro.build.version.oneui" -> param.result = "80500"
                        }
                    }
                }
            )

            // 2. Hook SystemProperties.get(String key, String def)
            XposedHelpers.findAndHookMethod(
                systemPropertiesClass,
                "get",
                String::class.java,
                String::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        val key = param.args[0] as String
                        when (key) {
                            "ro.build.version.sep" -> param.result = "170500"
                            "ro.build.version.oneui" -> param.result = "80500"
                        }
                    }
                }
            )

            // 3. Hook SystemProperties.getInt(String key, int def)
            XposedHelpers.findAndHookMethod(
                systemPropertiesClass,
                "getInt",
                String::class.java,
                Int::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        val key = param.args[0] as String
                        when (key) {
                            "ro.build.version.sep" -> param.result = 170500
                            "ro.build.version.oneui" -> param.result = 80500
                        }
                    }
                }
            )

        } catch (e: Throwable) {
            XposedBridge.log("LSPosed Prop Spoof Error: " + e.message)
        }
    }
}
