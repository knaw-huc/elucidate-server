package com.digirati.elucidate.test.service.impl

import com.digirati.elucidate.common.model.annotation.AbstractAnnotation
import com.digirati.elucidate.common.model.annotation.AbstractAnnotationCollection
import com.digirati.elucidate.common.model.annotation.w3c.W3CAnnotation
import com.digirati.elucidate.common.service.IRIBuilderService
import com.digirati.elucidate.common.test.AbstractTest
import com.digirati.elucidate.model.ServiceResponse
import com.digirati.elucidate.repository.AnnotationStoreRepository
import com.digirati.elucidate.service.query.AbstractAnnotationService
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import java.util.*

abstract class AbstractAnnotationServiceImplTest<A : AbstractAnnotation?, C : AbstractAnnotationCollection?> :
    AbstractTest() {
    private var annotationStoreRepository: AnnotationStoreRepository? = null
    private var annotationService: AbstractAnnotationService<A>? = null

    protected abstract fun createAnnotationService(
        iriBuilderService: IRIBuilderService?,
        annotationStoreRepository: AnnotationStoreRepository?
    ): AbstractAnnotationService<A>

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
        Mockito.`when`(
            annotationStoreRepository!!.getAnnotationByCollectionIdAndAnnotationId(
                collectionId,
                annotationId
            )
        ).thenReturn(w3cAnnotation)
        val serviceResponse = annotationService!!.getAnnotation(collectionId, annotationId)
        MatcherAssert.assertThat(serviceResponse, Matchers.`is`(Matchers.not(Matchers.nullValue())))
        MatcherAssert.assertThat(serviceResponse.status, Matchers.`is`(Matchers.equalTo(ServiceResponse.Status.OK)))
        validateConversionToAnnotation(w3cAnnotation, serviceResponse.obj)
    }

    @Test
    fun testGetAnnotationNotFound() {
        val w3cAnnotation = generateRandomW3CAnnotation()
        val collectionId = w3cAnnotation.collectionId
        val annotationId = w3cAnnotation.annotationId
        Mockito.`when`(
            annotationStoreRepository!!.getAnnotationByCollectionIdAndAnnotationId(
                collectionId,
                annotationId
            )
        ).thenReturn(null)
        val serviceResponse = annotationService!!.getAnnotation(collectionId, annotationId)
        MatcherAssert.assertThat(serviceResponse, Matchers.`is`(Matchers.not(Matchers.nullValue())))
        MatcherAssert.assertThat(
            serviceResponse.status,
            Matchers.`is`(Matchers.equalTo(ServiceResponse.Status.NOT_FOUND))
        )
        MatcherAssert.assertThat(serviceResponse.obj, Matchers.`is`(Matchers.nullValue()))
    }

    @Test
    fun testGetAnnotationDeleted() {
        val w3cAnnotation = generateRandomW3CAnnotation()
        val collectionId = w3cAnnotation.collectionId
        val annotationId = w3cAnnotation.annotationId
        Mockito.`when`(annotationStoreRepository!!.countDeletedAnnotations(collectionId, annotationId)).thenReturn(1)
        Mockito.`when`(
            annotationStoreRepository!!.getAnnotationByCollectionIdAndAnnotationId(
                collectionId,
                annotationId
            )
        ).thenReturn(null)
        val serviceResponse = annotationService!!.getAnnotation(collectionId, annotationId)
        MatcherAssert.assertThat(serviceResponse, Matchers.`is`(Matchers.not(Matchers.nullValue())))
        MatcherAssert.assertThat(
            serviceResponse.status,
            Matchers.`is`(Matchers.equalTo(ServiceResponse.Status.DELETED))
        )
        MatcherAssert.assertThat(serviceResponse.obj, Matchers.`is`(Matchers.nullValue()))
    }

    @Test
    fun testGetAnnotations() {
        val collectionId = generateRandomId()
        val w3cAnnotations = generateRandomW3CAnnotations(10)
        Mockito.`when`(annotationStoreRepository!!.getAnnotationsByCollectionId(collectionId))
            .thenReturn(w3cAnnotations)
        val serviceResponse = annotationService!!.getAnnotations(collectionId)
        MatcherAssert.assertThat(serviceResponse, Matchers.`is`(Matchers.not(Matchers.nullValue())))
        MatcherAssert.assertThat(serviceResponse.status, Matchers.`is`(Matchers.equalTo(ServiceResponse.Status.OK)))
        val targetAnnotations = serviceResponse.obj
        MatcherAssert.assertThat(targetAnnotations, Matchers.`is`(Matchers.not(Matchers.nullValue())))
        MatcherAssert.assertThat(targetAnnotations.size, Matchers.`is`(Matchers.equalTo(10)))
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
        w3cAnnotation.jsonMap = annotation!!.jsonMap
        Mockito.`when`(
            annotationStoreRepository!!.createAnnotation(
                ArgumentMatchers.eq(collectionId),
                ArgumentMatchers.eq("test-annotation-id"),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any()
            )
        ).thenReturn(w3cAnnotation)
        val serviceResponse = annotationService!!.createAnnotation(collectionId, null, annotation)
        MatcherAssert.assertThat(serviceResponse, Matchers.`is`(Matchers.not(Matchers.nullValue())))
        MatcherAssert.assertThat(serviceResponse.status, Matchers.`is`(Matchers.equalTo(ServiceResponse.Status.OK)))
        val targetAnnotation = serviceResponse.obj
        MatcherAssert.assertThat(targetAnnotation, Matchers.`is`(Matchers.not(Matchers.nullValue())))
        MatcherAssert.assertThat(targetAnnotation!!.collectionId, Matchers.`is`(Matchers.equalTo(collectionId)))
        MatcherAssert.assertThat(targetAnnotation.annotationId, Matchers.`is`(Matchers.not(Matchers.nullValue())))
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
        val jsonMap = annotation!!.jsonMap
        jsonMap["custom_field"] = "custom_value"
        w3cAnnotation.jsonMap = jsonMap
        Mockito.`when`(
            annotationStoreRepository!!.createAnnotation(
                ArgumentMatchers.eq(collectionId),
                ArgumentMatchers.eq("test-annotation-id"),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any()
            )
        ).thenReturn(w3cAnnotation)
        val serviceResponse = annotationService!!.createAnnotation(collectionId, null, annotation)
        MatcherAssert.assertThat(serviceResponse, Matchers.`is`(Matchers.not(Matchers.nullValue())))
        MatcherAssert.assertThat(serviceResponse.status, Matchers.`is`(Matchers.equalTo(ServiceResponse.Status.OK)))
        val targetAnnotation = serviceResponse.obj
        MatcherAssert.assertThat(targetAnnotation, Matchers.`is`(Matchers.not(Matchers.nullValue())))
        MatcherAssert.assertThat(targetAnnotation!!.collectionId, Matchers.`is`(Matchers.equalTo(collectionId)))
        MatcherAssert.assertThat(targetAnnotation.annotationId, Matchers.`is`(Matchers.not(Matchers.nullValue())))
        MatcherAssert.assertThat(targetAnnotation.jsonMap, Matchers.hasKey("custom_field"))
        MatcherAssert.assertThat(targetAnnotation.jsonMap["custom_field"], Matchers.`is`("custom_value"))
    }

    //    public void testUpdateAnnotation() {
    //        // TODO
    //    }
    //
    //    public void testDeleteAnnotation() {
    //        // TODO
    //    }
}