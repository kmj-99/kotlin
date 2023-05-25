/*
 * Copyright 2010-2023 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.parcelize.test.runners;

import com.intellij.testFramework.TestDataPath;
import org.jetbrains.kotlin.test.util.KtTestUtil;
import org.jetbrains.kotlin.test.TargetBackend;
import org.jetbrains.kotlin.test.TestMetadata;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.regex.Pattern;

/** This class is generated by {@link org.jetbrains.kotlin.generators.tests.GenerateTestsKt}. DO NOT MODIFY MANUALLY */
@SuppressWarnings("all")
@TestMetadata("plugins/parcelize/parcelize-compiler/testData/box")
@TestDataPath("$PROJECT_ROOT")
public class ParcelizeIrBoxTestGenerated extends AbstractParcelizeIrBoxTest {
    @Test
    public void testAllFilesPresentInBox() throws Exception {
        KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("plugins/parcelize/parcelize-compiler/testData/box"), Pattern.compile("^(.+)\\.kt$"), null, TargetBackend.JVM_IR, true);
    }

    @Test
    @TestMetadata("allPrimitiveTypes.kt")
    public void testAllPrimitiveTypes() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/allPrimitiveTypes.kt");
    }

    @Test
    @TestMetadata("allUnsignedTypes.kt")
    public void testAllUnsignedTypes() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/allUnsignedTypes.kt");
    }

    @Test
    @TestMetadata("arraySimple.kt")
    public void testArraySimple() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/arraySimple.kt");
    }

    @Test
    @TestMetadata("arrays.kt")
    public void testArrays() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/arrays.kt");
    }

    @Test
    @TestMetadata("binder.kt")
    public void testBinder() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/binder.kt");
    }

    @Test
    @TestMetadata("boxedTypes.kt")
    public void testBoxedTypes() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/boxedTypes.kt");
    }

    @Test
    @TestMetadata("bundle.kt")
    public void testBundle() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/bundle.kt");
    }

    @Test
    @TestMetadata("charSequence.kt")
    public void testCharSequence() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/charSequence.kt");
    }

    @Test
    @TestMetadata("constructorWithoutValOrVar.kt")
    public void testConstructorWithoutValOrVar() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/constructorWithoutValOrVar.kt");
    }

    @Test
    @TestMetadata("customNewArray.kt")
    public void testCustomNewArray() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/customNewArray.kt");
    }

    @Test
    @TestMetadata("customParcelable.kt")
    public void testCustomParcelable() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/customParcelable.kt");
    }

    @Test
    @TestMetadata("customParcelerChecks.kt")
    public void testCustomParcelerChecks() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/customParcelerChecks.kt");
    }

    @Test
    @TestMetadata("customParcelerScoping.kt")
    public void testCustomParcelerScoping() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/customParcelerScoping.kt");
    }

    @Test
    @TestMetadata("customSerializerBoxing.kt")
    public void testCustomSerializerBoxing() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/customSerializerBoxing.kt");
    }

    @Test
    @TestMetadata("customSerializerSimple.kt")
    public void testCustomSerializerSimple() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/customSerializerSimple.kt");
    }

    @Test
    @TestMetadata("customSerializerWriteWith.kt")
    public void testCustomSerializerWriteWith() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/customSerializerWriteWith.kt");
    }

    @Test
    @TestMetadata("customSimple.kt")
    public void testCustomSimple() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/customSimple.kt");
    }

    @Test
    @TestMetadata("enumObject.kt")
    public void testEnumObject() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/enumObject.kt");
    }

    @Test
    @TestMetadata("enums.kt")
    public void testEnums() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/enums.kt");
    }

    @Test
    @TestMetadata("exceptions.kt")
    public void testExceptions() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/exceptions.kt");
    }

    @Test
    @TestMetadata("functions.kt")
    public void testFunctions() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/functions.kt");
    }

    @Test
    @TestMetadata("genericParcelable.kt")
    public void testGenericParcelable() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/genericParcelable.kt");
    }

    @Test
    @TestMetadata("generics.kt")
    public void testGenerics() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/generics.kt");
    }

    @Test
    @TestMetadata("ignoredOnParcel.kt")
    public void testIgnoredOnParcel() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/ignoredOnParcel.kt");
    }

    @Test
    @TestMetadata("intArray.kt")
    public void testIntArray() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/intArray.kt");
    }

    @Test
    @TestMetadata("javaInterop.kt")
    public void testJavaInterop() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/javaInterop.kt");
    }

    @Test
    @TestMetadata("kt19747.kt")
    public void testKt19747() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/kt19747.kt");
    }

    @Test
    @TestMetadata("kt19747Deprecated.kt")
    public void testKt19747Deprecated() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/kt19747Deprecated.kt");
    }

    @Test
    @TestMetadata("kt19747_2.kt")
    public void testKt19747_2() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/kt19747_2.kt");
    }

    @Test
    @TestMetadata("kt19749.kt")
    public void testKt19749() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/kt19749.kt");
    }

    @Test
    @TestMetadata("kt19853.kt")
    public void testKt19853() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/kt19853.kt");
    }

    @Test
    @TestMetadata("kt20002.kt")
    public void testKt20002() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/kt20002.kt");
    }

    @Test
    @TestMetadata("kt20021.kt")
    public void testKt20021() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/kt20021.kt");
    }

    @Test
    @TestMetadata("kt20717.kt")
    public void testKt20717() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/kt20717.kt");
    }

    @Test
    @TestMetadata("kt25839.kt")
    public void testKt25839() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/kt25839.kt");
    }

    @Test
    @TestMetadata("kt26221.kt")
    public void testKt26221() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/kt26221.kt");
    }

    @Test
    @TestMetadata("kt36658.kt")
    public void testKt36658() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/kt36658.kt");
    }

    @Test
    @TestMetadata("kt39981.kt")
    public void testKt39981() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/kt39981.kt");
    }

    @Test
    @TestMetadata("kt41553.kt")
    public void testKt41553() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/kt41553.kt");
    }

    @Test
    @TestMetadata("kt41553_2.kt")
    public void testKt41553_2() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/kt41553_2.kt");
    }

    @Test
    @TestMetadata("kt46567.kt")
    public void testKt46567() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/kt46567.kt");
    }

    @Test
    @TestMetadata("listKinds.kt")
    public void testListKinds() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/listKinds.kt");
    }

    @Test
    @TestMetadata("listSimple.kt")
    public void testListSimple() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/listSimple.kt");
    }

    @Test
    @TestMetadata("lists.kt")
    public void testLists() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/lists.kt");
    }

    @Test
    @TestMetadata("mapKinds.kt")
    public void testMapKinds() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/mapKinds.kt");
    }

    @Test
    @TestMetadata("mapSimple.kt")
    public void testMapSimple() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/mapSimple.kt");
    }

    @Test
    @TestMetadata("maps.kt")
    public void testMaps() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/maps.kt");
    }

    @Test
    @TestMetadata("nestedArrays.kt")
    public void testNestedArrays() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/nestedArrays.kt");
    }

    @Test
    @TestMetadata("nestedLists.kt")
    public void testNestedLists() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/nestedLists.kt");
    }

    @Test
    @TestMetadata("nestedMaps.kt")
    public void testNestedMaps() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/nestedMaps.kt");
    }

    @Test
    @TestMetadata("nestedParcelable.kt")
    public void testNestedParcelable() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/nestedParcelable.kt");
    }

    @Test
    @TestMetadata("nestedSparseArrays.kt")
    public void testNestedSparseArrays() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/nestedSparseArrays.kt");
    }

    @Test
    @TestMetadata("nestedSparseArrays2.kt")
    public void testNestedSparseArrays2() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/nestedSparseArrays2.kt");
    }

    @Test
    @TestMetadata("newArray.kt")
    public void testNewArray() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/newArray.kt");
    }

    @Test
    @TestMetadata("newArrayParceler.kt")
    public void testNewArrayParceler() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/newArrayParceler.kt");
    }

    @Test
    @TestMetadata("nullableSparseArrays.kt")
    public void testNullableSparseArrays() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/nullableSparseArrays.kt");
    }

    @Test
    @TestMetadata("nullableTypes.kt")
    public void testNullableTypes() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/nullableTypes.kt");
    }

    @Test
    @TestMetadata("nullableTypesSimple.kt")
    public void testNullableTypesSimple() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/nullableTypesSimple.kt");
    }

    @Test
    @TestMetadata("objectWriteParcelable.kt")
    public void testObjectWriteParcelable() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/objectWriteParcelable.kt");
    }

    @Test
    @TestMetadata("objects.kt")
    public void testObjects() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/objects.kt");
    }

    @Test
    @TestMetadata("openParcelize.kt")
    public void testOpenParcelize() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/openParcelize.kt");
    }

    @Test
    @TestMetadata("overriddenDescribeContents.kt")
    public void testOverriddenDescribeContents() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/overriddenDescribeContents.kt");
    }

    @Test
    @TestMetadata("parcelableValueClass.kt")
    public void testParcelableValueClass() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/parcelableValueClass.kt");
    }

    @Test
    @TestMetadata("parcelizeCustomValueClass.kt")
    public void testParcelizeCustomValueClass() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/parcelizeCustomValueClass.kt");
    }

    @Test
    @TestMetadata("persistableBundle.kt")
    public void testPersistableBundle() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/persistableBundle.kt");
    }

    @Test
    @TestMetadata("primitiveTypes.kt")
    public void testPrimitiveTypes() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/primitiveTypes.kt");
    }

    @Test
    @TestMetadata("privateConstructor.kt")
    public void testPrivateConstructor() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/privateConstructor.kt");
    }

    @Test
    @TestMetadata("recursiveGenerics.kt")
    public void testRecursiveGenerics() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/recursiveGenerics.kt");
    }

    @Test
    @TestMetadata("sealedClass.kt")
    public void testSealedClass() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/sealedClass.kt");
    }

    @Test
    @TestMetadata("sealedClass2.kt")
    public void testSealedClass2() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/sealedClass2.kt");
    }

    @Test
    @TestMetadata("sealedInterface.kt")
    public void testSealedInterface() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/sealedInterface.kt");
    }

    @Test
    @TestMetadata("shortArray.kt")
    public void testShortArray() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/shortArray.kt");
    }

    @Test
    @TestMetadata("simple.kt")
    public void testSimple() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/simple.kt");
    }

    @Test
    @TestMetadata("simpleDeprecated.kt")
    public void testSimpleDeprecated() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/simpleDeprecated.kt");
    }

    @Test
    @TestMetadata("sparseArrays.kt")
    public void testSparseArrays() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/sparseArrays.kt");
    }

    @Test
    @TestMetadata("sparseBooleanArray.kt")
    public void testSparseBooleanArray() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/sparseBooleanArray.kt");
    }

    @Test
    @TestMetadata("typeParameters.kt")
    public void testTypeParameters() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/typeParameters.kt");
    }

    @Test
    @TestMetadata("typealiasedPluginAnnotation.kt")
    public void testTypealiasedPluginAnnotation() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/typealiasedPluginAnnotation.kt");
    }

    @Test
    @TestMetadata("unsignedArrays.kt")
    public void testUnsignedArrays() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/unsignedArrays.kt");
    }

    @Test
    @TestMetadata("valueClassWrapper.kt")
    public void testValueClassWrapper() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/valueClassWrapper.kt");
    }

    @Test
    @TestMetadata("valueClasses.kt")
    public void testValueClasses() throws Exception {
        runTest("plugins/parcelize/parcelize-compiler/testData/box/valueClasses.kt");
    }
}
