package com.digirati.elucidate.common.test.infrastructure.rowmapper

import com.digirati.elucidate.common.infrastructure.database.rowmapper.W3CAnnotationRowMapper
import com.digirati.elucidate.common.test.AbstractTest
import com.github.jsonldjava.utils.JsonUtils
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import java.io.IOException
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Timestamp
import org.hamcrest.Matchers.`is` as _is
import org.mockito.Mockito.`when` as _when

@RunWith(MockitoJUnitRunner::class)
class W3CAnnotationRowMapperTest : AbstractTest() {
    @Test
    @Throws(SQLException::class, IOException::class)
    fun testRowMapper() {

        // Build up our fake data
        val annotationId = generateRandomId()
        val cacheKey = generateRandomCacheKey()
        val collectionId = generateRandomId()
        val createdDateTime = generateRandomDate()
        val deleted = generateRandomBoolean()
        val jsonMap = generateRandomJsonMap()
        val jsonStr = JsonUtils.toString(jsonMap)
        val modifiedDateTime = generateRandomDate()

        // Stub out the result set
        val resultSet = mock(ResultSet::class.java)
        _when(resultSet.getString("annotationid")).thenReturn(annotationId)
        _when(resultSet.getString("cachekey")).thenReturn(cacheKey)
        _when(resultSet.getString("collectionid")).thenReturn(collectionId)
        _when(resultSet.getTimestamp("createddatetime")).thenReturn(Timestamp(createdDateTime.time))
        _when(resultSet.getBoolean("deleted")).thenReturn(deleted)
        _when(resultSet.getString("json")).thenReturn(jsonStr)
        _when(resultSet.getTimestamp("modifieddatetime")).thenReturn(Timestamp(modifiedDateTime.time))

        // Execute the row mapper
        val w3cAnnotation = W3CAnnotationRowMapper().mapRow(resultSet, 0)

        // Verify the W3CAnnotation fields
        assertThat(annotationId, _is(equalTo(w3cAnnotation.annotationId)))
        assertThat(cacheKey, _is(equalTo(w3cAnnotation.cacheKey)))
        assertThat(collectionId, _is(equalTo(w3cAnnotation.collectionId)))
        assertThat(createdDateTime, _is(equalTo(w3cAnnotation.createdDateTime)))
        assertThat(deleted, _is(equalTo(w3cAnnotation.isDeleted)))
        assertThat(jsonMap, _is(equalTo(w3cAnnotation.jsonMap)))
        assertThat(modifiedDateTime, _is(equalTo(w3cAnnotation.modifiedDateTime)))
    }
}