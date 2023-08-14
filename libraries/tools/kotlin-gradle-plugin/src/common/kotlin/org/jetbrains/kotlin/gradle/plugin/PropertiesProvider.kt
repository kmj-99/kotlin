/*
 * Copyright 2010-2021 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.gradle.plugin

import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.util.GradleVersion
import org.jetbrains.kotlin.cli.common.CompilerSystemProperties
import org.jetbrains.kotlin.cli.common.toBooleanLenient
import org.jetbrains.kotlin.gradle.dsl.NativeCacheKind
import org.jetbrains.kotlin.gradle.dsl.NativeCacheOrchestration
import org.jetbrains.kotlin.gradle.dsl.jvm.JvmTargetValidationMode
import org.jetbrains.kotlin.gradle.internal.testing.TCServiceMessageOutputStreamHandler.Companion.IGNORE_TCSM_OVERFLOW
import org.jetbrains.kotlin.gradle.plugin.KotlinJsCompilerType.Companion.jsCompilerProperty
import org.jetbrains.kotlin.gradle.plugin.PropertiesProvider.PropertyNames.KOTLIN_ABI_SNAPSHOT
import org.jetbrains.kotlin.gradle.plugin.PropertiesProvider.PropertyNames.KOTLIN_COMPILER_KEEP_INCREMENTAL_COMPILATION_CACHES_IN_MEMORY
import org.jetbrains.kotlin.gradle.plugin.PropertiesProvider.PropertyNames.KOTLIN_COMPILER_USE_PRECISE_COMPILATION_RESULTS_BACKUP
import org.jetbrains.kotlin.gradle.plugin.PropertiesProvider.PropertyNames.KOTLIN_CREATE_DEFAULT_MULTIPLATFORM_PUBLICATIONS
import org.jetbrains.kotlin.gradle.plugin.PropertiesProvider.PropertyNames.KOTLIN_EXPERIMENTAL_TRY_K2
import org.jetbrains.kotlin.gradle.plugin.PropertiesProvider.PropertyNames.KOTLIN_INTERNAL_DIAGNOSTICS_SHOW_STACKTRACE
import org.jetbrains.kotlin.gradle.plugin.PropertiesProvider.PropertyNames.KOTLIN_INTERNAL_DIAGNOSTICS_USE_PARSABLE_FORMATTING
import org.jetbrains.kotlin.gradle.plugin.PropertiesProvider.PropertyNames.KOTLIN_JS_KARMA_BROWSERS
import org.jetbrains.kotlin.gradle.plugin.PropertiesProvider.PropertyNames.KOTLIN_JS_STDLIB_DOM_API_INCLUDED
import org.jetbrains.kotlin.gradle.plugin.PropertiesProvider.PropertyNames.KOTLIN_MPP_ALLOW_LEGACY_DEPENDENCIES
import org.jetbrains.kotlin.gradle.plugin.PropertiesProvider.PropertyNames.KOTLIN_MPP_ANDROID_GRADLE_PLUGIN_COMPATIBILITY_NO_WARN
import org.jetbrains.kotlin.gradle.plugin.PropertiesProvider.PropertyNames.KOTLIN_MPP_ANDROID_SOURCE_SET_LAYOUT_ANDROID_STYLE_NO_WARN
import org.jetbrains.kotlin.gradle.plugin.PropertiesProvider.PropertyNames.KOTLIN_MPP_ANDROID_SOURCE_SET_LAYOUT_VERSION
import org.jetbrains.kotlin.gradle.plugin.PropertiesProvider.PropertyNames.KOTLIN_MPP_APPLY_DEFAULT_HIERARCHY_TEMPLATE
import org.jetbrains.kotlin.gradle.plugin.PropertiesProvider.PropertyNames.KOTLIN_MPP_ENABLE_CINTEROP_COMMONIZATION
import org.jetbrains.kotlin.gradle.plugin.PropertiesProvider.PropertyNames.KOTLIN_MPP_ENABLE_COMPATIBILITY_METADATA_VARIANT
import org.jetbrains.kotlin.gradle.plugin.PropertiesProvider.PropertyNames.KOTLIN_MPP_ENABLE_GRANULAR_SOURCE_SETS_METADATA
import org.jetbrains.kotlin.gradle.plugin.PropertiesProvider.PropertyNames.KOTLIN_MPP_ENABLE_INTRANSITIVE_METADATA_CONFIGURATION
import org.jetbrains.kotlin.gradle.plugin.PropertiesProvider.PropertyNames.KOTLIN_MPP_ENABLE_OPTIMISTIC_NUMBER_COMMONIZATION
import org.jetbrains.kotlin.gradle.plugin.PropertiesProvider.PropertyNames.KOTLIN_MPP_ENABLE_PLATFORM_INTEGER_COMMONIZATION
import org.jetbrains.kotlin.gradle.plugin.PropertiesProvider.PropertyNames.KOTLIN_MPP_HIERARCHICAL_STRUCTURE_BY_DEFAULT
import org.jetbrains.kotlin.gradle.plugin.PropertiesProvider.PropertyNames.KOTLIN_MPP_HIERARCHICAL_STRUCTURE_SUPPORT
import org.jetbrains.kotlin.gradle.plugin.PropertiesProvider.PropertyNames.KOTLIN_MPP_IMPORT_ENABLE_KGP_DEPENDENCY_RESOLUTION
import org.jetbrains.kotlin.gradle.plugin.PropertiesProvider.PropertyNames.KOTLIN_MPP_IMPORT_ENABLE_SLOW_SOURCES_JAR_RESOLVER
import org.jetbrains.kotlin.gradle.plugin.PropertiesProvider.PropertyNames.KOTLIN_PUBLISH_JVM_ENVIRONMENT_ATTRIBUTE
import org.jetbrains.kotlin.gradle.plugin.PropertiesProvider.PropertyNames.KOTLIN_NATIVE_IGNORE_DISABLED_TARGETS
import org.jetbrains.kotlin.gradle.plugin.PropertiesProvider.PropertyNames.KOTLIN_NATIVE_SUPPRESS_EXPERIMENTAL_ARTIFACTS_DSL_WARNING
import org.jetbrains.kotlin.gradle.plugin.PropertiesProvider.PropertyNames.KOTLIN_NATIVE_USE_XCODE_MESSAGE_STYLE
import org.jetbrains.kotlin.gradle.plugin.PropertiesProvider.PropertyNames.KOTLIN_RUN_COMPILER_VIA_BUILD_TOOLS_API
import org.jetbrains.kotlin.gradle.plugin.PropertiesProvider.PropertyNames.KOTLIN_STDLIB_DEFAULT_DEPENDENCY
import org.jetbrains.kotlin.gradle.plugin.PropertiesProvider.PropertyNames.KOTLIN_STDLIB_JDK_VARIANTS_VERSION_ALIGNMENT
import org.jetbrains.kotlin.gradle.plugin.PropertiesProvider.PropertyNames.KOTLIN_SUPPRESS_EXPERIMENTAL_IC_OPTIMIZATIONS_WARNING
import org.jetbrains.kotlin.gradle.plugin.PropertiesProvider.PropertyNames.MPP_13X_FLAGS_SET_BY_PLUGIN
import org.jetbrains.kotlin.gradle.plugin.diagnostics.KotlinToolingDiagnostics
import org.jetbrains.kotlin.gradle.plugin.diagnostics.reportDiagnosticOncePerBuild
import org.jetbrains.kotlin.gradle.plugin.statistics.KotlinBuildStatsService
import org.jetbrains.kotlin.gradle.targets.js.ir.KotlinIrJsGeneratedTSValidationStrategy
import org.jetbrains.kotlin.gradle.targets.js.ir.KotlinJsIrOutputGranularity
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilerExecutionStrategy
import org.jetbrains.kotlin.gradle.utils.NativeCompilerDownloader
import org.jetbrains.kotlin.konan.target.KonanTarget
import org.jetbrains.kotlin.konan.target.presetName
import org.jetbrains.kotlin.statistics.metrics.StringMetrics
import org.jetbrains.kotlin.tooling.core.UnsafeApi
import org.jetbrains.kotlin.util.capitalizeDecapitalize.toLowerCaseAsciiOnly
import org.jetbrains.kotlin.util.capitalizeDecapitalize.toUpperCaseAsciiOnly
import org.jetbrains.kotlin.util.prefixIfNot
import java.io.File
import java.util.*

@OptIn(UnsafeApi::class)
internal class PropertiesProvider private constructor(private val project: Project) {
    private val localProperties: Properties by lazy {
        Properties().apply {
            val localPropertiesFile = File(project.rootDir, "local.properties")
            if (localPropertiesFile.isFile) {
                localPropertiesFile.inputStream().use {
                    load(it)
                }
            }
        }
    }

    @Deprecated(message = "Please use kotlin.build.report.output=SINGLE_FILE and kotlin.build.report.single_file ")
    val singleBuildMetricsFile: File?
        get() = this.property("kotlin.internal.single.build.metrics.file")?.let { File(it) }

    val buildReportSingleFile: File?
        get() = this.property(PropertyNames.KOTLIN_BUILD_REPORT_SINGLE_FILE)?.let { File(it) }

    val buildReportOutputs: List<String>
        get() = this.property("kotlin.build.report.output")?.split(",") ?: emptyList()

    val buildReportLabel: String?
        get() = this.property("kotlin.build.report.label")

    val buildReportFileOutputDir: File?
        get() = this.property("kotlin.build.report.file.output_dir")?.let { File(it) }

    val buildReportHttpUrl: String?
        get() = this.property(PropertyNames.KOTLIN_BUILD_REPORT_HTTP_URL)

    val buildReportHttpUser: String?
        get() = this.property("kotlin.build.report.http.user")

    val buildReportHttpPassword: String?
        get() = this.property("kotlin.build.report.http.password")

    val buildReportHttpVerboseEnvironment: Boolean
        get() = property("kotlin.build.report.http.verbose_environment")?.toBoolean() ?: false

    val buildReportHttpIncludeGitBranchName: Boolean
        get() = property("kotlin.build.report.http.include_git_branch.name")?.toBoolean() ?: false

    val buildReportIncludeCompilerArguments: Boolean
        get() = booleanProperty("kotlin.build.report.include_compiler_arguments") ?: true

    val buildReportBuildScanCustomValuesLimit: Int
        get() = property("kotlin.build.report.build_scan.custom_values_limit")?.toInt() ?: 1000

    val buildReportBuildScanMetrics: String?
        get() = property("kotlin.build.report.build_scan.metrics")

    val buildReportMetrics: Boolean
        get() = booleanProperty("kotlin.build.report.metrics") ?: false

    val buildReportVerbose: Boolean
        get() = booleanProperty("kotlin.build.report.verbose") ?: false

    @Deprecated("Please use \"kotlin.build.report.file.output_dir\" property instead")
    val buildReportDir: File?
        get() = this.property("kotlin.build.report.dir")?.let { File(it) }

    val incrementalJvm: Boolean?
        get() = booleanProperty("kotlin.incremental")

    val incrementalJs: Boolean?
        get() = booleanProperty("kotlin.incremental.js")

    val incrementalJsKlib: Boolean?
        get() = booleanProperty("kotlin.incremental.js.klib")

    val incrementalJsIr: Boolean
        get() = booleanProperty("kotlin.incremental.js.ir") ?: true

    val incrementalNative: Boolean?
        get() = booleanProperty(PropertyNames.KOTLIN_NATIVE_INCREMENTAL_COMPILATION)

    val jsIrOutputGranularity: KotlinJsIrOutputGranularity
        get() = this.property("kotlin.js.ir.output.granularity")?.let { KotlinJsIrOutputGranularity.byArgument(it) }
            ?: KotlinJsIrOutputGranularity.PER_MODULE

    val jsIrGeneratedTypeScriptValidationDevStrategy: KotlinIrJsGeneratedTSValidationStrategy
        get() = this.property("kotlin.js.ir.development.typescript.validation.strategy")?.let {
            KotlinIrJsGeneratedTSValidationStrategy.byArgument(
                it
            )
        } ?: KotlinIrJsGeneratedTSValidationStrategy.IGNORE

    val jsIrGeneratedTypeScriptValidationProdStrategy: KotlinIrJsGeneratedTSValidationStrategy
        get() = this.property("kotlin.js.ir.production.typescript.validation.strategy")?.let {
            KotlinIrJsGeneratedTSValidationStrategy.byArgument(
                it
            )
        } ?: KotlinIrJsGeneratedTSValidationStrategy.IGNORE

    val incrementalMultiplatform: Boolean?
        get() = booleanProperty("kotlin.incremental.multiplatform")

    val usePreciseJavaTracking: Boolean?
        get() = booleanProperty("kotlin.incremental.usePreciseJavaTracking")

    val useClasspathSnapshot: Boolean
        get() {
            val reporter = KotlinBuildStatsService.getInstance()
            // The feature should be controlled by a Gradle property.
            // Currently, we also allow it to be controlled by a system property to make it easier to test the feature during development.
            // TODO: Remove the system property later.

            val gradleProperty = booleanProperty(CompilerSystemProperties.COMPILE_INCREMENTAL_WITH_ARTIFACT_TRANSFORM.property)
            if (gradleProperty != null) {
                reporter?.report(StringMetrics.USE_CLASSPATH_SNAPSHOT, gradleProperty.toString())
                return gradleProperty
            }
            val systemProperty = CompilerSystemProperties.COMPILE_INCREMENTAL_WITH_ARTIFACT_TRANSFORM.value?.toBooleanLenient()
            if (systemProperty != null) {
                reporter?.report(StringMetrics.USE_CLASSPATH_SNAPSHOT, systemProperty.toString())
                return systemProperty
            }
            reporter?.report(StringMetrics.USE_CLASSPATH_SNAPSHOT, "default-true")
            return true
        }

    val useKotlinAbiSnapshot: Boolean
        get() = booleanProperty(KOTLIN_ABI_SNAPSHOT) ?: false

    val useK2: Boolean?
        get() = booleanProperty("kotlin.useK2")

    val keepMppDependenciesIntactInPoms: Boolean?
        get() = booleanProperty("kotlin.mpp.keepMppDependenciesIntactInPoms")

    val ignorePluginLoadedInMultipleProjects: Boolean?
        get() = booleanProperty("kotlin.pluginLoadedInMultipleProjects.ignore")

    val keepAndroidBuildTypeAttribute: Boolean
        get() = booleanProperty("kotlin.android.buildTypeAttribute.keep") ?: false

    val enableGranularSourceSetsMetadata: Boolean?
        get() = booleanProperty(KOTLIN_MPP_ENABLE_GRANULAR_SOURCE_SETS_METADATA)

    val hierarchicalStructureSupport: Boolean
        get() = booleanProperty(KOTLIN_MPP_HIERARCHICAL_STRUCTURE_SUPPORT) ?: mppHierarchicalStructureByDefault

    var mpp13XFlagsSetByPlugin: Boolean
        get() = booleanProperty(MPP_13X_FLAGS_SET_BY_PLUGIN) ?: false
        set(value) {
            project.extensions.extraProperties.set(MPP_13X_FLAGS_SET_BY_PLUGIN, "$value")
        }

    val mppHierarchicalStructureByDefault: Boolean
        get() = booleanProperty(KOTLIN_MPP_HIERARCHICAL_STRUCTURE_BY_DEFAULT) ?: true

    val enableCompatibilityMetadataVariant: Boolean
        get() {
            return (booleanProperty(KOTLIN_MPP_ENABLE_COMPATIBILITY_METADATA_VARIANT) ?: !mppHierarchicalStructureByDefault)
        }

    val enableKotlinToolingMetadataArtifact: Boolean
        get() = booleanProperty("kotlin.mpp.enableKotlinToolingMetadataArtifact") ?: true

    val mppEnableOptimisticNumberCommonization: Boolean
        get() = booleanProperty(KOTLIN_MPP_ENABLE_OPTIMISTIC_NUMBER_COMMONIZATION) ?: true

    val mppEnablePlatformIntegerCommonization: Boolean
        get() = booleanProperty(KOTLIN_MPP_ENABLE_PLATFORM_INTEGER_COMMONIZATION) ?: false

    val mppApplyDefaultHierarchyTemplate: Boolean
        get() = this.booleanProperty(KOTLIN_MPP_APPLY_DEFAULT_HIERARCHY_TEMPLATE) ?: true

    val wasmStabilityNoWarn: Boolean
        get() = booleanProperty("kotlin.wasm.stability.nowarn") ?: false

    val jsCompilerNoWarn: Boolean
        get() = booleanProperty("$jsCompilerProperty.nowarn") ?: false

    val ignoreDisabledNativeTargets: Boolean?
        get() = booleanProperty(KOTLIN_NATIVE_IGNORE_DISABLED_TARGETS)

    val ignoreAbsentAndroidMultiplatformTarget: Boolean
        get() = booleanProperty("kotlin.mpp.absentAndroidTarget.nowarn") ?: false

    val ignoreAndroidGradlePluginCompatibilityIssues: Boolean
        get() = booleanProperty(KOTLIN_MPP_ANDROID_GRADLE_PLUGIN_COMPATIBILITY_NO_WARN) ?: false

    val mppAndroidSourceSetLayoutVersion: Int?
        get() = this.property(KOTLIN_MPP_ANDROID_SOURCE_SET_LAYOUT_VERSION)?.toIntOrNull()

    val ignoreMppAndroidSourceSetLayoutV2AndroidStyleDirs: Boolean
        get() = booleanProperty(KOTLIN_MPP_ANDROID_SOURCE_SET_LAYOUT_ANDROID_STYLE_NO_WARN) ?: false

    val ignoreDisabledCInteropCommonization: Boolean
        get() = booleanProperty("$KOTLIN_MPP_ENABLE_CINTEROP_COMMONIZATION.nowarn") ?: false

    val ignoreIncorrectNativeDependencies: Boolean?
        get() = booleanProperty(KOTLIN_NATIVE_IGNORE_INCORRECT_DEPENDENCIES)

    val publishJvmEnvironmentAttribute: Boolean
        get() = booleanProperty(KOTLIN_PUBLISH_JVM_ENVIRONMENT_ATTRIBUTE) ?: false

    /**
     * Enables individual test task reporting for aggregated test tasks.
     *
     * By default individual test tasks will not fail build if this task will be executed,
     * also individual html and xml reports will replaced by one consolidated html report.
     */
    val individualTaskReports: Boolean
        get() = booleanProperty("kotlin.tests.individualTaskReports") ?: false

    /**
     * Allow a user to choose distribution type. The following distribution types are available:
     *  - light - Doesn't include platform libraries and generates them at the user side. For 1.3 corresponds to the restricted distribution.
     *  - prebuilt - Includes all platform libraries.
     */
    val nativeDistributionType: String?
        get() = this.property("kotlin.native.distribution.type")

    /**
     * Allows overriding Kotlin/Native base download url.
     *
     * When Kotlin/native will try to download native compiler, it will append compiler version and os type to this url.
     */
    val nativeBaseDownloadUrl: String
        get() = this.property("kotlin.native.distribution.baseDownloadUrl") ?: NativeCompilerDownloader.BASE_DOWNLOAD_URL

    /**
     * Allows downloading Kotlin/Native distribution with maven.
     *
     * Makes downloader search for bundles in maven repositories specified in the project.
     */
    val nativeDownloadFromMaven: Boolean
        get() = this.booleanProperty("kotlin.native.distribution.downloadFromMaven") ?: false

    /**
     * Allows a user to provide a local Kotlin/Native distribution instead of a downloaded one.
     */
    val nativeHome: String?
        get() = propertyWithDeprecatedVariant(KOTLIN_NATIVE_HOME, "org.jetbrains.kotlin.native.home")

    /**
     * Allows a user to override Kotlin/Native version.
     */
    val nativeVersion: String?
        get() = propertyWithDeprecatedVariant("kotlin.native.version", "org.jetbrains.kotlin.native.version")

    /**
     * Forces reinstalling a K/N distribution.
     *
     * The current distribution directory will be removed along with generated platform libraries and precompiled dependencies.
     * After that a fresh distribution with the same version will be installed. Platform libraries and precompiled dependencies will
     * be built in a regular way.
     *
     * Ignored if kotlin.native.home is specified.
     */
    val nativeReinstall: Boolean
        get() = booleanProperty("kotlin.native.reinstall") ?: false

    /**
     * Allows a user to specify additional arguments of a JVM executing a K/N compiler.
     */
    val nativeJvmArgs: String?
        get() = propertyWithDeprecatedVariant("kotlin.native.jvmArgs", "org.jetbrains.kotlin.native.jvmArgs")

    /**
     * Allows a user to specify free compiler arguments for K/N linker.
     */
    val nativeLinkArgs: List<String>
        get() = this.property("kotlin.native.linkArgs").orEmpty().split(' ').filterNot { it.isBlank() }

    /**
     * Forces to run a compilation in a separate JVM.
     */
    val nativeDisableCompilerDaemon: Boolean?
        get() = booleanProperty("kotlin.native.disableCompilerDaemon")

    /**
     * Switches Kotlin/Native tasks to using embeddable compiler jar,
     * allowing to apply backend-agnostic compiler plugin artifacts.
     * Will be default after proper migration.
     */
    val nativeUseEmbeddableCompilerJar: Boolean
        get() = booleanProperty("kotlin.native.useEmbeddableCompilerJar") ?: true

    /**
     * Allows a user to set project-wide options that will be passed to the K/N compiler via -Xbinary flag.
     * E.g. setting kotlin.native.binary.memoryModel=experimental results in passing -Xbinary=memoryModel=experimental to the compiler.
     * @return a map: property name without `kotlin.native.binary.` prefix -> property value
     */
    val nativeBinaryOptions: Map<String, String>
        get() = propertiesWithPrefix(KOTLIN_NATIVE_BINARY_OPTION_PREFIX).mapKeys { (key, _) ->
            key.removePrefix(KOTLIN_NATIVE_BINARY_OPTION_PREFIX)
        }

    /**
     * Forces K/N compiler to print messages which could be parsed by Xcode
     */
    val nativeUseXcodeMessageStyle: Boolean?
        get() = booleanProperty(KOTLIN_NATIVE_USE_XCODE_MESSAGE_STYLE)

    /**
     * Allows a user to specify additional arguments of a JVM executing KLIB commonizer.
     */
    val commonizerJvmArgs: List<String>
        get() = propertyWithDeprecatedVariant("kotlin.mpp.commonizerJvmArgs", "kotlin.commonizer.jvmArgs")
            ?.split("\\s+".toRegex())
            .orEmpty()

    /**
     * Enables experimental commonization of user defined c-interop libraries.
     */
    val enableCInteropCommonization: Boolean?
        get() = booleanProperty(KOTLIN_MPP_ENABLE_CINTEROP_COMMONIZATION)

    private val enableCInteropCommonizationSetByExternalPluginKey = "kotlin.internal.mpp.enableCInteropCommonization.setByExternalPlugin"

    internal var enableCInteropCommonizationSetByExternalPlugin: Boolean?
        get() {
            if (!project.extensions.extraProperties.has(enableCInteropCommonizationSetByExternalPluginKey)) return null
            return booleanProperty(enableCInteropCommonizationSetByExternalPluginKey)
        }
        set(value) {
            project.extensions.extraProperties.set(enableCInteropCommonizationSetByExternalPluginKey, "$value")
        }

    val commonizerLogLevel: String?
        get() = this.property("kotlin.mpp.commonizerLogLevel")

    val enableNativeDistributionCommonizationCache: Boolean
        get() = booleanProperty("kotlin.mpp.enableNativeDistributionCommonizationCache") ?: true

    val enableIntransitiveMetadataConfiguration: Boolean
        get() = booleanProperty(KOTLIN_MPP_ENABLE_INTRANSITIVE_METADATA_CONFIGURATION) ?: false

    val enableKgpDependencyResolution: Boolean?
        get() = booleanProperty(KOTLIN_MPP_IMPORT_ENABLE_KGP_DEPENDENCY_RESOLUTION)

    val enableSlowIdeSourcesJarResolver: Boolean
        get() = booleanProperty(KOTLIN_MPP_IMPORT_ENABLE_SLOW_SOURCES_JAR_RESOLVER) ?: true

    /**
     * Dependencies caching strategy for all targets that support caches.
     */
    val nativeCacheKind: NativeCacheKind?
        get() = this.property("kotlin.native.cacheKind")?.let { NativeCacheKind.byCompilerArgument(it) }

    /**
     * Dependencies caching strategy for [target].
     */
    fun nativeCacheKindForTarget(target: KonanTarget): NativeCacheKind? =
        this.property("kotlin.native.cacheKind.${target.presetName}")?.let { NativeCacheKind.byCompilerArgument(it) }

    /**
     * Dependencies caching orchestration machinery.
     */
    val nativeCacheOrchestration: NativeCacheOrchestration?
        get() = this.property(PropertyNames.KOTLIN_NATIVE_CACHE_ORCHESTRATION)?.let { NativeCacheOrchestration.byCompilerArgument(it) }

    /**
     * Native backend threads.
     */
    val nativeParallelThreads: Int?
        get() = this.property(PropertyNames.KOTLIN_NATIVE_PARALLEL_THREADS)?.toInt()

    /**
     * Ignore overflow in [org.jetbrains.kotlin.gradle.internal.testing.TCServiceMessageOutputStreamHandler]
     */
    val ignoreTcsmOverflow: Boolean
        get() = booleanProperty(IGNORE_TCSM_OVERFLOW) ?: false

    val errorJsGenerateExternals: Boolean?
        get() = booleanProperty("kotlin.js.generate.externals")

    /**
     * Use Kotlin/JS backend compiler type
     */
    val jsCompiler: KotlinJsCompilerType?
        get() = this.property(jsCompilerProperty)?.let { KotlinJsCompilerType.byArgumentOrNull(it) }

    /**
     * Use Kotlin/JS backend compiler publishing attribute
     */
    val publishJsCompilerAttribute: Boolean
        get() = this.booleanProperty("$jsCompilerProperty.publish.attribute") ?: true

    /**
     * Use Kotlin/JS backend compiler type
     */
    val jsGenerateExecutableDefault: Boolean
        get() = (booleanProperty("kotlin.js.generate.executable.default") ?: true).also {
            KotlinBuildStatsService.getInstance()
                ?.report(StringMetrics.JS_GENERATE_EXECUTABLE_DEFAULT, it.toString())
        }

    val stdlibDefaultDependency: Boolean
        get() = booleanProperty(KOTLIN_STDLIB_DEFAULT_DEPENDENCY) ?: true

    val stdlibJdkVariantsVersionAlignment: Boolean
        get() = booleanProperty(KOTLIN_STDLIB_JDK_VARIANTS_VERSION_ALIGNMENT) ?: true

    val stdlibDomApiIncluded: Boolean
        get() = booleanProperty(KOTLIN_JS_STDLIB_DOM_API_INCLUDED) ?: true

    val kotlinTestInferJvmVariant: Boolean
        get() = booleanProperty("kotlin.test.infer.jvm.variant") ?: true

    val kotlinOptionsSuppressFreeArgsModificationWarning: Boolean
        get() = booleanProperty(PropertyNames.KOTLIN_OPTIONS_SUPPRESS_FREEARGS_MODIFICATION_WARNING) ?: false

    val jvmTargetValidationMode: JvmTargetValidationMode
        get() = enumProperty(
            "kotlin.jvm.target.validation.mode",
            if (GradleVersion.current().baseVersion >= GradleVersion.version("8.0")) JvmTargetValidationMode.ERROR else JvmTargetValidationMode.WARNING
        )

    val kotlinDaemonJvmArgs: String?
        get() = this.property("kotlin.daemon.jvmargs")

    val kotlinCompilerExecutionStrategy: KotlinCompilerExecutionStrategy
        get() = KotlinCompilerExecutionStrategy.fromProperty(
            this.property("kotlin.compiler.execution.strategy")?.toLowerCaseAsciiOnly()
        )

    val kotlinDaemonUseFallbackStrategy: Boolean
        get() = booleanProperty("kotlin.daemon.useFallbackStrategy") ?: true

    val preciseCompilationResultsBackup: Boolean
        get() = booleanProperty(KOTLIN_COMPILER_USE_PRECISE_COMPILATION_RESULTS_BACKUP) ?: false

    /**
     * This property should be enabled together with [preciseCompilationResultsBackup]
     */
    val keepIncrementalCompilationCachesInMemory: Boolean
        get() = booleanProperty(KOTLIN_COMPILER_KEEP_INCREMENTAL_COMPILATION_CACHES_IN_MEMORY) ?: false

    val suppressExperimentalICOptimizationsWarning: Boolean
        get() = booleanProperty(KOTLIN_SUPPRESS_EXPERIMENTAL_IC_OPTIMIZATIONS_WARNING) ?: false

    val createDefaultMultiplatformPublications: Boolean
        get() = booleanProperty(KOTLIN_CREATE_DEFAULT_MULTIPLATFORM_PUBLICATIONS) ?: true

    val runKotlinCompilerViaBuildToolsApi: Boolean
        get() = booleanProperty(KOTLIN_RUN_COMPILER_VIA_BUILD_TOOLS_API) ?: false

    val allowLegacyMppDependencies: Boolean
        get() = booleanProperty(KOTLIN_MPP_ALLOW_LEGACY_DEPENDENCIES) ?: false

    val kotlinExperimentalTryK2: Provider<Boolean> = project.providers
        .provider<Boolean> {
            booleanProperty(KOTLIN_EXPERIMENTAL_TRY_K2)
        }
        .orElse(false)

    /**
     * Outputs diagnostics in a more verbose format with synthetic delimiters, helping to parse
     * diagnostics from unstructured input (e.g. build log).
     * This mode is meant to be at least somewhat human-readable (as it's used in our tests),
     * but is not meant to be pretty (users shouldn't see it)
     */
    val internalDiagnosticsUseParsableFormat: Boolean
        get() = booleanProperty(KOTLIN_INTERNAL_DIAGNOSTICS_USE_PARSABLE_FORMATTING) ?: false

    /**
     * *Overrides* the default option for rendering a stacktrace from which a diagnostic has been reported.
     * Because it's an override, 'null' is meaningful here and signifies "prefer the choice made without this property"
     */
    val internalDiagnosticsShowStacktrace: Boolean?
        get() = booleanProperty(KOTLIN_INTERNAL_DIAGNOSTICS_SHOW_STACKTRACE)

    val suppressedGradlePluginWarnings: List<String>
        get() = property(PropertyNames.KOTLIN_SUPPRESS_GRADLE_PLUGIN_WARNINGS)?.split(",").orEmpty()

    val suppressedGradlePluginErrors: List<String>
        get() = property(PropertyNames.KOTLIN_SUPPRESS_GRADLE_PLUGIN_ERRORS)?.split(",").orEmpty()

    val suppressExperimentalArtifactsDslWarning: Boolean
        get() = booleanProperty(KOTLIN_NATIVE_SUPPRESS_EXPERIMENTAL_ARTIFACTS_DSL_WARNING) ?: false

    /**
     * Allows the user to specify a custom location for the Kotlin/Native distribution.
     * This property takes precedence over the 'KONAN_DATA_DIR' environment variable.
     */
    val konanDataDir: String?
        get() = property(PropertyNames.KONAN_DATA_DIR)

    /**
     * The directory where Kotlin global caches, logs, or project persistent data are stored.
     *
     * If the property is not set, the plugin will use `<user_home>/.kotlin` as default.
     */
    val kotlinUserHomeDir: String?
        get() = property(PropertyNames.KOTLIN_USER_HOME_DIR)

    /**
     * Retrieves a comma-separated list of browsers to use when running karma tests for [target]
     * @see KOTLIN_JS_KARMA_BROWSERS
     */
    fun jsKarmaBrowsers(target: KotlinTarget? = null): String? =
        target?.name?.prefixIfNot("$KOTLIN_JS_KARMA_BROWSERS.")?.let(::property) ?: property(KOTLIN_JS_KARMA_BROWSERS)

    private fun propertyWithDeprecatedVariant(propName: String, deprecatedPropName: String): String? {
        val deprecatedProperty = this.property(deprecatedPropName)
        if (deprecatedProperty != null) {
            project.reportDiagnosticOncePerBuild(KotlinToolingDiagnostics.DeprecatedPropertyWithReplacement(deprecatedProperty, propName))
        }
        return this.property(propName) ?: deprecatedProperty
    }

    private fun booleanProperty(propName: String): Boolean? =
        this.property(propName)?.toBoolean()

    private inline fun <reified T : Enum<T>> enumProperty(
        propName: String,
        defaultValue: T,
    ): T = this.property(propName)?.let { enumValueOf<T>(it.toUpperCaseAsciiOnly()) } ?: defaultValue

    /**
     * Looks up the property in the following sources with decreasing priority:
     * 1. Project properties (-P, gradle.properties, etc...)
     * 2. `local.properties`
     *
     * Please prefer using dedicated properties for proper defaults handling.
     * Use this API only if you specifically need declared project properties disregarding defaults.
     */
    @UnsafeApi
    internal fun property(propName: String): String? =
        if (project.hasProperty(propName)) {
            project.property(propName) as? String
        } else {
            localProperties.getProperty(propName)
        }

    private fun propertiesWithPrefix(prefix: String): Map<String, String> {
        val result: MutableMap<String, String> = mutableMapOf()
        project.properties.forEach { (name, value) ->
            if (name.startsWith(prefix) && value is String) {
                result.put(name, value)
            }
        }
        localProperties.forEach { (name, value) ->
            if (name is String && name.startsWith(prefix) && value is String) {
                // Project properties have higher priority.
                result.putIfAbsent(name, value)
            }
        }
        return result
    }

    object PropertyNames {
        private val allPropertiesStorage: MutableSet<String> = mutableSetOf()
        private fun property(name: String) = name.also { allPropertiesStorage += it }

        fun allProperties(): Set<String> = allPropertiesStorage
        fun allInternalProperties(): Set<String> = allProperties().filterTo(mutableSetOf()) { it.startsWith(KOTLIN_INTERNAL_NAMESPACE) }

        val KOTLIN_STDLIB_DEFAULT_DEPENDENCY = property("kotlin.stdlib.default.dependency")
        val KOTLIN_STDLIB_JDK_VARIANTS_VERSION_ALIGNMENT = property("kotlin.stdlib.jdk.variants.version.alignment")
        val KOTLIN_JS_STDLIB_DOM_API_INCLUDED = property("kotlin.js.stdlib.dom.api.included")
        val KOTLIN_MPP_ENABLE_GRANULAR_SOURCE_SETS_METADATA = property("kotlin.mpp.enableGranularSourceSetsMetadata")
        val KOTLIN_MPP_ENABLE_COMPATIBILITY_METADATA_VARIANT = property("kotlin.mpp.enableCompatibilityMetadataVariant")
        val KOTLIN_MPP_ENABLE_CINTEROP_COMMONIZATION = property("kotlin.mpp.enableCInteropCommonization")
        val KOTLIN_MPP_HIERARCHICAL_STRUCTURE_SUPPORT = property("kotlin.mpp.hierarchicalStructureSupport")
        val KOTLIN_MPP_ANDROID_GRADLE_PLUGIN_COMPATIBILITY_NO_WARN = property("kotlin.mpp.androidGradlePluginCompatibility.nowarn")
        val KOTLIN_MPP_ANDROID_SOURCE_SET_LAYOUT_VERSION = property("kotlin.mpp.androidSourceSetLayoutVersion")
        val KOTLIN_MPP_ANDROID_SOURCE_SET_LAYOUT_ANDROID_STYLE_NO_WARN = property("kotlin.mpp.androidSourceSetLayoutV2AndroidStyleDirs.nowarn")
        val KOTLIN_MPP_IMPORT_ENABLE_KGP_DEPENDENCY_RESOLUTION = property("kotlin.mpp.import.enableKgpDependencyResolution")
        val KOTLIN_MPP_IMPORT_ENABLE_SLOW_SOURCES_JAR_RESOLVER = property("kotlin.mpp.import.enableSlowSourcesJarResolver")
        val KOTLIN_MPP_ENABLE_INTRANSITIVE_METADATA_CONFIGURATION = property("kotlin.mpp.enableIntransitiveMetadataConfiguration")
        val KOTLIN_MPP_APPLY_DEFAULT_HIERARCHY_TEMPLATE = property("kotlin.mpp.applyDefaultHierarchyTemplate")
        val KOTLIN_NATIVE_DEPENDENCY_PROPAGATION = property("kotlin.native.enableDependencyPropagation")
        val KOTLIN_NATIVE_CACHE_ORCHESTRATION = property("kotlin.native.cacheOrchestration")
        val KOTLIN_NATIVE_PARALLEL_THREADS = property("kotlin.native.parallelThreads")
        val KOTLIN_NATIVE_INCREMENTAL_COMPILATION = property("kotlin.incremental.native")
        val KOTLIN_MPP_ENABLE_OPTIMISTIC_NUMBER_COMMONIZATION = property("kotlin.mpp.enableOptimisticNumberCommonization")
        val KOTLIN_MPP_ENABLE_PLATFORM_INTEGER_COMMONIZATION = property("kotlin.mpp.enablePlatformIntegerCommonization")
        val KOTLIN_ABI_SNAPSHOT = property("kotlin.incremental.classpath.snapshot.enabled")
        val KOTLIN_JS_KARMA_BROWSERS = property("kotlin.js.browser.karma.browsers")
        val KOTLIN_BUILD_REPORT_SINGLE_FILE = property("kotlin.build.report.single_file")
        val KOTLIN_BUILD_REPORT_HTTP_URL = property("kotlin.build.report.http.url")
        val KOTLIN_OPTIONS_SUPPRESS_FREEARGS_MODIFICATION_WARNING = property("kotlin.options.suppressFreeCompilerArgsModificationWarning")
        val KOTLIN_NATIVE_USE_XCODE_MESSAGE_STYLE = property("kotlin.native.useXcodeMessageStyle")
        val KOTLIN_COMPILER_USE_PRECISE_COMPILATION_RESULTS_BACKUP = property("kotlin.compiler.preciseCompilationResultsBackup")
        val KOTLIN_COMPILER_KEEP_INCREMENTAL_COMPILATION_CACHES_IN_MEMORY = property("kotlin.compiler.keepIncrementalCompilationCachesInMemory")
        val KOTLIN_SUPPRESS_EXPERIMENTAL_IC_OPTIMIZATIONS_WARNING = property("kotlin.compiler.suppressExperimentalICOptimizationsWarning")
        val KOTLIN_RUN_COMPILER_VIA_BUILD_TOOLS_API = property("kotlin.compiler.runViaBuildToolsApi")
        val KOTLIN_MPP_ALLOW_LEGACY_DEPENDENCIES = property("kotlin.mpp.allow.legacy.dependencies")
        val KOTLIN_PUBLISH_JVM_ENVIRONMENT_ATTRIBUTE = property("kotlin.publishJvmEnvironmentAttribute")
        val KOTLIN_EXPERIMENTAL_TRY_K2 = property("kotlin.experimental.tryK2")
        val KOTLIN_SUPPRESS_GRADLE_PLUGIN_WARNINGS = property("kotlin.suppressGradlePluginWarnings")
        val KOTLIN_NATIVE_IGNORE_DISABLED_TARGETS = property("kotlin.native.ignoreDisabledTargets")
        val KOTLIN_NATIVE_SUPPRESS_EXPERIMENTAL_ARTIFACTS_DSL_WARNING = property("kotlin.native.suppressExperimentalArtifactsDslWarning")
        val KONAN_DATA_DIR = property("konan.data.dir")
        val KOTLIN_USER_HOME_DIR = property("kotlin.user.home")

        /**
         * Internal properties: builds get big non-suppressible warning when such properties are used
         * See [org.jetbrains.kotlin.gradle.plugin.diagnostics.checkers.InternalGradlePropertiesUsageChecker]
         **/
        val KOTLIN_MPP_HIERARCHICAL_STRUCTURE_BY_DEFAULT = property("$KOTLIN_INTERNAL_NAMESPACE.mpp.hierarchicalStructureByDefault")
        val KOTLIN_CREATE_DEFAULT_MULTIPLATFORM_PUBLICATIONS = property("$KOTLIN_INTERNAL_NAMESPACE.mpp.createDefaultMultiplatformPublications")
        val KOTLIN_INTERNAL_DIAGNOSTICS_USE_PARSABLE_FORMATTING = property("$KOTLIN_INTERNAL_NAMESPACE.diagnostics.useParsableFormatting")
        val KOTLIN_INTERNAL_DIAGNOSTICS_SHOW_STACKTRACE = property("$KOTLIN_INTERNAL_NAMESPACE.diagnostics.showStacktrace")
        val KOTLIN_SUPPRESS_GRADLE_PLUGIN_ERRORS = property("$KOTLIN_INTERNAL_NAMESPACE.suppressGradlePluginErrors")
        val MPP_13X_FLAGS_SET_BY_PLUGIN = property("$KOTLIN_INTERNAL_NAMESPACE.mpp.13X.flags.setByPlugin")
    }

    companion object {
        internal const val KOTLIN_NATIVE_HOME = "kotlin.native.home"

        private const val CACHED_PROVIDER_EXT_NAME = "kotlin.properties.provider"

        internal const val KOTLIN_NATIVE_IGNORE_INCORRECT_DEPENDENCIES = "kotlin.native.ignoreIncorrectDependencies"

        private const val KOTLIN_NATIVE_BINARY_OPTION_PREFIX = "kotlin.native.binary."

        internal const val KOTLIN_INTERNAL_NAMESPACE = "kotlin.internal"

        operator fun invoke(project: Project): PropertiesProvider =
            with(project.extensions.extraProperties) {
                if (!has(CACHED_PROVIDER_EXT_NAME)) {
                    set(CACHED_PROVIDER_EXT_NAME, PropertiesProvider(project))
                }
                return get(CACHED_PROVIDER_EXT_NAME) as? PropertiesProvider
                    ?: PropertiesProvider(project) // Fallback if multiple class loaders are involved
            }

        internal val Project.kotlinPropertiesProvider get() = invoke(this)
    }
}
