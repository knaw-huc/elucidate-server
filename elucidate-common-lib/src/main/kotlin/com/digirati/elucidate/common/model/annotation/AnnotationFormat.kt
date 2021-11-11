package com.digirati.elucidate.common.model.annotation

enum class AnnotationFormat {
    OA, W3C
}

fun AnnotationFormat.prefix() = name.lowercase()
