package com.digirati.elucidate.model.annotation.temporal

import com.digirati.elucidate.common.model.annotation.AbstractObject
import java.io.Serializable
import java.util.*

class AnnotationTemporal : AbstractObject(), Serializable {
    var type: String? = null
    var value: Date? = null
}