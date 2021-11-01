package com.digirati.elucidate.model.annotation.selector.textquote

import com.digirati.elucidate.model.annotation.selector.AbstractAnnotationSelector
import java.io.Serializable

class AnnotationTextQuoteSelector : AbstractAnnotationSelector(), Serializable {
    var exact: String? = null
    var prefix: String? = null
    var suffix: String? = null
}