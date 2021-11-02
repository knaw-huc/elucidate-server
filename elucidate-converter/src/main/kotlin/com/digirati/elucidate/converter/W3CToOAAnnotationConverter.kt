package com.digirati.elucidate.converter

import com.digirati.elucidate.common.infrastructure.constants.ActivityStreamConstants
import com.digirati.elucidate.common.infrastructure.constants.DCTermsConstants
import com.digirati.elucidate.common.infrastructure.constants.OAConstants
import com.digirati.elucidate.common.infrastructure.constants.XMLSchemaConstants
import com.digirati.elucidate.converter.node.ItemsNodeConverterImpl
import com.digirati.elucidate.converter.node.JSONNodeConverter

private val FIELD_MAPPINGS: Map<String, String> = mapOf(
    DCTermsConstants.URI_CREATOR to OAConstants.URI_ANNOTATED_BY,
    DCTermsConstants.URI_CREATED to OAConstants.URI_ANNOTATED_AT,
    ActivityStreamConstants.URI_GENERATOR to OAConstants.URI_SERIALIZED_BY,
    DCTermsConstants.URI_ISSUED to OAConstants.URI_SERIALIZED_AT,
    ActivityStreamConstants.URI_ITEMS to OAConstants.URI_ITEM
)

private val TYPE_MAPPINGS: Map<String, String> = mapOf(
    XMLSchemaConstants.URI_DATE_TIME to XMLSchemaConstants.URI_DATE_TIME_STAMP
)

private val NODE_CONVERTER_MAPPINGS: Map<String, JSONNodeConverter> = mapOf(
    ActivityStreamConstants.URI_ITEMS to ItemsNodeConverterImpl()
)

object W3CToOAAnnotationConverter : AbstractConverter(
    fieldMappings = FIELD_MAPPINGS,
    typeMappings = TYPE_MAPPINGS,
    nodeConverterMappings = NODE_CONVERTER_MAPPINGS
)