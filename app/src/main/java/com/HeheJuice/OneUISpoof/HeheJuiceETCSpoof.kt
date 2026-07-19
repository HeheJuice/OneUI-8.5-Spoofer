package com.HeheJuice.OneUISpoof

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam
import de.robv.android.xposed.XC_MethodHook
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileInputStream

class HeheJuiceETCSpoof : IXposedHookLoadPackage {

    // Your comprehensive target list mocked directly into the memory stream payload
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

        // ---- PATCH 1: Mock File.exists() ----
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

        // ---- PATCH 2: Mock FileInputStream reading ----
        try {
            XposedHelpers.findAndHookConstructor(
                FileInputStream::class.java,
                File::class.java,
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        val file = param.args[0] as? File
                        if (file?.absolutePath == targetPath) {
                            XposedHelpers.setObjectField(param.thisObject, "path", targetPath)
                            // Optional: inject custom stream manipulation logic here if needed
                        }
                    }
                }
            )
        } catch (e: Throwable) {}
    }
}
