/*
 * Copyright 2010-2023 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.gradle.native

import org.gradle.api.logging.LogLevel
import org.gradle.testkit.runner.BuildResult
import org.gradle.util.GradleVersion
import org.jetbrains.kotlin.gradle.native.GeneralNativeIT.Companion.containsSequentially
import org.jetbrains.kotlin.gradle.testbase.*
import org.jetbrains.kotlin.gradle.util.replaceFirst
import org.jetbrains.kotlin.gradle.utils.NativeCompilerDownloader
import org.jetbrains.kotlin.konan.target.HostManager
import org.jetbrains.kotlin.konan.target.KonanTarget
import org.jetbrains.kotlin.konan.target.Xcode
import org.jetbrains.kotlin.konan.target.XcodeVersion
import org.jetbrains.kotlin.konan.target.presetName
import org.jetbrains.kotlin.konan.util.DependencyDirectories
import org.junit.jupiter.api.Assumptions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.condition.DisabledOnOs
import org.junit.jupiter.api.condition.OS
import kotlin.io.path.appendText
import kotlin.test.assertTrue

@DisabledOnOs(
    OS.WINDOWS, disabledReason =
    " This test class causes build timeouts on Windows CI machines. " +
            "We temporarily disable it for windows until a proper fix is found."
)
@DisplayName("Tests for K/N builds with native downloading and platform libs")
@NativeGradlePluginTests
class NativeDownloadAndPlatformLibsIT : KGPBaseTest() {

    companion object {
        private const val KOTLIN_SPACE_DEV = "https://cache-redirector.jetbrains.com/maven.pkg.jetbrains.space/kotlin/p/kotlin/dev"
        private const val MAVEN_CENTRAL = "https://cache-redirector.jetbrains.com/maven-central"
    }

    private val platformName: String = HostManager.platformName()
    private val currentCompilerVersion = NativeCompilerDownloader.DEFAULT_KONAN_VERSION

    private fun platformLibrariesProject(
        vararg targets: String,
        gradleVersion: GradleVersion,
        test: TestProject.() -> Unit = {},
    ) {
        nativeProject("native-platform-libraries", gradleVersion) {
            buildGradleKts.appendText(
                targets.joinToString(prefix = "\n", separator = "\n") {
                    "kotlin.$it()"
                }
            )
            configureJvmMemory()
            test()
        }
    }

    private fun TestProject.buildWithLightDist(vararg tasks: String, assertions: BuildResult.() -> Unit) =
        build(*tasks, "-Pkotlin.native.distribution.type=light", assertions = assertions)

    @BeforeEach
    fun deleteInstalledCompilers() {
        val currentCompilerDir = DependencyDirectories.localKonanDir
            .resolve("kotlin-native-$platformName-$currentCompilerVersion")
        val prebuiltDistDir = DependencyDirectories.localKonanDir
            .resolve("kotlin-native-prebuilt-$platformName-$currentCompilerVersion")

        for (compilerDirectory in listOf(currentCompilerDir, prebuiltDistDir)) {
            compilerDirectory.deleteRecursively()
        }
    }

    @DisplayName("K/N distribution without platform libraries generation")
    @GradleTest
    fun testNoGenerationByDefault(gradleVersion: GradleVersion) {
        platformLibrariesProject("linuxX64", gradleVersion = gradleVersion) {
            build("assemble") {
                assertOutputContains("Kotlin/Native distribution: .*kotlin-native-prebuilt-$platformName".toRegex())
                assertOutputDoesNotContain("Generate platform libraries for ")
            }
        }
    }

    @DisplayName("K/N distribution with platform libraries generation")
    @GradleTest
    fun testLibrariesGeneration(gradleVersion: GradleVersion) {
        nativeProject("native-platform-libraries", gradleVersion = gradleVersion) {

            includeOtherProjectAsSubmodule("native-platform-libraries", "", "subproject", true)
            configureJvmMemory()

            buildGradleKts.appendText("\nkotlin.linuxX64()\n")
            subProject("subproject").buildGradleKts.appendText("\nkotlin.linuxArm64()\n")

            // Check that platform libraries are correctly generated for both root project and a subproject.
            buildWithLightDist("assemble") {
                assertOutputContains("Kotlin/Native distribution: .*kotlin-native-$platformName".toRegex())
                assertOutputContains("Generate platform libraries for linux_x64")
                assertOutputContains("Generate platform libraries for linux_arm64")
            }

            // Check that we don't generate libraries during a second run. Don't clean to reduce execution time.
            buildWithLightDist("assemble") {
                assertOutputDoesNotContain("Generate platform libraries for ")
            }
        }
    }

    @DisplayName("Link with args via gradle properties")
    @GradleTest
    fun testLinkerArgsViaGradleProperties(gradleVersion: GradleVersion) {
        nativeProject("native-platform-libraries", gradleVersion = gradleVersion) {

            configureJvmMemory()
            addPropertyToGradleProperties(
                "kotlin.native.linkArgs",
                mapOf(
                    "-Xfoo" to "-Xfoo=bar",
                    "-Xbaz" to "-Xbaz=qux"
                )
            )

            buildGradleKts.appendText(
                """
                |
                |kotlin.linuxX64() {
                |    binaries.sharedLib {
                |        freeCompilerArgs += "-Xmen=pool"
                |    }
                |}
                """.trimMargin()
            )

            build("linkDebugSharedLinuxX64", buildOptions = defaultBuildOptions.copy(logLevel = LogLevel.DEBUG)) {
                assertTasksExecuted(
                    ":compileKotlinLinuxX64",
                    ":linkDebugSharedLinuxX64"
                )
                assertNativeTasksCommandLineArguments(":linkDebugSharedLinuxX64") {
                    assertCommandLineArgumentsContain(
                        "-Xfoo=bar", "-Xbaz=qux", "-Xmen=pool",
                        commandLineArguments = it
                    )
                }
                assertFileInProjectExists("build/bin/linuxX64/debugShared/libnative_platform_libraries.so")
                assertFileInProjectExists("build/bin/linuxX64/debugShared/libnative_platform_libraries_api.h")
            }
        }
    }

    @DisabledOnOs(OS.MAC)
    @DisplayName("Assembling project generates no platform libraries for unsupported host")
    @GradleTest
    fun testNoGenerationForUnsupportedHost(gradleVersion: GradleVersion) {
        platformLibrariesProject(KonanTarget.IOS_X64.presetName, gradleVersion = gradleVersion) {
            buildWithLightDist("assemble") {
                assertOutputDoesNotContain("Generate platform libraries for ")
            }
        }
    }

    @DisplayName("Build K/N project with prebuild type")
    @GradleTest
    fun testCanUsePrebuiltDistribution(gradleVersion: GradleVersion) {
        platformLibrariesProject("linuxX64", gradleVersion = gradleVersion) {
            build("assemble", "-Pkotlin.native.distribution.type=prebuilt") {
                assertOutputContains("Kotlin/Native distribution: .*kotlin-native-prebuilt-$platformName".toRegex())
                assertOutputDoesNotContain("Generate platform libraries for ")
            }
        }
    }

    @DisplayName("Build K/N project with restrictedDistribution turned on")
    @GradleTest
    fun testDeprecatedRestrictedDistributionProperty(gradleVersion: GradleVersion) {
        platformLibrariesProject("linuxX64", gradleVersion = gradleVersion) {
            // We allow using this deprecated property for 1.4 too. Just download the distribution without platform libs in this case.
            build("tasks", "-Pkotlin.native.restrictedDistribution=true") {
                assertOutputContains("Warning: Project property 'kotlin.native.restrictedDistribution' is deprecated. Please use 'kotlin.native.distribution.type=light' instead")
                assertOutputContains("Kotlin/Native distribution: .*kotlin-native-$platformName".toRegex())
            }
        }
    }

    @DisplayName("Build K/N project with compiler reinstallation")
    @GradleTest
    fun testCompilerReinstallation(gradleVersion: GradleVersion) {
        platformLibrariesProject("linuxX64", gradleVersion = gradleVersion) {
            // Install the compiler at the first time. Don't build to reduce execution time.
            buildWithLightDist("tasks") {
                assertOutputContains("Generate platform libraries for linux_x64")
            }

            // Reinstall the compiler.
            buildWithLightDist("tasks", "-Pkotlin.native.reinstall=true") {
                assertOutputContains("Unpack Kotlin/Native compiler to ")
                assertOutputContains("Generate platform libraries for linux_x64")
            }
        }
    }

    private fun mavenUrl(): String {
        val versionPattern = "(\\d+)\\.(\\d+)(?:\\.(\\d+))?(?:-(\\p{Alpha}*\\p{Alnum}|[\\p{Alpha}-]*))?(?:-(\\d+))?".toRegex()
        val (_, _, _, metaString, build) = versionPattern.matchEntire(currentCompilerVersion)?.destructured
            ?: error("Unable to parse version $currentCompilerVersion")
        return when {
            metaString == "dev" || build.isNotEmpty() -> KOTLIN_SPACE_DEV
            metaString in listOf("RC", "RC2", "Beta") || metaString.isEmpty() -> MAVEN_CENTRAL
            else -> throw IllegalStateException("Not a published version $currentCompilerVersion")
        }
    }

    @DisplayName("Download prebuilt Native bundle with maven")
    @GradleTest
    fun shouldDownloadPrebuiltNativeBundleWithMaven(gradleVersion: GradleVersion) {
        val maven = mavenUrl()
        // Don't run this test for build that are not yet published to central
        Assumptions.assumeTrue(maven != MAVEN_CENTRAL)

        nativeProject("native-download-maven", gradleVersion = gradleVersion) {

            addPropertyToGradleProperties(
                "kotlin.native.distribution.downloadFromMaven",
                mapOf("true" to "true")
            )

            buildGradleKts.replaceFirst("// <MavenPlaceholder>", "maven(\"${maven}\")")

            build("assemble") {
                assertOutputContains("Unpack Kotlin/Native compiler to ")
                assertOutputDoesNotContain("Generate platform libraries for ")
            }
        }
    }

    @DisplayName("Download light Native bundle with maven")
    @GradleTest
    fun shouldDownloadLightNativeBundleWithMaven(gradleVersion: GradleVersion) {
        val maven = mavenUrl()
        // Don't run this test for build that are not yet published to central
        Assumptions.assumeTrue(maven != MAVEN_CENTRAL)

        if (HostManager.hostIsMac) {
            // Building platform libs require Xcode 14.1
            Assumptions.assumeTrue(Xcode.findCurrent().version >= XcodeVersion(14, 1))
        }

        nativeProject("native-download-maven", gradleVersion = gradleVersion) {

            addPropertyToGradleProperties("kotlin.native.distribution.downloadFromMaven", mapOf("true" to "true"))

            buildGradleKts.replaceFirst("// <MavenPlaceholder>", "maven(\"${maven}\")")

            build("assemble", "-Pkotlin.native.distribution.type=light") {
                assertOutputContains("Unpack Kotlin/Native compiler to ")
                assertOutputContains("Generate platform libraries for ")
            }
        }
    }

    @DisplayName("Download from maven should fail if there is no such build in the default repos")
    @GradleTest
    fun shouldFailDownloadWithNoBuildInDefaultRepos(gradleVersion: GradleVersion) {
        nativeProject("native-download-maven", gradleVersion = gradleVersion) {

            addPropertyToGradleProperties(
                "kotlin.native.version",
                mapOf("1.8.0-dev-1234" to "1.8.0-dev-1234")
            )
            addPropertyToGradleProperties(
                "kotlin.native.distribution.downloadFromMaven",
                mapOf("true" to "true")
            )

            buildAndFail("assemble") {
                assertOutputContains("Could not find org.jetbrains.kotlin:kotlin-native")
            }
        }
    }
}
