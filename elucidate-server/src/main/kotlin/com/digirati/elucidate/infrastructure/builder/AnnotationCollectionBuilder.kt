package com.digirati.elucidate.infrastructure.builder

import com.digirati.elucidate.common.infrastructure.constants.ActivityStreamConstants
import com.digirati.elucidate.common.infrastructure.constants.JSONLDConstants
import com.digirati.elucidate.common.infrastructure.constants.XMLSchemaConstants
import com.digirati.elucidate.common.infrastructure.util.PaginationUtils.calculateLastPage
import com.digirati.elucidate.common.model.annotation.AbstractAnnotation
import com.digirati.elucidate.common.model.annotation.AbstractAnnotationCollection
import com.digirati.elucidate.common.model.annotation.AbstractAnnotationPage
import com.digirati.elucidate.common.model.annotation.w3c.W3CAnnotationCollection
import com.digirati.elucidate.infrastructure.builder.function.AnnotationCollectionConverter
import com.digirati.elucidate.infrastructure.builder.function.AnnotationCollectionIRIBuilder
import com.digirati.elucidate.infrastructure.builder.function.AnnotationPageIRIBuilder
import com.digirati.elucidate.infrastructure.builder.function.FirstAnnotationPageBuilder
import com.digirati.elucidate.model.ServiceResponse
import com.digirati.elucidate.model.enumeration.ClientPreference

class AnnotationCollectionBuilder<A : AbstractAnnotation, P : AbstractAnnotationPage, C : AbstractAnnotationCollection>(
    private val annotationCollectionConverter: AnnotationCollectionConverter<C>,
    private val annotationCollectionIriBuilder: AnnotationCollectionIRIBuilder,
    private val annotationPageIriBuilder: AnnotationPageIRIBuilder,
    private val firstAnnotationPageBuilder: FirstAnnotationPageBuilder<P>
) {

    fun buildAnnotationCollection(
        w3cAnnotationCollection: W3CAnnotationCollection,
        annotations: List<A>,
        pageSize: Int,
        clientPref: ClientPreference
    ): ServiceResponse<C> {
        val totalAnnotations = annotations.size
        val lastPage = calculateLastPage(totalAnnotations, pageSize)
        val annotationCollection = annotationCollectionConverter.convertToAnnotationCollection()
        val jsonMap: MutableMap<String, Any> = annotationCollection.jsonMap!!
        jsonMap[JSONLDConstants.ATTRIBUTE_TYPE] = listOf(ActivityStreamConstants.URI_ORDERED_COLLECTION)
        jsonMap[JSONLDConstants.ATTRIBUTE_ID] = annotationCollectionIriBuilder.buildAnnotationCollectionIri()

        val firstObject: Any
        val lastObject: Any
        if (clientPref == ClientPreference.MINIMAL_CONTAINER) {
            firstObject = listOf(
                mapOf(
                    JSONLDConstants.ATTRIBUTE_ID to annotationPageIriBuilder.buildAnnotationPageIri(0, false)
                )
            )
            lastObject = listOf(
                mapOf(
                    JSONLDConstants.ATTRIBUTE_ID to annotationPageIriBuilder.buildAnnotationPageIri(0, false)
                )
            )
        } else {
            val serviceResponse = firstAnnotationPageBuilder.buildFirstAnnotationPage()
            val status = serviceResponse.status
            if (status != ServiceResponse.Status.OK) {
                return ServiceResponse(status, null)
            }
            firstObject = serviceResponse.obj!!.jsonMap!!
            lastObject = listOf(
                mapOf(JSONLDConstants.ATTRIBUTE_ID to annotationPageIriBuilder.buildAnnotationPageIri(lastPage, true))
            )
        }
        jsonMap[ActivityStreamConstants.URI_FIRST] = firstObject
        jsonMap[ActivityStreamConstants.URI_LAST] = lastObject
        jsonMap[ActivityStreamConstants.URI_TOTAL_ITEMS] = listOf(
            mapOf(
                JSONLDConstants.ATTRIBUTE_TYPE to XMLSchemaConstants.URI_NON_NEGATIVE_INTEGER,
                JSONLDConstants.ATTRIBUTE_VALUE to totalAnnotations
            )
        )
        return ServiceResponse(ServiceResponse.Status.OK, annotationCollection)
    }
}