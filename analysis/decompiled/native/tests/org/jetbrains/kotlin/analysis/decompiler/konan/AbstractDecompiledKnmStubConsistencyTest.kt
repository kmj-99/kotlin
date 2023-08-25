/*
 * Copyright 2010-2023 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.analysis.decompiler.konan

import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.impl.jar.CoreJarFileSystem
import com.intellij.psi.PsiManager
import com.intellij.util.indexing.FileContentImpl
import org.jetbrains.kotlin.analysis.decompiler.stub.files.findMainTestKotlinFile
import org.jetbrains.kotlin.analysis.decompiler.stub.files.serializeToString
import org.jetbrains.kotlin.cli.jvm.compiler.EnvironmentConfigFiles
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.jetbrains.kotlin.library.KLIB_METADATA_FILE_EXTENSION_WITH_DOT
import org.jetbrains.kotlin.psi.stubs.elements.KtFileStubBuilder
import org.jetbrains.kotlin.test.*
import org.jetbrains.kotlin.test.util.KtTestUtil
import org.jetbrains.kotlin.utils.addIfNotNull
import org.junit.Assert
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Collectors
import kotlin.io.path.absolutePathString
import kotlin.io.path.extension

abstract class AbstractDecompiledKnmStubConsistencyFe10Test : AbstractDecompiledKnmStubConsistencyTest() {
    override fun createStubBuilder(): KlibMetadataStubBuilder {
        return KotlinNativeMetadataDecompiler().stubBuilder
    }
}

abstract class AbstractDecompiledKnmStubConsistencyK2Test : AbstractDecompiledKnmStubConsistencyTest() {
    override fun createStubBuilder(): KlibMetadataStubBuilder {
        return K2KotlinNativeMetadataDecompiler().stubBuilder
    }
}


abstract class AbstractDecompiledKnmStubConsistencyTest : KotlinTestWithEnvironment() {
    abstract fun createStubBuilder(): KlibMetadataStubBuilder

    override fun createEnvironment(): KotlinCoreEnvironment {
        return KotlinCoreEnvironment.createForTests(
            ApplicationEnvironmentDisposer.ROOT_DISPOSABLE,
            KotlinTestUtils.newConfiguration(
                ConfigurationKind.JDK_NO_RUNTIME,
                TestJdkKind.MOCK_JDK,
            ),
            EnvironmentConfigFiles.METADATA_CONFIG_FILES,
        )
    }

    override fun setUp() {
        super.setUp()

        environment.projectEnvironment.environment.registerFileType(
            KlibMetaFileType, KlibMetaFileType.defaultExtension
        )
    }

    fun runTest(testDirectory: String) {
        val testDirectoryPath = Paths.get(testDirectory)
        doTest(testDirectoryPath)
    }

    private fun isTestIgnored(testDirectory: Path): Boolean {
        val mainKotlinFile = findMainTestKotlinFile(testDirectory).toFile()
        return InTextDirectivesUtils.isDirectiveDefined(mainKotlinFile.readText(), "// $KNM_IGNORE_DIRECTIVE_TEXT")
    }

    private fun doTest(testDirectoryPath: Path) {
        if (isTestIgnored(testDirectoryPath)) return

        val commonKlib = compileCommonKlib(testDirectoryPath)
        val files = getKnmFilesFromKlib(commonKlib)

        for (knmFile in files) {
            checkKnmStubConsistency(knmFile)
        }
    }

    private fun compileCommonKlib(testDirectory: Path): File {
        val ktFiles = Files.list(testDirectory).filter { it.extension == "kt" }.collect(Collectors.toList())
        val testKlib = KtTestUtil.tmpDir("testLibrary").resolve("library.klib")
        KlibTestUtil.compileCommonSourcesToKlib(
            ktFiles.map(Path::toFile),
            libraryName = "library",
            testKlib,
        )

        return testKlib
    }

    private fun getKnmFilesFromKlib(klib: File): List<VirtualFile> {
        val path = klib.toPath()
        val jarFileSystem = environment.projectEnvironment.environment.jarFileSystem as CoreJarFileSystem
        val root = jarFileSystem.refreshAndFindFileByPath(path.absolutePathString() + "!/")!!
        val files = mutableListOf<VirtualFile>()
        VfsUtilCore.iterateChildrenRecursively(
            root,
            { virtualFile -> virtualFile.isDirectory || virtualFile.name.endsWith(KLIB_METADATA_FILE_EXTENSION_WITH_DOT) },
            { virtualFile ->
                if (!virtualFile.isDirectory) {
                    files.addIfNotNull(virtualFile)
                }
                true
            })

        return files
    }

    private fun checkKnmStubConsistency(knmFile: VirtualFile) {
        val stubTreeBinaryFile = createStubBuilder().buildFileStub(FileContentImpl.createByFile(knmFile, environment.project))!!

        val fileViewProviderForDecompiledFile = K2KotlinNativeMetadataDecompiler().createFileViewProvider(
            knmFile, PsiManager.getInstance(project), physical = false,
        )

        val stubTreeForDecompiledFile = KtFileStubBuilder().buildStubTree(
            KlibDecompiledFile(fileViewProviderForDecompiledFile) { virtualFile ->
                K2KotlinNativeMetadataDecompiler().buildDecompiledTextForTests(virtualFile)
            }
        )

        Assert.assertEquals(
            "PSI and deserialized stubs don't match",
            stubTreeForDecompiledFile.serializeToString(),
            stubTreeBinaryFile.serializeToString()
        )
    }
}

private const val KNM_IGNORE_DIRECTIVE_TEXT = "KNM_IGNORE"
