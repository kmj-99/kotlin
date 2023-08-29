/*
 * Copyright 2010-2023 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.gradle.native

import org.gradle.util.GradleVersion
import org.jetbrains.kotlin.gradle.dsl.NativeCacheKind
import org.jetbrains.kotlin.gradle.testbase.*
import org.jetbrains.kotlin.konan.target.HostManager
import org.jetbrains.kotlin.konan.target.KonanTarget
import org.junit.jupiter.api.DisplayName
import java.nio.file.Path
import kotlin.io.path.appendText
import kotlin.io.path.writeText
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@DisplayName("Tests for K/N incremental compilation")
@NativeGradlePluginTests
class NativeIncrementalCompilationIT : KGPBaseTest() {
    @DisplayName("Smoke test")
    @GradleTest
    fun checkIncrementalCacheIsCreated(gradleVersion: GradleVersion) {
        nativeProject("native-incremental-simple", gradleVersion) {
            build(
                "linkDebugExecutableHost",
                buildOptions = buildOptions.copy(
                    nativeOptions = BuildOptions.NativeOptions(
                        cacheKind = NativeCacheKind.STATIC,
                        incremental = true
                    )
                ),
            ) {
                assertDirectoryExists(
                    getFileCache("native-incremental-simple", "src/hostMain/kotlin/main.kt", "")
                )
            }
        }
    }

    @DisplayName("IC works after compilation error (test 1)")
    @GradleTest
    fun compilationError1(gradleVersion: GradleVersion) {
        nativeProject("native-incremental-multifile", gradleVersion) {
            var mainKtCacheModified = 0L
            var fooKtCacheModified = 0L
            val mainKtCache = getFileCache("native-incremental-multifile", "src/hostMain/kotlin/main.kt", "")
            val fooKtCache = getFileCache("native-incremental-multifile", "src/hostMain/kotlin/foo.kt", "")
            build(
                "linkDebugExecutableHost",
                buildOptions = buildOptions.copy(
                    nativeOptions = BuildOptions.NativeOptions(
                        cacheKind = NativeCacheKind.STATIC,
                        incremental = true
                    )
                ),
            ) {
                assertDirectoryExists(mainKtCache)
                assertDirectoryExists(fooKtCache)
                mainKtCacheModified = mainKtCache.toFile().lastModified()
                fooKtCacheModified = fooKtCache.toFile().lastModified()
            }

            val fooKt = kotlinSourcesDir("hostMain").resolve("foo.kt")
            fooKt.writeText("fun foo(): Int = \"zzz\"")

            buildAndFail(
                "linkDebugExecutableHost",
                buildOptions = buildOptions.copy(
                    nativeOptions = BuildOptions.NativeOptions(
                        cacheKind = NativeCacheKind.STATIC,
                        incremental = true
                    )
                ),
            ) {
                assertTasksFailed(":compileKotlinHost")
            }

            fooKt.writeText("fun foo(): Int = 42")

            build(
                "linkDebugExecutableHost",
                buildOptions = buildOptions.copy(
                    nativeOptions = BuildOptions.NativeOptions(
                        cacheKind = NativeCacheKind.STATIC,
                        incremental = true
                    )
                ),
            ) {
                assertDirectoryExists(mainKtCache)
                assertDirectoryExists(fooKtCache)
                assertEquals(mainKtCacheModified, mainKtCache.toFile().lastModified())
                assertEquals(fooKtCacheModified, fooKtCache.toFile().lastModified())
            }
        }
    }

    @DisplayName("IC works after compilation error (test 2)")
    @GradleTest
    fun compilationError2(gradleVersion: GradleVersion) {
        nativeProject("native-incremental-multifile", gradleVersion) {
            var mainKtCacheModified = 0L
            var fooKtCacheModified = 0L
            val mainKtCache = getFileCache("native-incremental-multifile", "src/hostMain/kotlin/main.kt", "")
            val fooKtCache = getFileCache("native-incremental-multifile", "src/hostMain/kotlin/foo.kt", "")
            build(
                "linkDebugExecutableHost",
                buildOptions = buildOptions.copy(
                    nativeOptions = BuildOptions.NativeOptions(
                        cacheKind = NativeCacheKind.STATIC,
                        incremental = true
                    )
                ),
            ) {
                assertDirectoryExists(mainKtCache)
                assertDirectoryExists(fooKtCache)
                mainKtCacheModified = mainKtCache.toFile().lastModified()
                fooKtCacheModified = fooKtCache.toFile().lastModified()
            }

            val fooKt = kotlinSourcesDir("hostMain").resolve("foo.kt")
            fooKt.writeText("fun foo(): Int = \"zzz\"")

            buildAndFail(
                "linkDebugExecutableHost",
                buildOptions = buildOptions.copy(
                    nativeOptions = BuildOptions.NativeOptions(
                        cacheKind = NativeCacheKind.STATIC,
                        incremental = true
                    )
                ),
            ) {
                assertTasksFailed(":compileKotlinHost")
            }

            fooKt.writeText("fun foo(): String = \"zzz\"")

            build(
                "linkDebugExecutableHost",
                buildOptions = buildOptions.copy(
                    nativeOptions = BuildOptions.NativeOptions(
                        cacheKind = NativeCacheKind.STATIC,
                        incremental = true
                    )
                ),
            ) {
                assertDirectoryExists(mainKtCache)
                assertDirectoryExists(fooKtCache)
                assertNotEquals(mainKtCacheModified, mainKtCache.toFile().lastModified())
                assertNotEquals(fooKtCacheModified, fooKtCache.toFile().lastModified())
            }
        }
    }

    @DisplayName("Check dependencies on project level")
    @GradleTest
    fun inProjectDependencies(gradleVersion: GradleVersion) {
        nativeProject("native-incremental-multi-project", gradleVersion, configureSubProjects = true) {
            var fooKtCacheModified = 0L
            var barKtCacheModified = 0L
            var mainKtCacheModified = 0L
            val fooKtCache = getFileCache("program", "MultiProject:library", "library/src/hostMain/kotlin/foo.kt", "")
            val barKtCache = getFileCache("program", "MultiProject:program", "program/src/hostMain/kotlin/bar.kt", "")
            val mainKtCache = getFileCache("program", "MultiProject:program", "program/src/hostMain/kotlin/main.kt", "")
            build(
                "linkDebugExecutableHost",
                buildOptions = buildOptions.copy(
                    nativeOptions = BuildOptions.NativeOptions(
                        cacheKind = NativeCacheKind.STATIC,
                        incremental = true
                    )
                ),
            ) {
                assertDirectoryExists(fooKtCache)
                assertDirectoryExists(barKtCache)
                assertDirectoryExists(mainKtCache)
                fooKtCacheModified = fooKtCache.toFile().lastModified()
                barKtCacheModified = barKtCache.toFile().lastModified()
                mainKtCacheModified = mainKtCache.toFile().lastModified()
            }

            val fooKt = projectPath.resolve("library/src/hostMain/kotlin").resolve("foo.kt")
            fooKt.writeText("fun foo(): Int = 41")

            build(
                "linkDebugExecutableHost",
                buildOptions = buildOptions.copy(
                    nativeOptions = BuildOptions.NativeOptions(
                        cacheKind = NativeCacheKind.STATIC,
                        incremental = true
                    )
                ),
            ) {
                assertDirectoryExists(fooKtCache)
                assertDirectoryExists(barKtCache)
                assertDirectoryExists(mainKtCache)
                assertNotEquals(fooKtCacheModified, fooKtCache.toFile().lastModified())
                assertEquals(barKtCacheModified, barKtCache.toFile().lastModified())
                assertNotEquals(mainKtCacheModified, mainKtCache.toFile().lastModified())
            }
        }
    }

    private fun computeCacheDirName(
        testTarget: KonanTarget,
        cacheKind: String,
        debuggable: Boolean,
        partialLinkageEnabled: Boolean
    ) = "$testTarget${if (debuggable) "-g" else ""}$cacheKind${if (partialLinkageEnabled) "-pl" else ""}"

    private val cacheFlavor = computeCacheDirName(HostManager.host, NativeCacheKind.STATIC.name, true, true)

    private fun TestProject.getICCacheDir(projectName: String = "") =
        (if (projectName == "") projectPath else projectPath.resolve(projectName))
            .resolve("build/kotlin-native-ic-cache")

    private fun TestProject.getFileCache(fileProjectName: String, fileRelativePath: String, fqName: String) =
        getFileCache("", fileProjectName, fileRelativePath, fqName)

    private fun TestProject.getFileCache(
        executableProjectName: String,
        fileProjectName: String,
        fileRelativePath: String,
        fqName: String,
    ): Path {
        val libCacheDir = getICCacheDir(executableProjectName).resolve(cacheFlavor).resolve("$fileProjectName-per-file-cache")
        val fileId = cacheFileId(fqName, projectPath.resolve(fileRelativePath).toFile().canonicalPath)
        return libCacheDir.resolve(fileId)
    }

    private fun cacheFileId(fqName: String, filePath: String) =
        "${if (fqName == "") "ROOT" else fqName}.${filePath.hashCode().toString(Character.MAX_RADIX)}"
}