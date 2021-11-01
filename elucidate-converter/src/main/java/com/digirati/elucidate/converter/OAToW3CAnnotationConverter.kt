package com.digirati.elucidate.converter

import com.digirati.elucidate.common.infrastructure.constants.ActivityStreamConstants
import com.digirati.elucidate.common.infrastructure.constants.DCTermsConstants
import com.digirati.elucidate.common.infrastructure.constants.OAConstants
import com.digirati.elucidate.common.infrastructure.constants.XMLSchemaConstants
import com.digirati.elucidate.converter.node.ItemNodeConverterImpl
import com.digirati.elucidate.converter.node.JSONNodeConverter

private val FIELD_MAPPINGS: Map<String, String> = mapOf(
    OAConstants.URI_ANNOTATED_BY to DCTermsConstants.URI_CREATOR,
    OAConstants.URI_ANNOTATED_AT to DCTermsConstants.URI_CREATED,
    OAConstants.URI_SERIALIZED_BY to ActivityStreamConstants.URI_GENERATOR,
    OAConstants.URI_SERIALIZED_AT to DCTermsConstants.URI_ISSUED,
    OAConstants.URI_ITEM to ActivityStreamConstants.URI_ITEMS
)

private val TYPE_MAPPINGS: Map<String, String> = mapOf(
    XMLSchemaConstants.URI_DATE_TIME_STAMP to XMLSchemaConstants.URI_DATE_TIME
)

private val NODE_CONVERTER_MAPPINGS: Map<String, JSONNodeConverter> = mapOf(
    OAConstants.URI_ITEM to ItemNodeConverterImpl()
)

object OAToW3CAnnotationConverter : AbstractConverter(
    fieldMappings = FIELD_MAPPINGS,
    typeMappings = TYPE_MAPPINGS,
    nodeConverterMappings = NODE_CONVERTER_MAPPINGS
)