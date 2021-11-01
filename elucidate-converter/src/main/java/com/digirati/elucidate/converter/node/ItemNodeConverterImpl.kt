package com.digirati.elucidate.converter.node

import com.digirati.elucidate.common.infrastructure.constants.JSONLDConstants
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory

class ItemNodeConverterImpl : JSONNodeConverter {
    override fun convertJsonNode(inputNode: JsonNode): JsonNode {
        val objectNode = JsonNodeFactory.instance.objectNode()
        objectNode.set<JsonNode>(JSONLDConstants.ATTRIBUTE_LIST, inputNode)
        val arrayNode = JsonNodeFactory.instance.arrayNode()
        arrayNode.add(objectNode)
        return arrayNode
    }
}