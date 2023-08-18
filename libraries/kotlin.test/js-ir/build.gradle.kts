import org.jetbrains.kotlin.gradle.dsl.KotlinCompile

plugins {
    kotlin("multiplatform")
}

val commonMainSources by task<Sync> {
    from(
        "$rootDir/libraries/kotlin.test/common/src/main",
        "$rootDir/libraries/kotlin.test/annotations-common/src/main"
    )
    into(layout.buildDirectory.dir("commonMainSources"))
}

val commonTestSources by task<Sync> {
    from("$rootDir/libraries/kotlin.test/common/src/test/kotlin")
    into(layout.buildDirectory.dir("commonTestSources"))
}

val jsMainSources by task<Sync> {
    from("$rootDir/libraries/kotlin.test/js/src")
    into(layout.buildDirectory.dir("jsMainSources"))
}

kotlin {
    js(IR) {
        nodejs()
    }

    sourceSets {
        named("commonMain") {
            dependencies {
                api(kotlinStdlib("mpp"))
            }
            kotlin.srcDir(commonMainSources.get().destinationDir)
        }
        named("commonTest") {
            kotlin.srcDir(commonTestSources.get().destinationDir)
        }
        named("jsMain") {
            kotlin.srcDir(jsMainSources.get().destinationDir)
        }
    }
}

tasks.withType<KotlinCompile<*>>().configureEach {
    kotlinOptions.freeCompilerArgs += listOf(
        "-Xallow-kotlin-package",
        "-opt-in=kotlin.ExperimentalMultiplatform",
        "-opt-in=kotlin.contracts.ExperimentalContracts"
    )
}

tasks.named("compileKotlinJs") {
    (this as KotlinCompile<*>).kotlinOptions.freeCompilerArgs += "-Xir-module-name=kotlin-test"
    dependsOn(commonMainSources)
    dependsOn(jsMainSources)
}

tasks.named("compileTestKotlinJs") {
    dependsOn(commonTestSources)
}

