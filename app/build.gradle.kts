plugins {
    id("com.android.application")
    id("kotlin-parcelize")
    alias(libs.plugins.protobuf)
    alias(libs.plugins.ksp)
    alias(libs.plugins.aboutlibraries)
}

setupApp()

android {
    namespace = "io.nekohasekai.sagernet"
}

ksp {
    arg("room.incremental", "true")
    arg("room.schemaLocation", "$projectDir/schemas")
}

aboutLibraries {
    offlineMode = true
    collect {
        configPath = file("src/main/aboutlibraries/config")
        includePlatform = true
    }
    export {
        excludeFields.addAll("name", "description", "developers", "funding", "licenses", "organization", "scm", "website", "License")
        prettyPrint = true
    }
    exports {
        create("ossRelease") {
            outputFile = file("src/main/aboutlibraries/aboutlibraries.json")
        }
        create("legacyRelease") {
            outputFile = file("src/main/aboutlibraries/aboutlibraries_legacy.json")
        }
    }
}

dependencies {
    // Universal dependencies
    DependencyConfigs.addUniversalDependencies()
    DependencyConfigs.addRecyclerViewFastScroll()

    // OSS variant dependencies
    DependencyConfigs.addOssDependencies()
    DependencyConfigs.addOssKspDependencies()

    // Legacy variant dependencies (minSdk21)
    DependencyConfigs.addLegacyDependencies()
    DependencyConfigs.addLegacyKspDependencies()
}
