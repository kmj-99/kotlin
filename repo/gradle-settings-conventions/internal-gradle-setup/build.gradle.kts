plugins {
    `kotlin-dsl`
    kotlin("jvm")
    kotlin("plugin.serialization")
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.10.0")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.10.0")
}

kotlin.jvmToolchain(8)

tasks {
    test {
        useJUnitPlatform()
    }
}

gradlePlugin {
    plugins {
        create("internal-gradle-setup") {
            id = "internal-gradle-setup"
            implementationClass = "org.jetbrains.kotlin.build.InternalGradleSetupSettingsPlugin"
        }
    }
}