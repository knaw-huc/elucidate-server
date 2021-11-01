package com.digirati.elucidate.model.annotation.selector.fragment

import com.digirati.elucidate.model.annotation.selector.AbstractAnnotationSelector
import java.io.Serializable

class AnnotationFragmentSelector : AbstractAnnotationSelector(), Serializable {
    var conformsTo: String? = null
    var value: String? = null
    var x: Int? = null
    var y: Int? = null
    var w: Int? = null
    var h: Int? = null
    var start: Int? = null
    var end: Int? = null
}