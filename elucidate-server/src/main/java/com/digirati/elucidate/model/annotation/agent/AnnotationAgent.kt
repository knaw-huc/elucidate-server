package com.digirati.elucidate.model.annotation.agent

import com.digirati.elucidate.common.model.annotation.AbstractObject
import java.io.Serializable

class AnnotationAgent : AbstractObject(), Serializable {
    var agentIri: String? = null
    var types: List<String>? = null
    var typesJsonList: List<String>? = null
    var names: List<String>? = null
    var nameJsonMaps: List<Map<String, Any>>? = null
    var nickname: String? = null
    var emails: List<String>? = null
    var emailJsonMaps: List<Map<String, Any>>? = null
    var emailSha1s: List<String>? = null
    var emailSha1JsonMaps: List<Map<String, Any>>? = null
    var homepages: List<String>? = null
    var homepageJsonMaps: List<Map<String, Any>>? = null
}