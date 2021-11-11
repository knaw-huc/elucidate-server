package com.digirati.elucidate.infrastructure.builder

import com.digirati.elucidate.common.infrastructure.constants.ActivityStreamConstants.URI_ITEMS
import com.digirati.elucidate.common.infrastructure.constants.ActivityStreamConstants.URI_NEXT
import com.digirati.elucidate.common.infrastructure.constants.ActivityStreamConstants.URI_ORDERED_COLLECTION_PAGE
import com.digirati.elucidate.common.infrastructure.constants.ActivityStreamConstants.URI_PART_OF
import com.digirati.elucidate.common.infrastructure.constants.ActivityStreamConstants.URI_PREV
import com.digirati.elucidate.common.infrastructure.constants.ActivityStreamConstants.URI_START_INDEX
import com.digirati.elucidate.common.infrastructure.constants.JSONLDConstants.ATTRIBUTE_ID
import com.digirati.elucidate.common.infrastructure.constants.JSONLDConstants.ATTRIBUTE_LIST
import com.digirati.elucidate.common.infrastructure.constants.JSONLDConstants.ATTRIBUTE_TYPE
import com.digirati.elucidate.common.infrastructure.constants.JSONLDConstants.ATTRIBUTE_VALUE
import com.digirati.elucidate.common.infrastructure.constants.XMLSchemaConstants.URI_NON_NEGATIVE_INTEGER
import com.digirati.elucidate.common.infrastructure.util.PaginationUtils.calculateLastPage
import com.digirati.elucidate.common.model.annotation.AbstractAnnotation
import com.digirati.elucidate.common.model.annotation.AbstractAnnotationPage
import com.digirati.elucidate.infrastructure.builder.function.AnnotationCollectionIRIBuilder
import com.digirati.elucidate.infrastructure.builder.function.AnnotationPageConverter
import com.digirati.elucidate.infrastructure.builder.function.AnnotationPageIRIBuilder
import com.digirati.elucidate.model.ServiceResponse
import kotlin.math.max
import kotlin.math.min

class AnnotationPageBuilder<A : AbstractAnnotation, P : AbstractAnnotationPage>(
    private val annotationPageConverter: AnnotationPageConverter<P>,
    private val annotationCollectionIriBuilder: AnnotationCollectionIRIBuilder,
    private val annotationPageIriBuilder: AnnotationPageIRIBuilder
) {
    fun buildAnnotationPage(
        annotations: List<A>,
        page: Int,
        embeddedDescriptions: Boolean,
        pageSize: Int
    ): ServiceResponse<P> {
        var annotationSelection = annotations
        val lastPage = calculateLastPage(annotationSelection.size, pageSize)
        val from = min(annotationSelection.size, max(0, page * pageSize))
        val to = min(annotationSelection.size, (page + 1) * pageSize)
        annotationSelection = annotationSelection.subList(from, to)

        val jsonMap: MutableMap<String, Any> = mutableMapOf()
        jsonMap[ATTRIBUTE_TYPE] = listOf(URI_ORDERED_COLLECTION_PAGE)

        val partOfIri = annotationCollectionIriBuilder.buildAnnotationCollectionIri()
        jsonMap[URI_PART_OF] = listOf(mapOf(ATTRIBUTE_ID to partOfIri))

        jsonMap[URI_START_INDEX] = listOf(
            mapOf(
                ATTRIBUTE_TYPE to URI_NON_NEGATIVE_INTEGER,
                ATTRIBUTE_VALUE to from
            )
        )
        if (page > 0) {
            val prevIri = annotationPageIriBuilder.buildAnnotationPageIri(page - 1, embeddedDescriptions)
            jsonMap[URI_PREV] = listOf(mapOf(ATTRIBUTE_ID to prevIri))
        }
        if (page < lastPage) {
            val nextIri = annotationPageIriBuilder.buildAnnotationPageIri(page + 1, embeddedDescriptions)
            jsonMap[URI_NEXT] = listOf(mapOf(ATTRIBUTE_ID to nextIri))
        }
        if (embeddedDescriptions) {
            val annotationDescriptions = convertToDescriptions(annotationSelection)
            jsonMap[URI_ITEMS] = listOf(
                mapOf(
                    ATTRIBUTE_LIST to annotationDescriptions
                )
            )
        } else {
            val annotationIris = convertToIris(annotationSelection)
            jsonMap[URI_ITEMS] = listOf(mapOf(ATTRIBUTE_LIST to annotationIris))
        }
        val annotationPage = annotationPageConverter.convertToAnnotationPage(jsonMap)
        return ServiceResponse(ServiceResponse.Status.OK, annotationPage)
    }

    private fun convertToDescriptions(annotations: List<A>): List<Map<String, Any>> {
        val descriptions: MutableList<Map<String, Any>> = ArrayList()
        for (annotation in annotations) {
            descriptions.add(annotation.jsonMap!!)
        }
        return descriptions
    }

    private fun convertToIris(annotations: List<A>): List<Map<String, Any>> {
        return annotations.map { a -> mapOf(ATTRIBUTE_ID to a.jsonMap!![ATTRIBUTE_ID]!!) }
//        val iris: MutableList<Map<String, Any>> = ArrayList()
//        for (annotation in annotations) {
//            iris.add(mapOf(JSONLDConstants.ATTRIBUTE_ID to annotation.jsonMap!![JSONLDConstants.ATTRIBUTE_ID]!!))
//        }
//        return iris
    }
}