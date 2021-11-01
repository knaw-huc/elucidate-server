package com.digirati.elucidate.converter.node;

import com.fasterxml.jackson.databind.JsonNode;

import com.digirati.elucidate.common.infrastructure.constants.JSONLDConstants;

public class ItemsNodeConverterImpl implements JSONNodeConverter {

    @Override
    public JsonNode convertJsonNode(JsonNode inputNode) {
        return inputNode.get(0).get(JSONLDConstants.ATTRIBUTE_LIST);
    }
}
