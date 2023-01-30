/*
 * Copyright 2010-2023 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.konan.blackboxtest;

import com.intellij.testFramework.TestDataPath;
import org.jetbrains.kotlin.test.util.KtTestUtil;
import org.jetbrains.kotlin.test.TestMetadata;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.regex.Pattern;

/** This class is generated by {@link org.jetbrains.kotlin.generators.tests.GenerateNativeTestsKt}. DO NOT MODIFY MANUALLY */
@SuppressWarnings("all")
@TestMetadata("compiler/testData/klibABI")
@TestDataPath("$PROJECT_ROOT")
public class KlibABITestGenerated extends AbstractNativeKlibABITest {
    @Test
    @TestMetadata("addEnumEntry")
    public void testAddEnumEntry() throws Exception {
        runTest("compiler/testData/klibABI/addEnumEntry/");
    }

    @Test
    @TestMetadata("addSealedSubclass")
    public void testAddSealedSubclass() throws Exception {
        runTest("compiler/testData/klibABI/addSealedSubclass/");
    }

    @Test
    public void testAllFilesPresentInKlibABI() throws Exception {
        KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("compiler/testData/klibABI"), Pattern.compile("^([^_](.+))$"), null, false);
    }

    @Test
    @TestMetadata("changeClassVisibility")
    public void testChangeClassVisibility() throws Exception {
        runTest("compiler/testData/klibABI/changeClassVisibility/");
    }

    @Test
    @TestMetadata("changeFunctionVisibility")
    public void testChangeFunctionVisibility() throws Exception {
        runTest("compiler/testData/klibABI/changeFunctionVisibility/");
    }

    @Test
    @TestMetadata("changePropertyVisibility")
    public void testChangePropertyVisibility() throws Exception {
        runTest("compiler/testData/klibABI/changePropertyVisibility/");
    }

    @Test
    @TestMetadata("classTransformations")
    public void testClassTransformations() throws Exception {
        runTest("compiler/testData/klibABI/classTransformations/");
    }

    @Test
    @TestMetadata("inheritanceIssues")
    public void testInheritanceIssues() throws Exception {
        runTest("compiler/testData/klibABI/inheritanceIssues/");
    }

    @Test
    @TestMetadata("noNonImplementedCallableFalsePositives")
    public void testNoNonImplementedCallableFalsePositives() throws Exception {
        runTest("compiler/testData/klibABI/noNonImplementedCallableFalsePositives/");
    }

    @Test
    @TestMetadata("nonAbstractFunctionInAbstractClassBecomesAbstract")
    public void testNonAbstractFunctionInAbstractClassBecomesAbstract() throws Exception {
        runTest("compiler/testData/klibABI/nonAbstractFunctionInAbstractClassBecomesAbstract/");
    }

    @Test
    @TestMetadata("nonAbstractFunctionInInterfaceBecomesAbstract")
    public void testNonAbstractFunctionInInterfaceBecomesAbstract() throws Exception {
        runTest("compiler/testData/klibABI/nonAbstractFunctionInInterfaceBecomesAbstract/");
    }

    @Test
    @TestMetadata("nonAbstractPropertyInAbstractClassBecomesAbstract")
    public void testNonAbstractPropertyInAbstractClassBecomesAbstract() throws Exception {
        runTest("compiler/testData/klibABI/nonAbstractPropertyInAbstractClassBecomesAbstract/");
    }

    @Test
    @TestMetadata("nonAbstractPropertyInInterfaceBecomesAbstract")
    public void testNonAbstractPropertyInInterfaceBecomesAbstract() throws Exception {
        runTest("compiler/testData/klibABI/nonAbstractPropertyInInterfaceBecomesAbstract/");
    }

    @Test
    @TestMetadata("nonExhaustivenessOfWhenClause")
    public void testNonExhaustivenessOfWhenClause() throws Exception {
        runTest("compiler/testData/klibABI/nonExhaustivenessOfWhenClause/");
    }

    @Test
    @TestMetadata("referencingUnusableDeclarations")
    public void testReferencingUnusableDeclarations() throws Exception {
        runTest("compiler/testData/klibABI/referencingUnusableDeclarations/");
    }

    @Test
    @TestMetadata("removeAbstractFunctionFromAbstractClass")
    public void testRemoveAbstractFunctionFromAbstractClass() throws Exception {
        runTest("compiler/testData/klibABI/removeAbstractFunctionFromAbstractClass/");
    }

    @Test
    @TestMetadata("removeAbstractFunctionFromInterface")
    public void testRemoveAbstractFunctionFromInterface() throws Exception {
        runTest("compiler/testData/klibABI/removeAbstractFunctionFromInterface/");
    }

    @Test
    @TestMetadata("removeAbstractPropertyFromAbstractClass")
    public void testRemoveAbstractPropertyFromAbstractClass() throws Exception {
        runTest("compiler/testData/klibABI/removeAbstractPropertyFromAbstractClass/");
    }

    @Test
    @TestMetadata("removeAbstractPropertyFromInterface")
    public void testRemoveAbstractPropertyFromInterface() throws Exception {
        runTest("compiler/testData/klibABI/removeAbstractPropertyFromInterface/");
    }

    @Test
    @TestMetadata("removeClassAsConstructorCall")
    public void testRemoveClassAsConstructorCall() throws Exception {
        runTest("compiler/testData/klibABI/removeClassAsConstructorCall/");
    }

    @Test
    @TestMetadata("removeClassAsParameterType")
    public void testRemoveClassAsParameterType() throws Exception {
        runTest("compiler/testData/klibABI/removeClassAsParameterType/");
    }

    @Test
    @TestMetadata("removeClassAsReturnType")
    public void testRemoveClassAsReturnType() throws Exception {
        runTest("compiler/testData/klibABI/removeClassAsReturnType/");
    }

    @Test
    @TestMetadata("removeClassAsSuperType")
    public void testRemoveClassAsSuperType() throws Exception {
        runTest("compiler/testData/klibABI/removeClassAsSuperType/");
    }

    @Test
    @TestMetadata("removeClassAsSuperTypeArgument")
    public void testRemoveClassAsSuperTypeArgument() throws Exception {
        runTest("compiler/testData/klibABI/removeClassAsSuperTypeArgument/");
    }

    @Test
    @TestMetadata("removeClassAsTypeArgument")
    public void testRemoveClassAsTypeArgument() throws Exception {
        runTest("compiler/testData/klibABI/removeClassAsTypeArgument/");
    }

    @Test
    @TestMetadata("removeClassAsVariableType")
    public void testRemoveClassAsVariableType() throws Exception {
        runTest("compiler/testData/klibABI/removeClassAsVariableType/");
    }

    @Test
    @TestMetadata("removeEnumEntry")
    public void testRemoveEnumEntry() throws Exception {
        runTest("compiler/testData/klibABI/removeEnumEntry/");
    }

    @Test
    @TestMetadata("removeFunction")
    public void testRemoveFunction() throws Exception {
        runTest("compiler/testData/klibABI/removeFunction/");
    }

    @Test
    @TestMetadata("removeInlinedClass")
    public void testRemoveInlinedClass() throws Exception {
        runTest("compiler/testData/klibABI/removeInlinedClass/");
    }

    @Test
    @TestMetadata("removeInlinedFunction")
    public void testRemoveInlinedFunction() throws Exception {
        runTest("compiler/testData/klibABI/removeInlinedFunction/");
    }

    @Test
    @TestMetadata("removeInlinedProperty")
    public void testRemoveInlinedProperty() throws Exception {
        runTest("compiler/testData/klibABI/removeInlinedProperty/");
    }

    @Test
    @TestMetadata("removeOpenFunction")
    public void testRemoveOpenFunction() throws Exception {
        runTest("compiler/testData/klibABI/removeOpenFunction/");
    }

    @Test
    @TestMetadata("removeOpenProperty")
    public void testRemoveOpenProperty() throws Exception {
        runTest("compiler/testData/klibABI/removeOpenProperty/");
    }

    @Test
    @TestMetadata("removeProperty")
    public void testRemoveProperty() throws Exception {
        runTest("compiler/testData/klibABI/removeProperty/");
    }

    @Test
    @TestMetadata("removeSealedSubclass")
    public void testRemoveSealedSubclass() throws Exception {
        runTest("compiler/testData/klibABI/removeSealedSubclass/");
    }

    @Test
    @TestMetadata("replaceCallableReturnType")
    public void testReplaceCallableReturnType() throws Exception {
        runTest("compiler/testData/klibABI/replaceCallableReturnType/");
    }

    @Test
    @TestMetadata("typeAliasRHSTypeChange")
    public void testTypeAliasRHSTypeChange() throws Exception {
        runTest("compiler/testData/klibABI/typeAliasRHSTypeChange/");
    }
}
