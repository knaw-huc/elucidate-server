package com.digirati.elucidate.test.schema

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.github.fge.jackson.JsonLoader
import com.github.fge.jsonschema.core.exceptions.ProcessingException
import com.github.fge.jsonschema.main.JsonSchemaFactory
import com.github.jsonldjava.core.JsonLdError
import com.github.jsonldjava.core.JsonLdProcessor
import com.github.jsonldjava.utils.JsonUtils
import org.apache.commons.io.IOUtils
import org.junit.Assert
import java.io.IOException
import java.nio.charset.StandardCharsets

abstract class AbstractSchemaValidatorTest {
    protected abstract val schemaFileName: String

    @Throws(IOException::class, ProcessingException::class, JsonLdError::class)
    protected fun validateJson(jsonFileName: String) {
        val schema = schema
        Assert.assertNotNull(schema)
        var jsonStr: String? = getJson(jsonFileName)
        Assert.assertNotNull(jsonStr)
        val jsonObj = JsonUtils.fromString(jsonStr)
        val expandedJson = JsonLdProcessor.expand(jsonObj)
        jsonStr = JsonUtils.toString(expandedJson)
        val json = JsonLoader.fromString(jsonStr)
        val jsonValidator = JsonSchemaFactory.byDefault().validator
        val processingReport = jsonValidator.validate(schema, json)
        Assert.assertNotNull(processingReport)
        if (!processingReport.isSuccess) {
            val jsonArray = JsonNodeFactory.instance.arrayNode()
            Assert.assertNotNull(jsonArray)
            for (processingMessage in processingReport) {
                jsonArray.add(processingMessage.asJson())
            }
            val errorJson = JsonUtils.toPrettyString(jsonArray)
            Assert.assertNotNull(errorJson)
            Assert.fail(errorJson)
        }
    }

    @Throws(IOException::class)
    private fun getJson(fileName: String): String {
        val inputStream = AbstractSchemaValidatorTest::class.java.getResourceAsStream(fileName)
        return IOUtils.toString(inputStream, StandardCharsets.UTF_8)
    }

    @get:Throws(IOException::class)
    private val schema: JsonNode
        get() {
            val jsonStr = getJson(schemaFileName)
            return JsonLoader.fromString(jsonStr)
        }
}