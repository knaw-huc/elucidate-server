package com.digirati.elucidate.test.schema.oa

import com.digirati.elucidate.test.schema.AbstractSchemaValidatorTest
import org.junit.Test

class OAAnnotationValidatorTest : AbstractSchemaValidatorTest() {
    override val schemaFileName: String
        get() = "/schema/oa-annotation-schema.json"

    @Test
    @Throws(Exception::class)
    fun validateSimple() {
        validateJson("/example-oa-annotation/simple.jsonld")
    }

    @Test
    @Throws(Exception::class)
    fun validateFigureTwoOne() {
        validateJson("/example-oa-annotation/figure-2.1.jsonld")
    }

    @Test
    @Throws(Exception::class)
    fun validateFigureTwoOneOne() {
        validateJson("/example-oa-annotation/figure-2.1.1.jsonld")
    }

    @Test
    @Throws(Exception::class)
    fun validateFigureTwoOneTwo() {
        validateJson("/example-oa-annotation/figure-2.1.2.jsonld")
    }

    @Test
    @Throws(Exception::class)
    fun validateFigureTwoOneThreeOne() {
        validateJson("/example-oa-annotation/figure-2.1.3.1.jsonld")
    }

    @Test
    @Throws(Exception::class)
    fun validateFigureTwoOneThreeTwo() {
        validateJson("/example-oa-annotation/figure-2.1.3.2.jsonld")
    }

    @Test
    @Throws(Exception::class)
    fun validateFigureTwoOneThreeThree() {
        validateJson("/example-oa-annotation/figure-2.1.3.3.jsonld")
    }

    @Test
    @Throws(Exception::class)
    fun validateFigureTwoOneFour() {
        validateJson("/example-oa-annotation/figure-2.1.4.jsonld")
    }

    @Test
    @Throws(Exception::class)
    fun validateFigureTwoOneFive() {
        validateJson("/example-oa-annotation/figure-2.1.5.jsonld")
    }

    @Test
    @Throws(Exception::class)
    fun validateFigureTwoOneSix() {
        validateJson("/example-oa-annotation/figure-2.1.6.jsonld")
    }

    @Test
    @Throws(Exception::class)
    fun validateFigureTwoTwo() {
        validateJson("/example-oa-annotation/figure-2.2.jsonld")
    }

    @Test
    @Throws(Exception::class)
    fun validateFigureTwoTwoOne() {
        validateJson("/example-oa-annotation/figure-2.2.1.jsonld")
    }

    @Test
    @Throws(Exception::class)
    fun validateFigureThreeOneTwo() {
        validateJson("/example-oa-annotation/figure-3.1.2.jsonld")
    }

    @Test
    @Throws(Exception::class)
    fun validateFigureThreeTwoOne() {
        validateJson("/example-oa-annotation/figure-3.2.1.jsonld")
    }

    @Test
    @Throws(Exception::class)
    fun validateFigureThreeTwoTwoOne() {
        validateJson("/example-oa-annotation/figure-3.2.2.1.jsonld")
    }

    @Test
    @Throws(Exception::class)
    fun validateFigureThreeTwoTwoTwo() {
        validateJson("/example-oa-annotation/figure-3.2.2.2.jsonld")
    }

    @Test
    @Throws(Exception::class)
    fun validateFigureThreeTwoTwoThree() {
        validateJson("/example-oa-annotation/figure-3.2.2.3.jsonld")
    }

    @Test
    @Throws(Exception::class)
    fun validateFigureThreeTwoThreeOne() {
        validateJson("/example-oa-annotation/figure-3.2.3.1.jsonld")
    }

    @Test
    @Throws(Exception::class)
    fun validateFigureThreeThreeOne() {
        validateJson("/example-oa-annotation/figure-3.3.1.jsonld")
    }

    @Test
    @Throws(Exception::class)
    fun validateFigureThreeThreeTwo() {
        validateJson("/example-oa-annotation/figure-3.3.2.jsonld")
    }

    @Test
    @Throws(Exception::class)
    fun validateFigureThreeFourOne() {
        validateJson("/example-oa-annotation/figure-3.4.1.jsonld")
    }

    @Test
    @Throws(Exception::class)
    fun validateFigureThreeFive() {
        validateJson("/example-oa-annotation/figure-3.5.jsonld")
    }

    @Test
    @Throws(Exception::class)
    fun validateFigureFourOne() {
        validateJson("/example-oa-annotation/figure-4.1.jsonld")
    }

    @Test
    @Throws(Exception::class)
    fun validateFigureFourTwo() {
        validateJson("/example-oa-annotation/figure-4.2.jsonld")
    }

    @Test
    @Throws(Exception::class)
    fun validateFigureFourThree() {
        validateJson("/example-oa-annotation/figure-4.3.jsonld")
    }
}