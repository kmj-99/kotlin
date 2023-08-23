plugins {
    kotlin("jvm")
    id("jps-compatible")
}

dependencies {
    api(project(":compiler:ir.tree"))
    api(project(":compiler:ir.objcinterop"))
    api(project(":compiler:ir.serialization.common"))

    implementation(project(":core:compiler.common.native"))

    compileOnly(intellijCore())
}

sourceSets {
    "main" { projectDefault() }
}
