package com.HeheJuice.OneUISpoof

import android.os.Build
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam

class HeheJuiceSpoof : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: LoadPackageParam) {
        // Only spoof for official Samsung package targets to prevent system instability
        if (!lpparam.packageName.startsWith("com.samsung.")) return

        try {
            // 1. Hook the standard hidden One UI Version property field
            XposedHelpers.setStaticIntField(Build.VERSION::class.java, "SEM_PLATFORM_INT", 170500) 
            
            // 2. Hook system property checks if the app looks up properties directly
            // (One UI 8.5 corresponds roughly to SEM Platform value 170500 or version property 80500)
            XposedHelpers.findAndHookMethod(
                "android.os.SystemProperties", 
                lpparam.classLoader, 
                "get", 
                String::class.java, 
                object : de.robv.android.xposed.XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        val key = param.args[0] as String
                        if (key == "ro.build.version.oneui" || key == "ro.build.version.sep") {
                            param.result = "80500" // Identifies as One UI 8.5
                        }
                    }
                }
            )
        } catch (e: Throwable) {
            // Gracefully ignore if a specific app doesn't contain the platform field
        }
    }
}
