package com.digirati.elucidate.model.annotation.selector.svg

import com.digirati.elucidate.model.annotation.selector.AbstractAnnotationSelector
import java.io.Serializable

class AnnotationSVGSelector : AbstractAnnotationSelector(), Serializable {
    var value: String? = null
}