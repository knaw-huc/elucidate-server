package com.digirati.elucidate.common.model.annotation

import java.io.Serializable

abstract class AbstractAnnotation : AbstractObject(), Serializable {
    var annotationId: String? = null
    var collectionId: String? = null
    var cacheKey: String? = null
    var ownerId = 0
    var groups: Set<Int>? = null
}