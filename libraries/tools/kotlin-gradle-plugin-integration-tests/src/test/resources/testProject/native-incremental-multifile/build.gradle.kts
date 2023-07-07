plugins {
    id("org.jetbrains.kotlin.multiplatform")
}

repositories {
    mavenLocal()
    mavenCentral()
}

kotlin {
    <SingleNativeTarget>("host") {
        binaries {
            executable {
                entryPoint = "main"
            }
        }
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinNativeLink>().configureEach {
    toolOptions.verbose.set(true)
}