plugins {
    kotlin("jvm")
    id("application")
    id("com.github.johnrengelman.shadow")
}

description = "Compiler Daemon that includes the Compose plugin"
group = "org.jetbrains.kotlin.experimental.compose"

kotlin.jvmToolchain(11)

application {
    mainClass.set("androidx.compose.compiler.daemon.MainKt")
}

dependencies {
    implementation(project(":plugins:compose-compiler-plugin:compiler-hosted"))
    implementation(project(":compiler:cli"))
    implementation(project(":kotlin-build-common"))
    implementation(project(":compiler:incremental-compilation-impl"))
}

val enableComposePublish = findProperty("kotlin.build.compose.publish.enabled") as String? == "true"
if (enableComposePublish) {
    publish {
        pom {
            name.set("Compose Compiler Daemon")
            developers {
                developer {
                    name.set("The Android Open Source Project")
                }
            }
        }
    }
}

standardPublicJars()

