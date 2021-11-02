package com.digirati.elucidate.infrastructure.builder.function

fun interface AnnotationPageIRIBuilder {
    fun buildAnnotationPageIri(page: Int, embeddedDescriptions: Boolean): String
}