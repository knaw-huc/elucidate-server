package com.digirati.elucidate.test.service.impl

import com.digirati.elucidate.common.model.annotation.AbstractAnnotation
import com.digirati.elucidate.common.model.annotation.AbstractAnnotationCollection
import com.digirati.elucidate.common.model.annotation.w3c.W3CAnnotation
import com.digirati.elucidate.common.service.IRIBuilderService
import com.digirati.elucidate.common.test.AbstractTest
import com.digirati.elucidate.model.ServiceResponse
import com.digirati.elucidate.repository.AnnotationStoreRepository
import com.digirati.elucidate.service.query.AnnotationService
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import java.util.*

abstract class AbstractAnnotationServiceImplTest<A : AbstractAnnotation, C : AbstractAnnotationCollection> :
    AbstractTest() {
    private var annotationStoreRepository: AnnotationStoreRepository? = null
    private var annotationService: AnnotationService<A>? = null

    protected abstract fun createAnnotationService(
        iriBuilderService: IRIBuilderService?,
        annotationStoreRepository: AnnotationStoreRepository?
    ): AnnotationService<A>

    protected abstract fun validateConversionToAnnotation(w3cAnnotation: W3CAnnotation, targetAnnotation: A)
    protected abstract fun generateAnnotationWithJsonMapOnly(): A

    @Before
    fun before() {
        val iriBuilderService = Mockito.mock(IRIBuilderService::class.java)
        annotationStoreRepository = Mockito.mock(AnnotationStoreRepository::class.java)
        annotationService = createAnnotationService(iriBuilderService, annotationStoreRepository)
    }

    @Test
    fun testGetAnnotationOk() {
        val w3cAnnotation = generateRandomW3CAnnotation()
        val collectionId = w3cAnnotation.collectionId
        val annotationId = w3cAnnotation.annotationId
        `when`(
            annotationStoreRepository!!.getAnnotationByCollectionIdAndAnnotationId(
                collectionId,
                annotationId
            )
        ).thenReturn(w3cAnnotation)
        val serviceResponse = annotationService!!.getAnnotation(collectionId, annotationId)
        assertThat(serviceResponse, `is`(not(nullValue())))
        assertThat(serviceResponse!!.status, `is`(equalTo(ServiceResponse.Status.OK)))
        validateConversionToAnnotation(w3cAnnotation, serviceResponse!!.obj!!)
    }

    @Test
    fun testGetAnnotationNotFound() {
        val w3cAnnotation = generateRandomW3CAnnotation()
        val collectionId = w3cAnnotation.collectionId
        val annotationId = w3cAnnotation.annotationId
        `when`(
            annotationStoreRepository!!.getAnnotationByCollectionIdAndAnnotationId(
                collectionId,
                annotationId
            )
        ).thenReturn(null)
        val serviceResponse = annotationService!!.getAnnotation(collectionId, annotationId)
        assertThat(serviceResponse, `is`(not(nullValue())))
        assertThat(
            serviceResponse!!.status,
            `is`(equalTo(ServiceResponse.Status.NOT_FOUND))
        )
        assertThat(serviceResponse.obj, `is`(nullValue()))
    }

    @Test
    fun testGetAnnotationDeleted() {
        val w3cAnnotation = generateRandomW3CAnnotation()
        val collectionId = w3cAnnotation.collectionId
        val annotationId = w3cAnnotation.annotationId
        `when`(annotationStoreRepository!!.countDeletedAnnotations(collectionId, annotationId)).thenReturn(1)
        `when`(
            annotationStoreRepository!!.getAnnotationByCollectionIdAndAnnotationId(
                collectionId,
                annotationId
            )
        ).thenReturn(null)
        val serviceResponse = annotationService!!.getAnnotation(collectionId, annotationId)
        assertThat(serviceResponse, `is`(not(nullValue())))
        assertThat(
            serviceResponse!!.status,
            `is`(equalTo(ServiceResponse.Status.DELETED))
        )
        assertThat(serviceResponse.obj, `is`(nullValue()))
    }

    @Test
    fun testGetAnnotations() {
        val collectionId = generateRandomId()
        val w3cAnnotations = generateRandomW3CAnnotations(10)
        `when`(annotationStoreRepository!!.getAnnotationsByCollectionId(collectionId))
            .thenReturn(w3cAnnotations)
        val serviceResponse = annotationService!!.getAnnotations(collectionId)
        assertThat(serviceResponse, `is`(not(nullValue())))
        assertThat(serviceResponse.status, `is`(equalTo(ServiceResponse.Status.OK)))
        val targetAnnotations = serviceResponse.obj!!
        assertThat(targetAnnotations, `is`(not(nullValue())))
        assertThat(targetAnnotations.size, `is`(equalTo(10)))
        for (i in 0..9) {
            validateConversionToAnnotation(w3cAnnotations[i], targetAnnotations[i])
        }
    }

    @Test
    fun testCreateAnnotationWithoutAnnotationId() {
        val collectionId = generateRandomId()
        val annotation = generateAnnotationWithJsonMapOnly()
        val w3cAnnotation = W3CAnnotation()
        w3cAnnotation.annotationId = "test-annotation-id"
        w3cAnnotation.collectionId = collectionId
        w3cAnnotation.createdDateTime = Date()
        w3cAnnotation.isDeleted = false
        w3cAnnotation.jsonMap = annotation.jsonMap
        `when`(
            annotationStoreRepository!!.createAnnotation(
                ArgumentMatchers.eq(collectionId),
                ArgumentMatchers.eq("test-annotation-id"),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any()
            )
        ).thenReturn(w3cAnnotation)
        val serviceResponse = annotationService!!.createAnnotation(collectionId, null, annotation)
        assertThat(serviceResponse, `is`(not(nullValue())))
        assertThat(serviceResponse!!.status, `is`(equalTo(ServiceResponse.Status.OK)))
        val targetAnnotation = serviceResponse.obj
        assertThat(targetAnnotation, `is`(not(nullValue())))
        assertThat(targetAnnotation!!.collectionId, `is`(equalTo(collectionId)))
        assertThat(targetAnnotation.annotationId, `is`(not(nullValue())))
    }

    @Test
    fun testCreateAnnotationWithContextlessCustomFields() {
        val collectionId = generateRandomId()
        val annotation = generateAnnotationWithJsonMapOnly()
        val w3cAnnotation = W3CAnnotation()
        w3cAnnotation.annotationId = "test-annotation-id"
        w3cAnnotation.collectionId = collectionId
        w3cAnnotation.createdDateTime = Date()
        w3cAnnotation.isDeleted = false
        val jsonMap = annotation.jsonMap!!.toMutableMap()
        jsonMap["custom_field"] = "custom_value"
        w3cAnnotation.jsonMap = jsonMap
        `when`(
            annotationStoreRepository!!.createAnnotation(
                ArgumentMatchers.eq(collectionId),
                ArgumentMatchers.eq("test-annotation-id"),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any()
            )
        ).thenReturn(w3cAnnotation)
        val serviceResponse = annotationService!!.createAnnotation(collectionId, null, annotation)
        assertThat(serviceResponse, `is`(not(nullValue())))
        assertThat(serviceResponse!!.status, `is`(equalTo(ServiceResponse.Status.OK)))
        val targetAnnotation = serviceResponse.obj
        assertThat(targetAnnotation, `is`(not(nullValue())))
        assertThat(targetAnnotation!!.collectionId, `is`(equalTo(collectionId)))
        assertThat(targetAnnotation.annotationId, `is`(not(nullValue())))
        assertThat(targetAnnotation.jsonMap, hasKey("custom_field"))
        assertThat(targetAnnotation.jsonMap!!["custom_field"], `is`("custom_value"))
    }

    //    public void testUpdateAnnotation() {
    //        // TODO
    //    }
    //
    //    public void testDeleteAnnotation() {
    //        // TODO
    //    }
}