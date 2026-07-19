package com.HeheJuice.OneUISpoof

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam
import de.robv.android.xposed.XC_MethodHook

class HeheJuiceSpoof : IXposedHookLoadPackage {

    override fun handleLoadPackage(lpparam: LoadPackageParam) {
        // Prevent running on null packages or system-wide processes that don't need it
        if (lpparam.packageName == null) return

        try {
            XposedHelpers.findAndHookMethod(
                "android.app.ApplicationPackageManager",
                lpparam.classLoader,
                "hasSystemFeature",
                String::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        val featureName = param.args[0] as? String ?: return
                        
                        // Handle all variations of the feature string cleanly
                        if (featureName.startsWith("com.samsung.android.oneui.version")) {
                            param.result = true
                        }
                    }
                }
            )
        } catch (t: Throwable) {
            // Suppress framework initialization errors
        }

        // Alternative target used by secondary subsystems and permissions checks
        try {
            XposedHelpers.findAndHookMethod(
                "android.app.ApplicationPackageManager",
                lpparam.classLoader,
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
