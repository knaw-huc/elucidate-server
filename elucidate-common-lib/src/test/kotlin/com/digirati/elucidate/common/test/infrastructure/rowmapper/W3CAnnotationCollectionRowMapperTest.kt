package com.digirati.elucidate.common.test.infrastructure.rowmapper

import com.digirati.elucidate.common.infrastructure.database.rowmapper.W3CAnnotationCollectionRowMapper
import com.digirati.elucidate.common.test.AbstractTest
import com.github.jsonldjava.utils.JsonUtils
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.io.IOException
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Timestamp

@RunWith(MockitoJUnitRunner::class)
class W3CAnnotationCollectionRowMapperTest : AbstractTest() {
    @Test
    @Throws(SQLException::class, IOException::class)
    fun testRowMapper() {

        // Build up our fake data
        val cacheKey = generateRandomCacheKey()
        val collectionId = generateRandomId()
        val createdDateTime = generateRandomDate()
        val deleted = generateRandomBoolean()
        val jsonMap = generateRandomJsonMap()
        val jsonStr = JsonUtils.toString(jsonMap)
        val modifiedDateTime = generateRandomDate()

        // Stub out the result set
        val resultSet = Mockito.mock(ResultSet::class.java)
        Mockito.`when`(resultSet.getString("cachekey")).thenReturn(cacheKey)
        Mockito.`when`(resultSet.getString("collectionid")).thenReturn(collectionId)
        Mockito.`when`(resultSet.getTimestamp("createddatetime")).thenReturn(Timestamp(createdDateTime.time))
        Mockito.`when`(resultSet.getBoolean("deleted")).thenReturn(deleted)
        Mockito.`when`(resultSet.getString("json")).thenReturn(jsonStr)
        Mockito.`when`(resultSet.getTimestamp("modifieddatetime")).thenReturn(Timestamp(modifiedDateTime.time))

        // Execute the row mapper
        val w3cAnnotationCollection = W3CAnnotationCollectionRowMapper().mapRow(resultSet, 0)

        // Verify the W3CAnnotation fields
        MatcherAssert.assertThat(cacheKey, Matchers.`is`(Matchers.equalTo(w3cAnnotationCollection.cacheKey)))
        MatcherAssert.assertThat(collectionId, Matchers.`is`(Matchers.equalTo(w3cAnnotationCollection.collectionId)))
        MatcherAssert.assertThat(
                createdDateTime,
                Matchers.`is`(Matchers.equalTo(w3cAnnotationCollection.createdDateTime))
        )
        MatcherAssert.assertThat(deleted, Matchers.`is`(Matchers.equalTo(w3cAnnotationCollection.isDeleted)))
        MatcherAssert.assertThat(jsonMap, Matchers.`is`(Matchers.equalTo(w3cAnnotationCollection.jsonMap)))
        MatcherAssert.assertThat(
                modifiedDateTime,
                Matchers.`is`(Matchers.equalTo(w3cAnnotationCollection.modifiedDateTime))
        )
    }
}