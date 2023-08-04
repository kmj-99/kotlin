/*
 * Copyright 2010-2023 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.resolve.calls.mpp

import org.jetbrains.kotlin.mpp.*
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.StandardClassIds
import org.jetbrains.kotlin.resolve.multiplatform.ExpectActualCompatibility
import org.jetbrains.kotlin.types.model.KotlinTypeMarker
import org.jetbrains.kotlin.resolve.multiplatform.ExpectActualAnnotationsIncompatibilityType as IncompatibilityType

object AbstractExpectActualAnnotationMatchChecker {
    private val SKIPPED_CLASS_IDS = setOf(
        StandardClassIds.Annotations.Deprecated,
        StandardClassIds.Annotations.DeprecatedSinceKotlin,
        StandardClassIds.Annotations.OptionalExpectation,
        StandardClassIds.Annotations.RequireKotlin,
        StandardClassIds.Annotations.SinceKotlin,
        StandardClassIds.Annotations.Suppress,
        StandardClassIds.Annotations.WasExperimental,
    )

    class Incompatibility(
        val expectSymbol: DeclarationSymbolMarker,
        val actualSymbol: DeclarationSymbolMarker,
        val type: IncompatibilityType<ExpectActualMatchingContext.AnnotationCallInfo>,
    )

    sealed class ClassMembersCheck private constructor() {
        data object Disabled : ClassMembersCheck()
        class Enabled(internal val expectForActualFinder: ExpectForActualFinder) : ClassMembersCheck()
    }

    fun interface ExpectForActualFinder {
        fun findExpectForActual(
            actualSymbol: DeclarationSymbolMarker,
            actualClass: RegularClassSymbolMarker,
            expectClass: RegularClassSymbolMarker,
        ): Map<out ExpectActualCompatibility<*>, List<DeclarationSymbolMarker>>
    }

    fun areAnnotationsCompatible(
        expectSymbol: DeclarationSymbolMarker,
        actualSymbol: DeclarationSymbolMarker,
        context: ExpectActualMatchingContext<*>,
        classMembersCheckPreference: ClassMembersCheck,
    ): Incompatibility? = with(context) {
        areAnnotationsCompatible(expectSymbol, actualSymbol, classMembersCheckPreference)
    }

    context (ExpectActualMatchingContext<*>)
    private fun areAnnotationsCompatible(
        expectSymbol: DeclarationSymbolMarker,
        actualSymbol: DeclarationSymbolMarker,
        classMembersCheckPreference: ClassMembersCheck,
    ): Incompatibility? {
        return when (expectSymbol) {
            is CallableSymbolMarker -> {
                areCallableAnnotationsCompatible(expectSymbol, actualSymbol as CallableSymbolMarker)
            }
            is RegularClassSymbolMarker -> {
                areClassAnnotationsCompatible(expectSymbol, actualSymbol as ClassLikeSymbolMarker, classMembersCheckPreference)
            }
            else -> error("Incorrect types: $expectSymbol $actualSymbol")
        }
    }

    context (ExpectActualMatchingContext<*>)
    private fun areCallableAnnotationsCompatible(
        expectSymbol: CallableSymbolMarker,
        actualSymbol: CallableSymbolMarker,
    ): Incompatibility? {
        commonForClassAndCallableChecks(expectSymbol, actualSymbol)?.let { return it }
        areAnnotationsOnValueParametersCompatible(expectSymbol, actualSymbol)?.let { return it }

        if (expectSymbol is PropertySymbolMarker && actualSymbol is PropertySymbolMarker) {
            arePropertyGetterAndSetterAnnotationsCompatible(expectSymbol, actualSymbol)?.let { return it }
        }

        areAnnotationsSetOnTypesCompatible(expectSymbol.extensionReceiverType, actualSymbol.extensionReceiverType, actualSymbol)
            ?.let { return Incompatibility(expectSymbol, actualSymbol, type = it) }

        areAnnotationsSetOnTypesCompatible(expectSymbol.returnType, actualSymbol.returnType, actualSymbol)
            ?.let { return Incompatibility(expectSymbol, actualSymbol, type = it) }

        return null
    }

    context (ExpectActualMatchingContext<*>)
    private fun arePropertyGetterAndSetterAnnotationsCompatible(
        expectSymbol: PropertySymbolMarker,
        actualSymbol: PropertySymbolMarker,
    ): Incompatibility? {
        val accessorsToCheck = listOf(
            expectSymbol.getter to actualSymbol.getter,
            expectSymbol.setter to actualSymbol.setter,
        )
        return accessorsToCheck.firstNotNullOfOrNull { (expect, actual) ->
            if (expect != null && actual != null) {
                areAnnotationsSetOnDeclarationsCompatible(expect, actual)
            } else {
                null
            }
        }?.let {
            // Write the entire property into diagnostic, not only accessors
            Incompatibility(expectSymbol, actualSymbol, it.type)
        }
    }

    context (ExpectActualMatchingContext<*>)
    private fun areClassAnnotationsCompatible(
        expectSymbol: RegularClassSymbolMarker,
        actualSymbol: ClassLikeSymbolMarker,
        classMembersCheckPreference: ClassMembersCheck,
    ): Incompatibility? {
        if (actualSymbol is TypeAliasSymbolMarker) {
            val expanded = actualSymbol.expandToRegularClass() ?: return null
            return areClassAnnotationsCompatible(expectSymbol, expanded, classMembersCheckPreference)
        }
        check(actualSymbol is RegularClassSymbolMarker)

        commonForClassAndCallableChecks(expectSymbol, actualSymbol)?.let { return it }

        when (classMembersCheckPreference) {
            is ClassMembersCheck.Enabled ->
                checkAnnotationsInClassMemberScope(actualSymbol, expectSymbol, classMembersCheckPreference)
                    ?.let { return it }

            ClassMembersCheck.Disabled -> {}
        }

        val actualSuperTypes = actualSymbol.superTypes.groupBy { it.getClassId() }
        for (expectSuperType in expectSymbol.superTypes) {
            val actualSuperType = actualSuperTypes[expectSuperType.getClassId()]?.singleOrNull() ?: continue
            areAnnotationsSetOnTypesCompatible(expectSuperType, actualSuperType, actualSymbol)
                ?.let { return Incompatibility(expectSymbol, actualSymbol, type = it) }
        }

        return null
    }

    context (ExpectActualMatchingContext<*>)
    private fun commonForClassAndCallableChecks(
        expectSymbol: DeclarationSymbolMarker,
        actualSymbol: DeclarationSymbolMarker,
    ): Incompatibility? {
        areAnnotationsSetOnDeclarationsCompatible(expectSymbol, actualSymbol)?.let { return it }
        areAnnotationsOnTypeParametersCompatible(expectSymbol, actualSymbol)?.let { return it }

        return null
    }

    context (ExpectActualMatchingContext<*>)
    private fun areAnnotationsOnValueParametersCompatible(
        expectSymbol: CallableSymbolMarker,
        actualSymbol: CallableSymbolMarker,
    ): Incompatibility? {
        val expectParams = expectSymbol.valueParameters
        val actualParams = actualSymbol.valueParameters
        return expectParams.zip(actualParams).firstNotNullOfOrNull { (expectParam, actualParam) ->
            areAnnotationsSetOnDeclarationsCompatible(expectParam, actualParam)?.type
                ?: areAnnotationsSetOnTypesCompatible(expectParam.returnType, actualParam.returnType, actualSymbol)
        }?.let {
            // Write the entire declaration in diagnostic
            Incompatibility(expectSymbol, actualSymbol, type = it)
        }
    }

    context (ExpectActualMatchingContext<*>)
    private fun areAnnotationsOnTypeParametersCompatible(
        expectSymbol: DeclarationSymbolMarker,
        actualSymbol: DeclarationSymbolMarker,
    ): Incompatibility? {
        fun DeclarationSymbolMarker.getTypeParameters(): List<TypeParameterSymbolMarker>? {
            return when (this) {
                is FunctionSymbolMarker -> typeParameters
                is RegularClassSymbolMarker -> typeParameters
                else -> null
            }
        }

        val expectParams = expectSymbol.getTypeParameters() ?: return null
        val actualParams = actualSymbol.getTypeParameters() ?: return null

        return expectParams.zip(actualParams).firstNotNullOfOrNull { (expectParam, actualParam) ->
            areAnnotationsSetOnDeclarationsCompatible(expectParam, actualParam)?.type
                ?: areAnnotationsOnTypeParameterBoundsCompatible(expectParam, actualParam, actualSymbol)
        }?.let {
            // Write the entire declaration in diagnostic
            Incompatibility(expectSymbol, actualSymbol, type = it)
        }
    }

    context (ExpectActualMatchingContext<*>)
    private fun areAnnotationsOnTypeParameterBoundsCompatible(
        expectParam: TypeParameterSymbolMarker,
        actualParam: TypeParameterSymbolMarker,
        actualContainerSymbol: DeclarationSymbolMarker,
    ): IncompatibilityType<ExpectActualMatchingContext.AnnotationCallInfo>? {
        return expectParam.bounds.zip(actualParam.bounds).firstNotNullOfOrNull { (expectType, actualType) ->
            areAnnotationsSetOnTypesCompatible(expectType, actualType, actualContainerSymbol)
        }
    }

    context (ExpectActualMatchingContext<*>)
    private fun areAnnotationsSetOnTypesCompatible(
        expectType: KotlinTypeMarker?,
        actualType: KotlinTypeMarker?,
        actualContainerSymbol: DeclarationSymbolMarker,
    ): IncompatibilityType<ExpectActualMatchingContext.AnnotationCallInfo>? {
        if (expectType == null || actualType == null) {
            return null
        }
        areAnnotationListsCompatible(expectType.getAnnotations(), actualType.getAnnotations(), actualContainerSymbol)
            ?.let { return it }

        val expectTypeArgs = expectType.asSimpleType()?.getArguments() ?: return null
        val actualTypeArgs = actualType.asSimpleType()?.getArguments() ?: return null

        if (expectTypeArgs.size == actualTypeArgs.size) {
            for ((expectArg, actualArg) in expectTypeArgs.zip(actualTypeArgs)) {
                if (expectArg.isStarProjection() || actualArg.isStarProjection()) {
                    continue
                }
                areAnnotationsSetOnTypesCompatible(expectArg.getType(), actualArg.getType(), actualContainerSymbol)
                    ?.let { return it }
            }
        }
        return null
    }

    context (ExpectActualMatchingContext<*>)
    private fun areAnnotationsSetOnDeclarationsCompatible(
        expectSymbol: DeclarationSymbolMarker,
        actualSymbol: DeclarationSymbolMarker,
    ): Incompatibility? {
        return areAnnotationListsCompatible(expectSymbol.annotations, actualSymbol.annotations, actualSymbol)
            ?.let { Incompatibility(expectSymbol, actualSymbol, it) }
    }

    context (ExpectActualMatchingContext<*>)
    private fun areAnnotationListsCompatible(
        expectAnnotations: List<ExpectActualMatchingContext.AnnotationCallInfo>,
        actualAnnotations: List<ExpectActualMatchingContext.AnnotationCallInfo>,
        actualContainerSymbol: DeclarationSymbolMarker,
    ): IncompatibilityType<ExpectActualMatchingContext.AnnotationCallInfo>? {
        val skipSourceAnnotations = actualContainerSymbol.hasSourceAnnotationsErased
        val actualAnnotationsByName = actualAnnotations.groupBy { it.classId }

        for (expectAnnotation in expectAnnotations) {
            val expectClassId = expectAnnotation.classId ?: continue
            if (expectClassId in SKIPPED_CLASS_IDS || expectAnnotation.isOptIn) {
                continue
            }
            if (expectAnnotation.isRetentionSource && skipSourceAnnotations) {
                continue
            }
            val actualAnnotationsWithSameClassId = actualAnnotationsByName[expectClassId] ?: emptyList()
            if (actualAnnotationsWithSameClassId.isEmpty()) {
                return IncompatibilityType.MissingOnActual(expectAnnotation)
            }
            val collectionCompatibilityChecker = getAnnotationCollectionArgumentsCompatibilityChecker(expectClassId)
            if (actualAnnotationsWithSameClassId.none {
                    areAnnotationArgumentsEqual(expectAnnotation, it, collectionCompatibilityChecker)
                }) {
                return if (actualAnnotationsWithSameClassId.size == 1) {
                    IncompatibilityType.DifferentOnActual(expectAnnotation, actualAnnotationsWithSameClassId.single())
                } else {
                    // In the case of repeatable annotations, we can't choose on which to report
                    IncompatibilityType.MissingOnActual(expectAnnotation)
                }
            }
        }
        return null
    }

    private fun getAnnotationCollectionArgumentsCompatibilityChecker(annotationClassId: ClassId):
            ExpectActualCollectionArgumentsCompatibilityCheckStrategy {
        return if (annotationClassId == StandardClassIds.Annotations.Target) {
            ExpectActualCollectionArgumentsCompatibilityCheckStrategy.ExpectIsSubsetOfActual
        } else {
            ExpectActualCollectionArgumentsCompatibilityCheckStrategy.Default
        }
    }

    context (ExpectActualMatchingContext<*>)
    private fun checkAnnotationsInClassMemberScope(
        actualClass: RegularClassSymbolMarker,
        expectClass: RegularClassSymbolMarker,
        classMemberCheck: ClassMembersCheck.Enabled,
    ): Incompatibility? {
        for (actualMember in actualClass.collectAllMembers(isActualDeclaration = true)) {
            val expectForActualMap = classMemberCheck.expectForActualFinder.findExpectForActual(actualMember, actualClass, expectClass)
            val expectMember = expectForActualMap[ExpectActualCompatibility.Compatible]?.singleOrNull() ?: continue
            areAnnotationsCompatible(expectMember, actualMember, classMemberCheck)?.let { return it }
        }
        return null
    }
}