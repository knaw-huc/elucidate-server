package com.digirati.elucidate.model.annotation.history

import com.digirati.elucidate.common.model.annotation.AbstractObject
import java.io.Serializable

abstract class AbstractAnnotationHistory : AbstractObject(), Serializable {
    var annotationId: String? = null
    var collectionId: String? = null
    var version: Int? = null
}