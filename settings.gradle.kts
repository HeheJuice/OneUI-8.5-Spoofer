pluginManagement {
    repositories {
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // Adds the required legacy repository mirror for Xposed API artifacts
        maven { url = java.net.URI("https://api.bintray.com/maven/rovo89/xposed/api") }
    }
}

rootProject.name = "PropSpoofer"
include(":app")
