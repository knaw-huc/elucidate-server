package com.digirati.elucidate.converter.node

import com.fasterxml.jackson.databind.JsonNode

interface JSONNodeConverter {
    fun convertJsonNode(inputNode: JsonNode): JsonNode
}