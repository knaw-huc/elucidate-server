package com.digirati.elucidate.test.model

import com.digirati.elucidate.common.test.AbstractTest
import com.digirati.elucidate.model.ValidationError
import com.github.jsonldjava.utils.JsonUtils
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.Test
import java.io.IOException

class ValidationErrorTest : AbstractTest() {
    @Test
    @Throws(IOException::class)
    fun testValidationError() {
        val jsonMap = generateRandomJsonMap()
        val jsonStr = JsonUtils.toString(jsonMap)
        val validationError = ValidationError()
        validationError.jsonError = jsonStr
        MatcherAssert.assertThat(jsonStr, Matchers.`is`(Matchers.equalTo(validationError.jsonError)))
    }
}