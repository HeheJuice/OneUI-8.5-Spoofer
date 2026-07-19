package com.HeheJuice.OneUISpoof

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam
import de.robv.android.xposed.XC_MethodHook
import java.io.ByteArrayInputStream
import java.io.File

class HeheJuiceETCSpoof : IXposedHookLoadPackage {

    private val customXmlContent = """
        <?xml version="1.0" encoding="utf-8"?>
        <permissions>
            <feature name="com.samsung.android.oneui.version" version="80500" />
            <feature name="com.samsung.android.oneui.version.10000" />
            <feature name="com.samsung.android.oneui.version.10100" />
            <feature name="com.samsung.android.oneui.version.10200" />
            <feature name="com.samsung.android.oneui.version.10500" />
            <feature name="com.samsung.android.oneui.version.20000" />
            <feature name="com.samsung.android.oneui.version.20100" />
            <feature name="com.samsung.android.oneui.version.20500" />
            <feature name="com.samsung.android.oneui.version.30000" />
            <feature name="com.samsung.android.oneui.version.30100" />
            <feature name="com.samsung.android.oneui.version.30101" />
            <feature name="com.samsung.android.oneui.version.40000" />
            <feature name="com.samsung.android.oneui.version.40100" />
            <feature name="com.samsung.android.oneui.version.40101" />
            <feature name="com.samsung.android.oneui.version.50000" />
            <feature name="com.samsung.android.oneui.version.50100" />
            <feature name="com.samsung.android.oneui.version.50101" />
            <feature name="com.samsung.android.oneui.version.60000" />
            <feature name="com.samsung.android.oneui.version.60100" />
            <feature name="com.samsung.android.oneui.version.60101" />
            <feature name="com.samsung.android.oneui.version.70000" />
            <feature name="com.samsung.android.oneui.version.80000" />
            <feature name="com.samsung.android.oneui.version.80500" />
        </permissions>
    """.trimIndent()

    override fun handleLoadPackage(lpparam: LoadPackageParam) {
        if (lpparam.packageName == null) return

        val targetPath = "/system/etc/permissions/com.samsung.android.oneui.version.xml"

        // 1. Mock File.exists()
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
        } catch (e: Throwable) {}

        // 2. Intercept app-level parsing by mocking internal file reads safely
        try {
            XposedHelpers.findAndHookMethod(
                File::class.java,
                "getAbsolutePath",
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        val path = param.result as? String ?: return
                        if (path == targetPath) {
                            // Keeps the app targeted on our simulated file path
                        }
                    }
                }
            )
        } catch (e: Throwable) {}
    }
}
