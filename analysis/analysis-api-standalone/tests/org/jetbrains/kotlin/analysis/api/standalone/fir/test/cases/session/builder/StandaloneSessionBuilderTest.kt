/*
 * Copyright 2010-2023 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */


package org.jetbrains.kotlin.analysis.api.standalone.fir.test.cases.session.builder

import org.jetbrains.kotlin.analysis.api.KtAnalysisApiInternals
import org.jetbrains.kotlin.analysis.api.analyze
import org.jetbrains.kotlin.analysis.api.calls.KtSuccessCallInfo
import org.jetbrains.kotlin.analysis.api.calls.successfulFunctionCallOrNull
import org.jetbrains.kotlin.analysis.api.calls.symbol
import org.jetbrains.kotlin.analysis.api.lifetime.KtLifetimeTokenProvider
import org.jetbrains.kotlin.analysis.api.standalone.KtAlwaysAccessibleLifetimeTokenProvider
import org.jetbrains.kotlin.analysis.api.standalone.buildStandaloneAnalysisAPISession
import org.jetbrains.kotlin.analysis.api.symbols.KtConstructorSymbol
import org.jetbrains.kotlin.analysis.api.symbols.KtFunctionSymbol
import org.jetbrains.kotlin.analysis.project.structure.KtSourceModule
import org.jetbrains.kotlin.analysis.project.structure.builder.buildKtLibraryModule
import org.jetbrains.kotlin.analysis.project.structure.builder.buildKtSdkModule
import org.jetbrains.kotlin.analysis.project.structure.builder.buildKtSourceModule
import org.jetbrains.kotlin.builtins.StandardNames
import org.jetbrains.kotlin.codegen.forTestCompile.ForTestCompileRuntime
import org.jetbrains.kotlin.name.CallableId
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.platform.jvm.JvmPlatforms
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.psiUtil.findDescendantOfType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.nio.file.Paths

@OptIn(KtAnalysisApiInternals::class)
class StandaloneSessionBuilderTest {
    @Test
    fun testJdkSessionBuilder() {
        lateinit var sourceModule: KtSourceModule
        val session = buildStandaloneAnalysisAPISession {
            registerProjectService(KtLifetimeTokenProvider::class.java, KtAlwaysAccessibleLifetimeTokenProvider())

            buildKtModuleProvider {
                platform = JvmPlatforms.defaultJvmPlatform
                val sdk = addModule(
                    buildKtSdkModule {
                        addBinaryRootsFromJdkHome(Paths.get(System.getProperty("java.home")), isJre = true)
                        addBinaryRootsFromJdkHome(Paths.get(System.getProperty("java.home")), isJre = false)
                        platform = JvmPlatforms.defaultJvmPlatform
                        sdkName = "JDK"
                    }
                )
                sourceModule = addModule(
                    buildKtSourceModule {
                        addSourceRoot(testDataPath("jdkClassUsage"))
                        addRegularDependency(sdk)
                        platform = JvmPlatforms.defaultJvmPlatform
                        moduleName = "source"
                    }
                )
            }
        }
        val ktFile = session.modulesWithFiles.getValue(sourceModule).single() as KtFile
        analyze(ktFile) {
            val ktCallExpression = ktFile.findDescendantOfType<KtCallExpression>()!!
            val ktCallInfo = ktCallExpression.resolveCall()
            Assertions.assertInstanceOf(KtSuccessCallInfo::class.java, ktCallInfo); ktCallInfo as KtSuccessCallInfo
            val symbol = ktCallInfo.successfulFunctionCallOrNull()?.symbol
            Assertions.assertInstanceOf(KtConstructorSymbol::class.java, symbol); symbol as KtConstructorSymbol
            Assertions.assertEquals(ClassId.topLevel(FqName("java.lang.Thread")), symbol.containingClassIdIfNonLocal)
        }
    }

    @Test
    fun testKotlinSdkSessionBuilder() {
        lateinit var sourceModule: KtSourceModule
        val session = buildStandaloneAnalysisAPISession {
            registerProjectService(KtLifetimeTokenProvider::class.java, KtAlwaysAccessibleLifetimeTokenProvider())

            buildKtModuleProvider {
                platform = JvmPlatforms.defaultJvmPlatform
                val stdlib = addModule(
                    buildKtLibraryModule {
                        addBinaryRoot(ForTestCompileRuntime.runtimeJarForTests().toPath())
                        platform = JvmPlatforms.defaultJvmPlatform
                        libraryName = "stdlib"
                    }
                )
                sourceModule = addModule(
                    buildKtSourceModule {
                        addSourceRoot(testDataPath("stdlibFunctionUsage"))
                        addRegularDependency(stdlib)
                        platform = JvmPlatforms.defaultJvmPlatform
                        moduleName = "source"
                    }
                )
            }
        }
        val ktFile = session.modulesWithFiles.getValue(sourceModule).single() as KtFile
        val ktCallExpression = ktFile.findDescendantOfType<KtCallExpression>()!!
        ktCallExpression.assertIsCallOf(CallableId(StandardNames.COLLECTIONS_PACKAGE_FQ_NAME, Name.identifier("listOf")))
    }

    @Test
    fun testKotlinSourceModuleSessionBuilder() {
        lateinit var sourceModule: KtSourceModule
        val session = buildStandaloneAnalysisAPISession {
            registerProjectService(KtLifetimeTokenProvider::class.java, KtAlwaysAccessibleLifetimeTokenProvider())

            buildKtModuleProvider {
                platform = JvmPlatforms.defaultJvmPlatform
                val main = addModule(
                    buildKtSourceModule {
                        addSourceRoot(testDataPath("otherModuleUsage").resolve("dependent"))
                        platform = JvmPlatforms.defaultJvmPlatform
                        moduleName = "dependent"
                    }
                )
                sourceModule = addModule(
                    buildKtSourceModule {
                        addSourceRoot(testDataPath("otherModuleUsage").resolve("main"))
                        addRegularDependency(main)
                        platform = JvmPlatforms.defaultJvmPlatform
                        moduleName = "main"
                    }
                )
            }
        }
        val ktFile = session.modulesWithFiles.getValue(sourceModule).single() as KtFile
        val ktCallExpression = ktFile.findDescendantOfType<KtCallExpression>()!!
        ktCallExpression.assertIsCallOf(CallableId(FqName.ROOT, Name.identifier("foo")))
    }


    private fun KtCallExpression.assertIsCallOf(callableId: CallableId) {
        analyze(this) {
            val ktCallInfo = resolveCall()
            Assertions.assertInstanceOf(KtSuccessCallInfo::class.java, ktCallInfo); ktCallInfo as KtSuccessCallInfo
            val symbol = ktCallInfo.successfulFunctionCallOrNull()?.symbol
            Assertions.assertInstanceOf(KtFunctionSymbol::class.java, symbol); symbol as KtFunctionSymbol
            Assertions.assertEquals(callableId, symbol.callableIdIfNonLocal)
        }
    }
}