package com.digirati.elucidate.converter

import com.digirati.elucidate.converter.node.JSONNodeConverter

private val FIELD_MAPPINGS: Map<String, String> = HashMap()
private val TYPE_MAPPINGS: Map<String, String> = HashMap()
private val NODE_CONVERTER_MAPPINGS: Map<String, JSONNodeConverter> = HashMap()

object W3CToOAAnnotationCollectionConverter : AbstractConverter(
    fieldMappings = FIELD_MAPPINGS,
    typeMappings = TYPE_MAPPINGS,
    nodeConverterMappings = NODE_CONVERTER_MAPPINGS
)