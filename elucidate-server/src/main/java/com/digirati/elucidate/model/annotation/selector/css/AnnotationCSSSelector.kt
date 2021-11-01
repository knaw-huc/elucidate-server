package com.digirati.elucidate.model.annotation.selector.css

import com.digirati.elucidate.model.annotation.selector.AbstractAnnotationSelector
import java.io.Serializable

class AnnotationCSSSelector : AbstractAnnotationSelector(), Serializable {
    var value: String? = null
}