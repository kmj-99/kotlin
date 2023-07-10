/*
 * Copyright 2010-2023 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.fir.lazy

import org.jetbrains.kotlin.descriptors.DescriptorVisibility
import org.jetbrains.kotlin.descriptors.Modality
import org.jetbrains.kotlin.descriptors.PropertyDescriptor
import org.jetbrains.kotlin.descriptors.isAnnotationClass
import org.jetbrains.kotlin.fir.backend.*
import org.jetbrains.kotlin.fir.declarations.*
import org.jetbrains.kotlin.fir.declarations.impl.FirDefaultPropertyGetter
import org.jetbrains.kotlin.fir.declarations.impl.FirDefaultPropertySetter
import org.jetbrains.kotlin.fir.declarations.utils.*
import org.jetbrains.kotlin.fir.expressions.FirConstExpression
import org.jetbrains.kotlin.fir.expressions.FirExpression
import org.jetbrains.kotlin.fir.propertyIfBackingField
import org.jetbrains.kotlin.fir.symbols.Fir2IrFieldSymbol
import org.jetbrains.kotlin.fir.symbols.Fir2IrPropertySymbol
import org.jetbrains.kotlin.fir.symbols.Fir2IrSimpleFunctionSymbol
import org.jetbrains.kotlin.fir.unwrapFakeOverrides
import org.jetbrains.kotlin.ir.ObsoleteDescriptorBasedAPI
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.declarations.lazy.lazyVar
import org.jetbrains.kotlin.ir.expressions.IrConstructorCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrExpressionBody
import org.jetbrains.kotlin.ir.symbols.IrPropertySymbol
import org.jetbrains.kotlin.ir.types.IrErrorType
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.util.isComposite
import org.jetbrains.kotlin.ir.util.isInterface
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.name.NameUtils
import org.jetbrains.kotlin.serialization.deserialization.descriptors.DeserializedContainerSource

class Fir2IrLazyField(
    private val components: Fir2IrComponents,
    override val startOffset: Int,
    override val endOffset: Int,
    override var origin: IrDeclarationOrigin,
    override val fir: FirField,
    val containingClass: FirRegularClass?,
    override val symbol: Fir2IrFieldSymbol,
) : IrField(), AbstractFir2IrLazyDeclaration<FirField>, Fir2IrComponents by components {
    init {
        symbol.bind(this)
        classifierStorage.preCacheTypeParameters(fir, symbol)
    }

    override var annotations: List<IrConstructorCall> by createLazyAnnotations()
    override lateinit var parent: IrDeclarationParent

    @ObsoleteDescriptorBasedAPI
    override val descriptor: PropertyDescriptor
        get() = symbol.descriptor

    override var isExternal: Boolean
        get() = fir.isExternal
        set(_) = mutationNotSupported()

    override var isFinal: Boolean
        get() = fir.isFinal
        set(_) = mutationNotSupported()

    override var isStatic: Boolean
        get() = fir.isStatic
        set(_) = mutationNotSupported()

    override var name: Name
        get() = fir.name
        set(_) = mutationNotSupported()

    override var visibility: DescriptorVisibility = components.visibilityConverter.convertToDescriptorVisibility(fir.visibility)
        set(_) = mutationNotSupported()

    override var type: IrType
        get() = with(typeConverter) { fir.returnTypeRef.toIrType() }
        set(_) = mutationNotSupported()

    override var initializer: IrExpressionBody? by lazyVar(lock) {
        when (val initializer = fir.unwrapFakeOverrides().initializer) {
            is FirConstExpression<*> -> factory.createExpressionBody(initializer.toIrConst(type))
            else -> null
        }
    }

    override var correspondingPropertySymbol: IrPropertySymbol?
        get() = null
        set(_) = mutationNotSupported()

    override var metadata: MetadataSource?
        get() = null
        set(_) = error("We should never need to store metadata of external declarations.")
}
