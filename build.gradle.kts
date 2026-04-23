plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.composeHotReload) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
}

val iosDeveloperDir = providers.gradleProperty("iosDeveloperDir")
    .orElse(providers.environmentVariable("DEVELOPER_DIR"))
    .orElse("/Applications/Xcode.app/Contents/Developer")
val iosDeviceId = providers.gradleProperty("iosDeviceId")
    .orElse(providers.environmentVariable("IOS_DEVICE_ID"))
val iosAppBundleId = providers.gradleProperty("iosBundleId")
    .orElse("com.iam2kabhishek.notey.Notey")
val iosAppName = providers.gradleProperty("iosAppName")
    .orElse("Notey")
val iosProjectPath = "iosApp/iosApp.xcodeproj"
val iosScheme = "iosApp"
val iosDerivedDataPath = "${rootProject.rootDir}/.xcodebuild"

fun requireIosDeviceId(): String = iosDeviceId.orNull
    ?: throw GradleException(
        "Missing iOS device id. Set -PiosDeviceId=<udid> or IOS_DEVICE_ID in your environment. " +
            "Find your device id with: xcrun xctrace list devices"
    )

tasks.register<Exec>("iosDeviceBuild") {
    group = "ios"
    description = "Builds iosApp for a connected physical iPhone"
    notCompatibleWithConfigurationCache("Uses dynamic local iOS device parameters")
    doFirst {
        environment("DEVELOPER_DIR", iosDeveloperDir.get())
        commandLine(
            "xcodebuild",
            "-project", iosProjectPath,
            "-scheme", iosScheme,
            "-configuration", "Debug",
            "-destination", "id=${requireIosDeviceId()}",
            "-derivedDataPath", iosDerivedDataPath,
            "build"
        )
    }
}

tasks.register<Exec>("iosDeviceInstall") {
    group = "ios"
    description = "Installs built iosApp on a connected physical iPhone"
    notCompatibleWithConfigurationCache("Uses dynamic local iOS device parameters")
    dependsOn("iosDeviceBuild")
    doFirst {
        environment("DEVELOPER_DIR", iosDeveloperDir.get())
        commandLine(
            "xcrun",
            "devicectl",
            "device",
            "install",
            "app",
            "--device", requireIosDeviceId(),
            "$iosDerivedDataPath/Build/Products/Debug-iphoneos/${iosAppName.get()}.app"
        )
    }
}

tasks.register<Exec>("iosDeviceLaunch") {
    group = "ios"
    description = "Launches iosApp on a connected physical iPhone"
    notCompatibleWithConfigurationCache("Uses dynamic local iOS device parameters")
    doFirst {
        environment("DEVELOPER_DIR", iosDeveloperDir.get())
        commandLine(
            "xcrun",
            "devicectl",
            "device",
            "process",
            "launch",
            "--device", requireIosDeviceId(),
            "--terminate-existing",
            iosAppBundleId.get()
        )
    }
}

tasks.register("iosDeviceRun") {
    group = "ios"
    description = "Builds, installs, and launches iosApp on a physical iPhone"
    notCompatibleWithConfigurationCache("Depends on iOS device tasks that use local dynamic parameters")
    dependsOn("iosDeviceInstall")
    finalizedBy("iosDeviceLaunch")
}
