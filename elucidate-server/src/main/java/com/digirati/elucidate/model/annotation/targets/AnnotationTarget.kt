package com.digirati.elucidate.model.annotation.targets

import com.digirati.elucidate.common.model.annotation.AbstractAnnotation
import java.io.Serializable

class AnnotationTarget : AbstractAnnotation(), Serializable {
    var targetIri: String? = null
    var sourceIri: String? = null
}