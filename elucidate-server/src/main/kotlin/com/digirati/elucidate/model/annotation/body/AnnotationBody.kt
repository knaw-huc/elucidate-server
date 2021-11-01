package com.digirati.elucidate.model.annotation.body

import com.digirati.elucidate.common.model.annotation.AbstractAnnotation
import java.io.Serializable

class AnnotationBody : AbstractAnnotation(), Serializable {
    var bodyIri: String? = null
    var sourceIri: String? = null
}