package com.digirati.elucidate.model

class JSONLDProfile {
    enum class Format {
        COMPACTED, EXPANDED, FLATTENED
    }

    var contexts: Map<String, List<String>>? = null
    var formats: List<Format>? = null
}