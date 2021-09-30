package com.digirati.elucidate.converter;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.digirati.elucidate.converter.exception.AnnotationConversionException;
import com.digirati.elucidate.converter.node.JSONNodeConverter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.NumericNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.apache.commons.lang3.StringUtils;

public abstract class AbstractConverter {

    private final Map<String, String> fieldMappings;
    private final Map<String, String> typeMappings;
    private final Map<String, JSONNodeConverter> nodeConverterMappings;

    protected AbstractConverter(Map<String, String> fieldMappings, Map<String, String> typeMappings, Map<String, JSONNodeConverter> nodeConverterMappings) {
        this.fieldMappings = fieldMappings;
        this.typeMappings = typeMappings;
        this.nodeConverterMappings = nodeConverterMappings;
    }

    public JsonNode convert(JsonNode sourceNode) {

        if (sourceNode == null) {
            throw new AnnotationConversionException("Provided Source Node is NULL");
        }

        if (!isObjectNode(sourceNode)) {
            throw new AnnotationConversionException(String.format("Expected JSON Node Type [%s] for provided Source Node [%s], got [%s]", JsonNodeType.OBJECT, sourceNode, sourceNode.getNodeType()));
        }

        return processNode(sourceNode);
    }

    private JsonNode processNode(JsonNode node) {

        if (isObjectNode(node)) {
            return processObjectNode(node);
        } else if (isArrayNode(node)) {
            return processArrayNode(node);
        } else if (isTextNode(node)) {
            return processTextNode(node);
        } else if (isNumericNode(node)) {
            return processNumericNode(node);
        } else {
            throw new AnnotationConversionException(String.format("Unexpected JSON Node Type [%s] for JSON Node [%s]", node.getNodeType(), node));
        }
    }

    private ObjectNode processObjectNode(JsonNode objectNode) {

        ObjectNode oaObjectNode = JsonNodeFactory.instance.objectNode();

        Iterator<Entry<String, JsonNode>> objectNodeIterator = objectNode.fields();
        while (objectNodeIterator.hasNext()) {

            Entry<String, JsonNode> nodeEntry = objectNodeIterator.next();
            String fieldName = nodeEntry.getKey();
            JsonNode node = nodeEntry.getValue();

            String translatedFieldName = translateFieldName(fieldName);

            if (StringUtils.isNotBlank(translatedFieldName)) {
                JsonNode convertedNode = processNode(convertNode(fieldName, node));
                oaObjectNode.set(translatedFieldName, convertedNode);
            }
        }

        return oaObjectNode;
    }

    private ArrayNode processArrayNode(JsonNode arrayNode) {

        ArrayNode convertedArrayNode = JsonNodeFactory.instance.arrayNode();

        for (JsonNode node : arrayNode) {
            convertedArrayNode.add(processNode(node));
        }

        return convertedArrayNode;
    }

    private TextNode processTextNode(JsonNode textNode) {

        String value = translateType(textNode.asText());
        return JsonNodeFactory.instance.textNode(value);
    }

    private NumericNode processNumericNode(JsonNode numericNode) {

        int value = numericNode.asInt();
        return JsonNodeFactory.instance.numberNode(value);
    }

    private boolean isObjectNode(JsonNode jsonNode) {
        return jsonNode.getNodeType().equals(JsonNodeType.OBJECT);
    }

    private boolean isArrayNode(JsonNode jsonNode) {
        return jsonNode.getNodeType().equals(JsonNodeType.ARRAY);
    }

    private boolean isTextNode(JsonNode jsonNode) {
        return jsonNode.getNodeType().equals(JsonNodeType.STRING);
    }

    private boolean isNumericNode(JsonNode jsonNode) {
        return jsonNode.getNodeType().equals(JsonNodeType.NUMBER);
    }

    private String translateFieldName(String fieldName) {
        if (fieldMappings.containsKey(fieldName)) {
            return fieldMappings.get(fieldName);
        }
        return fieldName;
    }

    private String translateType(String type) {
        if (typeMappings.containsKey(type)) {
            return typeMappings.get(type);
        }
        return type;
    }

    private JsonNode convertNode(String fieldName, JsonNode inputNode) {
        if (nodeConverterMappings.containsKey(fieldName)) {
            return nodeConverterMappings.get(fieldName).convertJsonNode(inputNode);
        }
        return inputNode;
    }
}
