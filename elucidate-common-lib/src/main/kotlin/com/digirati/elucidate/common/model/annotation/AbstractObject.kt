package com.digirati.elucidate.common.model.annotation

import java.io.Serializable
import java.util.*

abstract class AbstractObject : Serializable {
    var pk: Int? = null
    var jsonMap: MutableMap<String, Any>? = null
    var isDeleted = false
    var createdDateTime: Date? = null
    var modifiedDateTime: Date? = null
}