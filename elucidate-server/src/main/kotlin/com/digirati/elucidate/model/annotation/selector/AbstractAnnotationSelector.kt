package com.digirati.elucidate.model.annotation.selector

import com.digirati.elucidate.common.model.annotation.AbstractAnnotation
import java.io.Serializable

abstract class AbstractAnnotationSelector : AbstractAnnotation(), Serializable {
    var selectorIri: String? = null
    var bodyiri: String? = null
    var bodySourceIri: String? = null
    var targetIri: String? = null
    var targetSourceIri: String? = null
}