package com.digirati.elucidate.converter.node

import com.digirati.elucidate.common.infrastructure.constants.JSONLDConstants
import com.fasterxml.jackson.databind.JsonNode

class ItemsNodeConverterImpl : JSONNodeConverter {
    override fun convertJsonNode(inputNode: JsonNode): JsonNode {
        return inputNode[0][JSONLDConstants.ATTRIBUTE_LIST]
    }
}