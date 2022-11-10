/*
 * Copyright 2010-2022 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.light.classes.symbol.source;

import com.intellij.testFramework.TestDataPath;
import org.jetbrains.kotlin.test.util.KtTestUtil;
import org.jetbrains.kotlin.test.TestMetadata;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.regex.Pattern;

/** This class is generated by {@link org.jetbrains.kotlin.generators.tests.analysis.api.GenerateAnalysisApiTestsKt}. DO NOT MODIFY MANUALLY */
@SuppressWarnings("all")
@TestMetadata("compiler/testData/asJava/lightClasses")
@TestDataPath("$PROJECT_ROOT")
public class SymbolLightClassesParentingForSourceTestGenerated extends AbstractSymbolLightClassesParentingForSourceTest {
    @Test
    public void testAllFilesPresentInLightClasses() throws Exception {
        KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("compiler/testData/asJava/lightClasses"), Pattern.compile("^([^.]+)\\.kt$"), null, true);
    }

    @Test
    @TestMetadata("AnnotatedParameterInEnumConstructor.kt")
    public void testAnnotatedParameterInEnumConstructor() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/AnnotatedParameterInEnumConstructor.kt");
    }

    @Test
    @TestMetadata("AnnotatedParameterInInnerClassConstructor.kt")
    public void testAnnotatedParameterInInnerClassConstructor() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/AnnotatedParameterInInnerClassConstructor.kt");
    }

    @Test
    @TestMetadata("AnnotatedPropertyWithSites.kt")
    public void testAnnotatedPropertyWithSites() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/AnnotatedPropertyWithSites.kt");
    }

    @Test
    @TestMetadata("AnnotationClass.kt")
    public void testAnnotationClass() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/AnnotationClass.kt");
    }

    @Test
    @TestMetadata("AnnotationJvmRepeatable.kt")
    public void testAnnotationJvmRepeatable() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/AnnotationJvmRepeatable.kt");
    }

    @Test
    @TestMetadata("AnnotationKotlinAndJavaRepeatable.kt")
    public void testAnnotationKotlinAndJavaRepeatable() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/AnnotationKotlinAndJavaRepeatable.kt");
    }

    @Test
    @TestMetadata("AnnotationKotlinAndJvmRepeatable.kt")
    public void testAnnotationKotlinAndJvmRepeatable() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/AnnotationKotlinAndJvmRepeatable.kt");
    }

    @Test
    @TestMetadata("AnnotationRepeatable.kt")
    public void testAnnotationRepeatable() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/AnnotationRepeatable.kt");
    }

    @Test
    @TestMetadata("Constructors.kt")
    public void testConstructors() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/Constructors.kt");
    }

    @Test
    @TestMetadata("DataClassWithCustomImplementedMembers.kt")
    public void testDataClassWithCustomImplementedMembers() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/DataClassWithCustomImplementedMembers.kt");
    }

    @Test
    @TestMetadata("DelegatedNested.kt")
    public void testDelegatedNested() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/DelegatedNested.kt");
    }

    @Test
    @TestMetadata("Delegation.kt")
    public void testDelegation() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/Delegation.kt");
    }

    @Test
    @TestMetadata("DeprecatedEnumEntry.kt")
    public void testDeprecatedEnumEntry() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/DeprecatedEnumEntry.kt");
    }

    @Test
    @TestMetadata("DeprecatedNotHiddenInClass.kt")
    public void testDeprecatedNotHiddenInClass() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/DeprecatedNotHiddenInClass.kt");
    }

    @Test
    @TestMetadata("DollarsInName.kt")
    public void testDollarsInName() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/DollarsInName.kt");
    }

    @Test
    @TestMetadata("DollarsInNameNoPackage.kt")
    public void testDollarsInNameNoPackage() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/DollarsInNameNoPackage.kt");
    }

    @Test
    @TestMetadata("ExtendingInterfaceWithDefaultImpls.kt")
    public void testExtendingInterfaceWithDefaultImpls() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/ExtendingInterfaceWithDefaultImpls.kt");
    }

    @Test
    @TestMetadata("HiddenDeprecated.kt")
    public void testHiddenDeprecated() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/HiddenDeprecated.kt");
    }

    @Test
    @TestMetadata("HiddenDeprecatedInClass.kt")
    public void testHiddenDeprecatedInClass() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/HiddenDeprecatedInClass.kt");
    }

    @Test
    @TestMetadata("InheritingInterfaceDefaultImpls.kt")
    public void testInheritingInterfaceDefaultImpls() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/InheritingInterfaceDefaultImpls.kt");
    }

    @Test
    @TestMetadata("InlineReified.kt")
    public void testInlineReified() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/InlineReified.kt");
    }

    @Test
    @TestMetadata("JavaBetween.kt")
    public void testJavaBetween() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/JavaBetween.kt");
    }

    @Test
    @TestMetadata("JvmNameOnMember.kt")
    public void testJvmNameOnMember() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/JvmNameOnMember.kt");
    }

    @Test
    @TestMetadata("JvmStatic.kt")
    public void testJvmStatic() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/JvmStatic.kt");
    }

    @Test
    @TestMetadata("LocalFunctions.kt")
    public void testLocalFunctions() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/LocalFunctions.kt");
    }

    @Test
    @TestMetadata("NestedObjects.kt")
    public void testNestedObjects() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/NestedObjects.kt");
    }

    @Test
    @TestMetadata("NonDataClassWithComponentFunctions.kt")
    public void testNonDataClassWithComponentFunctions() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/NonDataClassWithComponentFunctions.kt");
    }

    @Test
    @TestMetadata("OnlySecondaryConstructors.kt")
    public void testOnlySecondaryConstructors() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/OnlySecondaryConstructors.kt");
    }

    @Test
    @TestMetadata("PublishedApi.kt")
    public void testPublishedApi() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/PublishedApi.kt");
    }

    @Test
    @TestMetadata("SpecialAnnotationsOnAnnotationClass.kt")
    public void testSpecialAnnotationsOnAnnotationClass() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/SpecialAnnotationsOnAnnotationClass.kt");
    }

    @Test
    @TestMetadata("StubOrderForOverloads.kt")
    public void testStubOrderForOverloads() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/StubOrderForOverloads.kt");
    }

    @Test
    @TestMetadata("TypePararametersInClass.kt")
    public void testTypePararametersInClass() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/TypePararametersInClass.kt");
    }

    @Test
    @TestMetadata("VarArgs.kt")
    public void testVarArgs() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/VarArgs.kt");
    }

    @Nested
    @TestMetadata("compiler/testData/asJava/lightClasses/compilationErrors")
    @TestDataPath("$PROJECT_ROOT")
    public class CompilationErrors {
        @Test
        @TestMetadata("ActualClass.kt")
        public void testActualClass() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/compilationErrors/ActualClass.kt");
        }

        @Test
        @TestMetadata("ActualTypeAlias.kt")
        public void testActualTypeAlias() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/compilationErrors/ActualTypeAlias.kt");
        }

        @Test
        @TestMetadata("ActualTypeAliasCustomJvmPackageName.kt")
        public void testActualTypeAliasCustomJvmPackageName() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/compilationErrors/ActualTypeAliasCustomJvmPackageName.kt");
        }

        @Test
        public void testAllFilesPresentInCompilationErrors() throws Exception {
            KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("compiler/testData/asJava/lightClasses/compilationErrors"), Pattern.compile("^([^.]+)\\.kt$"), null, true);
        }

        @Test
        @TestMetadata("AllInlineOnly.kt")
        public void testAllInlineOnly() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/compilationErrors/AllInlineOnly.kt");
        }

        @Test
        @TestMetadata("AnnotationModifiers.kt")
        public void testAnnotationModifiers() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/compilationErrors/AnnotationModifiers.kt");
        }

        @Test
        @TestMetadata("EnumNameOverride.kt")
        public void testEnumNameOverride() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/compilationErrors/EnumNameOverride.kt");
        }

        @Test
        @TestMetadata("ExpectClass.kt")
        public void testExpectClass() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/compilationErrors/ExpectClass.kt");
        }

        @Test
        @TestMetadata("ExpectObject.kt")
        public void testExpectObject() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/compilationErrors/ExpectObject.kt");
        }

        @Test
        @TestMetadata("ExpectedNestedClass.kt")
        public void testExpectedNestedClass() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/compilationErrors/ExpectedNestedClass.kt");
        }

        @Test
        @TestMetadata("ExpectedNestedClassInObject.kt")
        public void testExpectedNestedClassInObject() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/compilationErrors/ExpectedNestedClassInObject.kt");
        }

        @Test
        @TestMetadata("FunctionWithoutName.kt")
        public void testFunctionWithoutName() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/compilationErrors/FunctionWithoutName.kt");
        }

        @Test
        @TestMetadata("JvmPackageName.kt")
        public void testJvmPackageName() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/compilationErrors/JvmPackageName.kt");
        }

        @Test
        @TestMetadata("LocalInAnnotation.kt")
        public void testLocalInAnnotation() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/compilationErrors/LocalInAnnotation.kt");
        }

        @Test
        @TestMetadata("PrivateInTrait.kt")
        public void testPrivateInTrait() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/compilationErrors/PrivateInTrait.kt");
        }

        @Test
        @TestMetadata("PropertyWithoutName.kt")
        public void testPropertyWithoutName() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/compilationErrors/PropertyWithoutName.kt");
        }

        @Test
        @TestMetadata("RepetableAnnotations.kt")
        public void testRepetableAnnotations() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/compilationErrors/RepetableAnnotations.kt");
        }

        @Test
        @TestMetadata("SameName.kt")
        public void testSameName() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/compilationErrors/SameName.kt");
        }

        @Test
        @TestMetadata("TopLevelDestructuring.kt")
        public void testTopLevelDestructuring() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/compilationErrors/TopLevelDestructuring.kt");
        }

        @Test
        @TestMetadata("TraitClassObjectField.kt")
        public void testTraitClassObjectField() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/compilationErrors/TraitClassObjectField.kt");
        }

        @Test
        @TestMetadata("TwoOverrides.kt")
        public void testTwoOverrides() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/compilationErrors/TwoOverrides.kt");
        }

        @Test
        @TestMetadata("WrongAnnotations.kt")
        public void testWrongAnnotations() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/compilationErrors/WrongAnnotations.kt");
        }
    }

    @Nested
    @TestMetadata("compiler/testData/asJava/lightClasses/delegation")
    @TestDataPath("$PROJECT_ROOT")
    public class Delegation {
        @Test
        public void testAllFilesPresentInDelegation() throws Exception {
            KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("compiler/testData/asJava/lightClasses/delegation"), Pattern.compile("^([^.]+)\\.kt$"), null, true);
        }

        @Test
        @TestMetadata("Function.kt")
        public void testFunction() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/delegation/Function.kt");
        }

        @Test
        @TestMetadata("Property.kt")
        public void testProperty() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/delegation/Property.kt");
        }
    }

    @Nested
    @TestMetadata("compiler/testData/asJava/lightClasses/facades")
    @TestDataPath("$PROJECT_ROOT")
    public class Facades {
        @Test
        public void testAllFilesPresentInFacades() throws Exception {
            KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("compiler/testData/asJava/lightClasses/facades"), Pattern.compile("^([^.]+)\\.kt$"), null, true);
        }

        @Test
        @TestMetadata("AllPrivate.kt")
        public void testAllPrivate() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/facades/AllPrivate.kt");
        }

        @Test
        @TestMetadata("MultiFile.kt")
        public void testMultiFile() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/facades/MultiFile.kt");
        }

        @Test
        @TestMetadata("SingleFile.kt")
        public void testSingleFile() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/facades/SingleFile.kt");
        }

        @Test
        @TestMetadata("SingleJvmClassName.kt")
        public void testSingleJvmClassName() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/facades/SingleJvmClassName.kt");
        }
    }

    @Nested
    @TestMetadata("compiler/testData/asJava/lightClasses/ideRegression")
    @TestDataPath("$PROJECT_ROOT")
    public class IdeRegression {
        @Test
        public void testAllFilesPresentInIdeRegression() throws Exception {
            KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("compiler/testData/asJava/lightClasses/ideRegression"), Pattern.compile("^([^.]+)\\.kt$"), null, true);
        }

        @Test
        @TestMetadata("AllOpenAnnotatedClasses.kt")
        public void testAllOpenAnnotatedClasses() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/ideRegression/AllOpenAnnotatedClasses.kt");
        }

        @Test
        @TestMetadata("ImplementingCharSequenceAndNumber.kt")
        public void testImplementingCharSequenceAndNumber() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/ideRegression/ImplementingCharSequenceAndNumber.kt");
        }

        @Test
        @TestMetadata("ImplementingMap.kt")
        public void testImplementingMap() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/ideRegression/ImplementingMap.kt");
        }

        @Test
        @TestMetadata("ImplementingMutableSet.kt")
        public void testImplementingMutableSet() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/ideRegression/ImplementingMutableSet.kt");
        }

        @Test
        @TestMetadata("InheritingInterfaceDefaultImpls.kt")
        public void testInheritingInterfaceDefaultImpls() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/ideRegression/InheritingInterfaceDefaultImpls.kt");
        }

        @Test
        @TestMetadata("OverridingFinalInternal.kt")
        public void testOverridingFinalInternal() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/ideRegression/OverridingFinalInternal.kt");
        }

        @Test
        @TestMetadata("OverridingInternal.kt")
        public void testOverridingInternal() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/ideRegression/OverridingInternal.kt");
        }

        @Test
        @TestMetadata("OverridingProtected.kt")
        public void testOverridingProtected() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/ideRegression/OverridingProtected.kt");
        }
    }

    @Nested
    @TestMetadata("compiler/testData/asJava/lightClasses/nullabilityAnnotations")
    @TestDataPath("$PROJECT_ROOT")
    public class NullabilityAnnotations {
        @Test
        public void testAllFilesPresentInNullabilityAnnotations() throws Exception {
            KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("compiler/testData/asJava/lightClasses/nullabilityAnnotations"), Pattern.compile("^([^.]+)\\.kt$"), null, true);
        }

        @Test
        @TestMetadata("Class.kt")
        public void testClass() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/nullabilityAnnotations/Class.kt");
        }

        @Test
        @TestMetadata("ClassObjectField.kt")
        public void testClassObjectField() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/nullabilityAnnotations/ClassObjectField.kt");
        }

        @Test
        @TestMetadata("ClassWithConstructor.kt")
        public void testClassWithConstructor() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/nullabilityAnnotations/ClassWithConstructor.kt");
        }

        @Test
        @TestMetadata("ClassWithConstructorAndProperties.kt")
        public void testClassWithConstructorAndProperties() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/nullabilityAnnotations/ClassWithConstructorAndProperties.kt");
        }

        @Test
        @TestMetadata("FileFacade.kt")
        public void testFileFacade() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/nullabilityAnnotations/FileFacade.kt");
        }

        @Test
        @TestMetadata("Generic.kt")
        public void testGeneric() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/nullabilityAnnotations/Generic.kt");
        }

        @Test
        @TestMetadata("IntOverridesAny.kt")
        public void testIntOverridesAny() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/nullabilityAnnotations/IntOverridesAny.kt");
        }

        @Test
        @TestMetadata("JvmOverloads.kt")
        public void testJvmOverloads() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/nullabilityAnnotations/JvmOverloads.kt");
        }

        @Test
        @TestMetadata("NullableUnitReturn.kt")
        public void testNullableUnitReturn() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/nullabilityAnnotations/NullableUnitReturn.kt");
        }

        @Test
        @TestMetadata("OverrideAnyWithUnit.kt")
        public void testOverrideAnyWithUnit() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/nullabilityAnnotations/OverrideAnyWithUnit.kt");
        }

        @Test
        @TestMetadata("PlatformTypes.kt")
        public void testPlatformTypes() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/nullabilityAnnotations/PlatformTypes.kt");
        }

        @Test
        @TestMetadata("Primitives.kt")
        public void testPrimitives() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/nullabilityAnnotations/Primitives.kt");
        }

        @Test
        @TestMetadata("PrivateInClass.kt")
        public void testPrivateInClass() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/nullabilityAnnotations/PrivateInClass.kt");
        }

        @Test
        @TestMetadata("Synthetic.kt")
        public void testSynthetic() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/nullabilityAnnotations/Synthetic.kt");
        }

        @Test
        @TestMetadata("Trait.kt")
        public void testTrait() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/nullabilityAnnotations/Trait.kt");
        }

        @Test
        @TestMetadata("UnitAsGenericArgument.kt")
        public void testUnitAsGenericArgument() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/nullabilityAnnotations/UnitAsGenericArgument.kt");
        }

        @Test
        @TestMetadata("UnitParameter.kt")
        public void testUnitParameter() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/nullabilityAnnotations/UnitParameter.kt");
        }

        @Test
        @TestMetadata("VoidReturn.kt")
        public void testVoidReturn() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/nullabilityAnnotations/VoidReturn.kt");
        }
    }

    @Nested
    @TestMetadata("compiler/testData/asJava/lightClasses/object")
    @TestDataPath("$PROJECT_ROOT")
    public class Object {
        @Test
        public void testAllFilesPresentInObject() throws Exception {
            KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("compiler/testData/asJava/lightClasses/object"), Pattern.compile("^([^.]+)\\.kt$"), null, true);
        }

        @Test
        @TestMetadata("SimpleObject.kt")
        public void testSimpleObject() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/object/SimpleObject.kt");
        }
    }

    @Nested
    @TestMetadata("compiler/testData/asJava/lightClasses/publicField")
    @TestDataPath("$PROJECT_ROOT")
    public class PublicField {
        @Test
        public void testAllFilesPresentInPublicField() throws Exception {
            KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("compiler/testData/asJava/lightClasses/publicField"), Pattern.compile("^([^.]+)\\.kt$"), null, true);
        }

        @Test
        @TestMetadata("CompanionObject.kt")
        public void testCompanionObject() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/publicField/CompanionObject.kt");
        }

        @Test
        @TestMetadata("Simple.kt")
        public void testSimple() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/publicField/Simple.kt");
        }
    }

    @Nested
    @TestMetadata("compiler/testData/asJava/lightClasses/script")
    @TestDataPath("$PROJECT_ROOT")
    public class Script {
        @Test
        public void testAllFilesPresentInScript() throws Exception {
            KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("compiler/testData/asJava/lightClasses/script"), Pattern.compile("^([^.]+)\\.kt$"), null, true);
        }
    }
}
