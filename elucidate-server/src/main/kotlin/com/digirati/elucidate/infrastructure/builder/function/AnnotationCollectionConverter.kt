package com.digirati.elucidate.infrastructure.builder.function

import com.digirati.elucidate.common.model.annotation.AbstractAnnotationCollection

fun interface AnnotationCollectionConverter<C : AbstractAnnotationCollection> {
    fun convertToAnnotationCollection(): C
}