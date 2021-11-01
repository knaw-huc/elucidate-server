package com.digirati.elucidate.model.annotation.selector.dataposition

import com.digirati.elucidate.model.annotation.selector.AbstractAnnotationSelector
import java.io.Serializable

class AnnotationDataPositionSelector : AbstractAnnotationSelector(), Serializable {
    var start: Int? = null
    var end: Int? = null
}