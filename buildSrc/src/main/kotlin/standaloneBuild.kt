/*
 * Copyright 2010-2019 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license
 * that can be found in the license/LICENSE.txt file.
 */

@file:JvmName("UltimateBuildSrc")
@file:JvmMultifileClass

package org.jetbrains.kotlin.ultimate

import org.gradle.api.Project
import org.gradle.api.artifacts.ExternalModuleDependency
import org.gradle.kotlin.dsl.*

internal val Project.includeKotlinUltimate
    get() = rootProject.findProperty("includeKotlinUltimate")?.toString()?.toBoolean() == true

private val EXCLUDES_LIST_FROM_IDEA_PLUGIN = listOf(
        "lib/android-*.jar", // no need Android stuff
        "lib/kapt3-*.jar", // no annotation processing
        "lib/jps/**", // JSP plugin
        "kotlinc/**"
)

fun Project.ideaPluginJarDep(): Any {
    return if (includeKotlinUltimate) {
        // depend on the artifact to be build
        dependencies.project(":prepare:idea-plugin", configuration = "runtimeJar")
    } else {
        // reuse JAR artifact from downloaded plugin
        val ideaPluginForCidrDir: String by rootProject.extra
        fileTree(ideaPluginForCidrDir) {
            include("lib/kotlin-plugin.jar")
        }
    }
}

fun Project.addIdeaNativeModuleDeps() {
    dependencies {

        if (includeKotlinUltimate) {
            // Gradle projects with Kotlin/Native-specific logic
            // (automatically brings all the necessary transient dependencies, include deps on IntelliJ platform)
            add("compile", project(":idea:idea-native"))
            add("compile", project(":idea:idea-gradle-native"))

            // Detect IDE name and version
            val ideName = if (rootProject.findProperty("intellijUltimateEnabled")?.toString()?.toBoolean() == true) "ideaIU" else "ideaIC" // TODO: what if AndroidStudio?
            val ideVersion = rootProject.extra["versions.intellijSdk"] as String

            // Java APIs (from Big Kotlin project)
            val javaApis = add("compile", "kotlin.build:$ideName:$ideVersion") as ExternalModuleDependency
            with (javaApis) {
                artifact {
                    name = "java-api"
                    type = "jar"
                    extension = "jar"
                }
                artifact {
                    name = "java-impl"
                    type = "jar"
                    extension = "jar"
                }
                isTransitive = false
            }
        } else {
            // contents of Kotlin plugin
            val ideaPluginForCidrDir: String by rootProject.extra
            val ideaPluginJars = fileTree(ideaPluginForCidrDir) {
                exclude(EXCLUDES_LIST_FROM_IDEA_PLUGIN)
            }
            add("compile", ideaPluginJars)

            // IntelliJ platform (out of CIDR IDE distribution)
            val cidrIdeDir: String by rootProject.extra
            val cidrPlatform = fileTree(cidrIdeDir) {
                include("lib/*.jar")
                exclude("lib/kotlin*.jar") // because Kotlin should be taken from Kotlin plugin
                exclude("lib/clion*.jar") // don't take scrambled JARs
                exclude("lib/appcode*.jar")
            }
            add("compile", cidrPlatform)

            // standard CIDR plugins
            val cidrPlugins = fileTree(cidrIdeDir) {
                include("plugins/cidr-*/lib/*.jar")
                include("plugins/gradle/lib/*.jar")
            }
            add("compile", cidrPlugins)

            // Java APIs (private artifact that goes together with CIDR IDEs)
            val cidrPlatformDepsDir: String by rootProject.extra
            val cidrPlatformDeps = fileTree(cidrPlatformDepsDir) { include(PLATFORM_DEPS_JAR_NAME) }
            add("compile", cidrPlatformDeps)
        }
    }
}
