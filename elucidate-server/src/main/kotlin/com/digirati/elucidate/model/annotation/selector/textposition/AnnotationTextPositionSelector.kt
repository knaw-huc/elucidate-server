package com.digirati.elucidate.model.annotation.selector.textposition

import com.digirati.elucidate.model.annotation.selector.AbstractAnnotationSelector
import java.io.Serializable

class AnnotationTextPositionSelector : AbstractAnnotationSelector(), Serializable {
    var start: Int? = null
    var end: Int? = null
}