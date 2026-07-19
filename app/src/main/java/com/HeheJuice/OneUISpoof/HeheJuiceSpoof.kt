package com.HeheJuice.OneUISpoof

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam
import de.robv.android.xposed.XC_MethodHook

class HeheJuiceSpoof : IXposedHookLoadPackage {

    override fun handleLoadPackage(lpparam: LoadPackageParam) {
        // Run globally across all package frameworks
        try {
            // Passing 'null' targets the core system boot classloader instead of app-specific paths
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
        } catch (t: Throwable) {
            // Silently absorb mapping exceptions on clean framework hooks
        }
    }
}
