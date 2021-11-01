package com.digirati.elucidate.common.model.annotation

import java.io.Serializable

abstract class AbstractAnnotationCollection : AbstractObject(), Serializable {
    var collectionId: String? = null
    var cacheKey: String? = null
}