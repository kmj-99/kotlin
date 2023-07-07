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

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":library"))
            }
        }
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinNativeLink>().configureEach {
    toolOptions.verbose.set(true)
}