plugins {
    kotlin("jvm")
    id("jps-compatible")
}

configureKotlinCompileTasksGradleCompatibility()

dependencies {
    api(project(":compiler:incremental-compilation-api"))
    implementation(kotlinStdlib())
    compileOnly(project(":kotlin-build-common")) // not sure
}

publish()