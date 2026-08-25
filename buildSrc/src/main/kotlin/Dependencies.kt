import org.gradle.api.artifacts.dsl.DependencyHandler

/**
 * Common dependency configurations for different build variants
 */
object DependencyConfigs {
    // OSS variant common dependencies
    fun DependencyHandler.addOssDependencies() {
        add("ossImplementation", project(":plugin:api"))
        add("ossImplementation", project(":library:proto-stub"))
        add("ossImplementation", libs.kotlinx.coroutines.android)
        add("ossImplementation", libs.core.ktx)
        add("ossImplementation", libs.activity.ktx)
        add("ossImplementation", libs.fragment.ktx)
        add("ossImplementation", libs.camera.view)
        add("ossImplementation", libs.camera.lifecycle)
        add("ossImplementation", libs.camera.camera2)
        add("ossImplementation", libs.appcompat)
        add("ossImplementation", libs.work.runtime.ktx)
        add("ossImplementation", libs.work.multiprocess)
        add("ossImplementation", libs.room.runtime)
        add("ossImplementation", libs.room.ktx)
        add("ossImplementation", libs.material)
    }

    // OSS variant KSP dependencies
    fun DependencyHandler.addOssKspDependencies() {
        add("kspOss", libs.room.compiler)
    }

    // Legacy variant common dependencies (minSdk21)
    fun DependencyHandler.addLegacyDependencies() {
        add("legacyImplementation", libs.core.ktx.minSdk21)
        add("legacyImplementation", libs.activity.ktx.minSdk21)
        add("legacyImplementation", libs.fragment.ktx.minSdk21)
        add("legacyImplementation", libs.camera.view.minSdk21)
        add("legacyImplementation", libs.camera.lifecycle.minSdk21)
        add("legacyImplementation", libs.camera.camera2.minSdk21)
        add("legacyImplementation", libs.appcompat.minSdk21)
        add("legacyImplementation", libs.work.runtime.ktx.minSdk21)
        add("legacyImplementation", libs.work.multiprocess.minSdk21)
        add("legacyImplementation", libs.room.runtime.minSdk21)
        add("legacyImplementation", libs.room.ktx.minSdk21)
        add("legacyImplementation", libs.material.minSdk21)
    }

    // Legacy variant KSP dependencies
    fun DependencyHandler.addLegacyKspDependencies() {
        add("kspLegacy", libs.room.compiler.minSdk21)
    }

    // Universal dependencies (used by all variants)
    fun DependencyHandler.addUniversalDependencies() {
        add("implementation", fileTree("libs"))
        add("implementation", project(":plugin:api"))
        add("implementation", project(":library:proto-stub"))
        add("implementation", libs.kotlinx.coroutines.android)
        add("implementation", libs.swiperefreshlayout)
        add("implementation", libs.preference)
        add("implementation", libs.flexbox)
        add("implementation", libs.gson)
        add("implementation", libs.zxing.core)
        add("implementation", libs.snakeyaml)
        add("implementation", libs.material.about.library)
        add("implementation", libs.process.phoenix)
        add("implementation", libs.kryo)
        add("implementation", libs.jini.lib)
        add("implementation", libs.markwon.core)
        add("implementation", libs.recyclerview.fastscroll)
        add("implementation", libs.editorkit)
        add("implementation", libs.editorkit.language.json)
        add("coreLibraryDesugaring", libs.desugar.jdk.libs)
    }

    // Recyclerview fastscroll exclusions
    fun DependencyHandler.addRecyclerViewFastScroll() {
        add("implementation", libs.recyclerview.fastscroll) {
            exclude(group = "androidx.recyclerview")
            exclude(group = "androidx.appcompat")
        }
    }
}
