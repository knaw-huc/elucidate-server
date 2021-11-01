package com.digirati.elucidate.model.annotation.selector.xpath

import com.digirati.elucidate.model.annotation.selector.AbstractAnnotationSelector
import java.io.Serializable

class AnnotationXPathSelector : AbstractAnnotationSelector(), Serializable {
    var value: String? = null
}