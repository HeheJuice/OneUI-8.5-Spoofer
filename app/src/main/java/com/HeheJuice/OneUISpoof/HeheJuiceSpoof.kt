package com.HeheJuice.OneUISpoof

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam
import de.robv.android.xposed.XC_MethodHook
import java.io.File

class HeheJuiceSpoof : IXposedHookLoadPackage {

    override fun handleLoadPackage(lpparam: LoadPackageParam) {
        val packageName = lpparam.packageName ?: return
        
        // Router: Detect if the app is a native Samsung/Sec application
        val isSamsungApp = packageName.startsWith("com.samsung.") || packageName.startsWith("com.sec.")

        // =========================================================
        // LAYER 1: UNIVERSAL HOOKS (For ALL Apps)
        // =========================================================
        
        // 1A. Spoof Standard SystemProperties (Strings & Integers)
        try {
            val systemPropertiesClass = XposedHelpers.findClass("android.os.SystemProperties", lpparam.classLoader)

            val propHookString = object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    val key = param.args[0] as? String ?: return
                    if (key == "ro.build.version.oneui") param.result = "80500" 
                    if (key == "ro.build.version.sep") param.result = "160500"
                }
            }
            XposedHelpers.findAndHookMethod(systemPropertiesClass, "get", String::class.java, propHookString)
            XposedHelpers.findAndHookMethod(systemPropertiesClass, "get", String::class.java, String::class.java, propHookString)

            val propHookInt = object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    val key = param.args[0] as? String ?: return
                    if (key == "ro.build.version.oneui") param.result = 80500
                    if (key == "ro.build.version.sep") param.result = 160500
                }
            }
            XposedHelpers.findAndHookMethod(systemPropertiesClass, "getInt", String::class.java, Int::class.javaPrimitiveType, propHookInt)
        } catch (t: Throwable) {}

        // 1B. Spoof PackageManager.hasSystemFeature
        try {
            val featureHook = object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    val featureName = param.args[0] as? String ?: return
                    if (featureName.startsWith("com.samsung.android.oneui.version")) {
                        param.result = true
                    }
                }
            }
            XposedHelpers.findAndHookMethod("android.app.ApplicationPackageManager", lpparam.classLoader, "hasSystemFeature", String::class.java, featureHook)
            XposedHelpers.findAndHookMethod("android.app.ApplicationPackageManager", lpparam.classLoader, "hasSystemFeature", String::class.java, Int::class.javaPrimitiveType, featureHook)
        } catch (t: Throwable) {}

        // 1C. Spoof Samsung's Hidden Static Build Variables
        try {
            val buildVersionClass = XposedHelpers.findClass("android.os.Build\$VERSION", lpparam.classLoader)
            // 160500 represents the SEP version matching OneUI 8.5
            XposedHelpers.setStaticIntField(buildVersionClass, "SEM_PLATFORM_INT", 160500)
            XposedHelpers.setStaticIntField(buildVersionClass, "SEM_INT", 160500)
        } catch (t: Throwable) {}

        // 1D. Spoof Samsung's Proprietary SemSystemProperties Wrapper
        try {
            val semSystemPropertiesClass = XposedHelpers.findClass("android.os.SemSystemProperties", lpparam.classLoader)
            
            val semPropHookString = object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    val key = param.args[0] as? String ?: return
                    if (key == "ro.build.version.oneui") param.result = "80500"
                    if (key == "ro.build.version.sep") param.result = "160500"
                }
            }
            XposedHelpers.findAndHookMethod(semSystemPropertiesClass, "get", String::class.java, semPropHookString)
            XposedHelpers.findAndHookMethod(semSystemPropertiesClass, "get", String::class.java, String::class.java, semPropHookString)

            val semPropHookInt = object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    val key = param.args[0] as? String ?: return
                    if (key == "ro.build.version.oneui") param.result = 80500
                    if (key == "ro.build.version.sep") param.result = 160500
                }
            }
            XposedHelpers.findAndHookMethod(semSystemPropertiesClass, "getInt", String::class.java, Int::class.javaPrimitiveType, semPropHookInt)
        } catch (t: Throwable) {}

        // =========================================================
        // LAYER 2: NON-SAMSUNG ONLY HOOKS (File Mocking)
        // =========================================================
        
        if (!isSamsungApp) {
            val targetPath = "/system/etc/permissions/com.samsung.android.oneui.version.xml"
            
            try {
                XposedHelpers.findAndHookMethod(
                    File::class.java,
                    "exists",
                    object : XC_MethodHook() {
                        override fun beforeHookedMethod(param: MethodHookParam) {
                            val file = param.thisObject as File
                            if (file.absolutePath == targetPath) {
                                param.result = true
                            }
                        }
                    }
                )
            } catch (t: Throwable) {}
        }
    }
}
