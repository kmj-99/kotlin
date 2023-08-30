/*
 * Copyright 2010-2023 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.psi.stubs.elements

import com.intellij.psi.stubs.IndexSink
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import com.intellij.util.io.StringRef
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.psi.KtTypeAlias
import org.jetbrains.kotlin.psi.psiUtil.safeFqNameForLazyResolve
import org.jetbrains.kotlin.psi.stubs.KotlinClassStub
import org.jetbrains.kotlin.psi.stubs.KotlinFileStub
import org.jetbrains.kotlin.psi.stubs.KotlinTypeAliasStub
import org.jetbrains.kotlin.psi.stubs.StubUtils
import org.jetbrains.kotlin.psi.stubs.impl.KotlinTypeAliasStubImpl

class KtTypeAliasElementType(debugName: String) :
    KtStubElementType<KotlinTypeAliasStub, KtTypeAlias>(debugName, KtTypeAlias::class.java, KotlinTypeAliasStub::class.java) {

    override fun createStub(psi: KtTypeAlias, parentStub: StubElement<*>?): KotlinTypeAliasStub {
        val name = StringRef.fromString(psi.nameAsSafeName.asString())
        val fqName = StringRef.fromString(psi.safeFqNameForLazyResolve()?.asString())
        val classId = parentStub?.let { createNestedClassId(it, psi) }
        val isTopLevel = psi.isTopLevel()
        return KotlinTypeAliasStubImpl(parentStub, name, fqName, classId, isTopLevel)
    }

    private fun createNestedClassId(parentStub: StubElement<*>, typeAlias: KtTypeAlias): ClassId? {
        val typeAliasName = typeAlias.nameAsName ?: return null
        return when {
            parentStub.stubType == KtStubElementTypes.CLASS_BODY -> {
                val parentClass = parentStub.parentStub as? KotlinClassStub
                parentClass?.getClassId()?.createNestedClassId(typeAliasName)
            }
            parentStub is KotlinFileStub -> ClassId(parentStub.getPackageFqName(), typeAliasName)
            else -> null
        }
    }

    override fun serialize(stub: KotlinTypeAliasStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.name)
        dataStream.writeName(stub.getFqName()?.asString())
        StubUtils.serializeClassId(dataStream, stub.getClassId())
        dataStream.writeBoolean(stub.isTopLevel())
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): KotlinTypeAliasStub {
        val name = dataStream.readName()
        val fqName = dataStream.readName()
        val classId = StubUtils.deserializeClassId(dataStream)
        val isTopLevel = dataStream.readBoolean()
        return KotlinTypeAliasStubImpl(parentStub, name, fqName, classId, isTopLevel)
    }

    override fun indexStub(stub: KotlinTypeAliasStub, sink: IndexSink) {
        StubIndexService.getInstance().indexTypeAlias(stub, sink)
    }
}
