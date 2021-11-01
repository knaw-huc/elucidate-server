package com.digirati.elucidate.converter

import com.digirati.elucidate.converter.exception.AnnotationConversionException
import com.digirati.elucidate.converter.node.JSONNodeConverter
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.*
import org.apache.commons.lang3.StringUtils

abstract class AbstractConverter protected constructor(
    private val fieldMappings: Map<String, String>,
    private val typeMappings: Map<String, String>,
    private val nodeConverterMappings: Map<String, JSONNodeConverter>
) {

    fun convert(sourceNode: JsonNode?): JsonNode {
        if (sourceNode == null) {
            throw AnnotationConversionException("Provided Source Node is NULL")
        }
        if (!sourceNode.isObjectNode()) {
            throw AnnotationConversionException(
                "Expected JSON Node Type [${JsonNodeType.OBJECT}] for provided Source Node [$sourceNode], got [${sourceNode.nodeType}]"
            )
        }
        return processNode(sourceNode)
    }

    private fun processNode(node: JsonNode): JsonNode =
        when {
            node.isObjectNode() -> processObjectNode(node)

            node.isArrayNode() -> processArrayNode(node)

            node.isTextNode() -> processTextNode(node)

            node.isNumericNode() -> processNumericNode(node)

            else -> throw AnnotationConversionException(
                "Unexpected JSON Node Type [${node.nodeType}] for JSON Node [$node]"
            )

        }

    private fun processObjectNode(objectNode: JsonNode): ObjectNode {
        val oaObjectNode = JsonNodeFactory.instance.objectNode()
        val objectNodeIterator = objectNode.fields()
        while (objectNodeIterator.hasNext()) {
            val (fieldName, node) = objectNodeIterator.next()
            val translatedFieldName = translateFieldName(fieldName)
            if (StringUtils.isNotBlank(translatedFieldName)) {
                val convertedNode = processNode(convertNode(fieldName, node))
                oaObjectNode.set<JsonNode>(translatedFieldName, convertedNode)
            }
        }
        return oaObjectNode
    }

    private fun processArrayNode(arrayNode: JsonNode): ArrayNode {
        val convertedArrayNode = JsonNodeFactory.instance.arrayNode()
        for (node in arrayNode) {
            convertedArrayNode.add(processNode(node))
        }
        return convertedArrayNode
    }

    private fun processTextNode(textNode: JsonNode): TextNode {
        val value = translateType(textNode.asText())
        return JsonNodeFactory.instance.textNode(value)
    }

    private fun processNumericNode(numericNode: JsonNode): NumericNode {
        val value = numericNode.asInt()
        return JsonNodeFactory.instance.numberNode(value)
    }

    private fun JsonNode.isObjectNode(): Boolean =
        nodeType == JsonNodeType.OBJECT

    private fun JsonNode.isArrayNode(): Boolean =
        nodeType == JsonNodeType.ARRAY

    private fun JsonNode.isTextNode(): Boolean =
        nodeType == JsonNodeType.STRING

    private fun JsonNode.isNumericNode(): Boolean =
        nodeType == JsonNodeType.NUMBER

    private fun translateFieldName(fieldName: String): String =
        fieldMappings.getOrDefault(fieldName, fieldName)

    private fun translateType(type: String): String =
        typeMappings.getOrDefault(type, type)


    private fun convertNode(fieldName: String, inputNode: JsonNode): JsonNode =
        if (nodeConverterMappings.containsKey(fieldName)) {
            nodeConverterMappings[fieldName]!!.convertJsonNode(inputNode)
        } else inputNode
}