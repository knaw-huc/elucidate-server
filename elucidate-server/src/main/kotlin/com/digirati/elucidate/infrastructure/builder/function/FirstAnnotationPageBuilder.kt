package com.digirati.elucidate.infrastructure.builder.function

import com.digirati.elucidate.common.model.annotation.AbstractAnnotationPage
import com.digirati.elucidate.model.ServiceResponse

fun interface FirstAnnotationPageBuilder<P : AbstractAnnotationPage?> {
    fun buildFirstAnnotationPage(): ServiceResponse<P>
}