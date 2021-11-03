package com.digirati.elucidate.infrastructure.builder.function

import com.digirati.elucidate.common.model.annotation.AbstractAnnotationPage

fun interface AnnotationPageConverter<P : AbstractAnnotationPage> {
    fun convertToAnnotationPage(jsonMap: MutableMap<String, Any>): P
}