package com.digirati.elucidate.test.schema.w3c

import com.digirati.elucidate.test.schema.AbstractSchemaValidatorTest
import org.junit.Test

class W3CAnnotationValidatorTest : AbstractSchemaValidatorTest() {
    override val schemaFileName: String
        get() = "/schema/w3c-annotation-schema.json"

    @Test
    @Throws(Exception::class)
    fun validateExampleOne() {
        validateJson("/example-w3c-annotation/example1.jsonld")
    }

    @Test
    @Throws(Exception::class)
    fun validateExampleTwo() {
        validateJson("/example-w3c-annotation/example2.jsonld")
    }

    @Test
    @Throws(Exception::class)
    fun validateExampleThree() {
        validateJson("/example-w3c-annotation/example3.jsonld")
    }

    @Test
    @Throws(Exception::class)
    fun validateExampleFour() {
        validateJson("/example-w3c-annotation/example4.jsonld")
    }

    @Test
    @Throws(Exception::class)
    fun validateExampleFive() {
        validateJson("/example-w3c-annotation/example5.jsonld")
    }

    @Test
    @Throws(Exception::class)
    fun validateExampleSix() {
        validateJson("/example-w3c-annotation/example6.jsonld")
    }

    @Test
    @Throws(Exception::class)
    fun validateExampleSeven() {
        validateJson("/example-w3c-annotation/example7.jsonld")
    }

    @Test
    @Throws(Exception::class)
    fun validateExampleEight() {
        validateJson("/example-w3c-annotation/example8.jsonld")
    }

    @Test
    @Throws(Exception::class)
    fun validateExampleNine() {
        validateJson("/example-w3c-annotation/example9.jsonld")
    }

    @Test
    @Throws(Exception::class)
    fun validateExampleTen() {
        validateJson("/example-w3c-annotation/example10.jsonld")
    }

    @Test
    @Throws(Exception::class)
    fun validateExampleEleven() {
        validateJson("/example-w3c-annotation/example11.jsonld")
    }

    @Test
    @Throws(Exception::class)
    fun validateExampleTwelve() {
        validateJson("/example-w3c-annotation/example12.jsonld")
    }

    @Test
    @Throws(Exception::class)
    fun validateExampleThirteen() {
        validateJson("/example-w3c-annotation/example13.jsonld")
    }

    @Test
    @Throws(Exception::class)
    fun validateExampleFourteen() {
        validateJson("/example-w3c-annotation/example14.jsonld")
    }

    @Test
    @Throws(Exception::class)
    fun validateExampleFifteen() {
        validateJson("/example-w3c-annotation/example15.jsonld")
    }

    @Test
    @Throws(Exception::class)
    fun validateExampleSixteen() {
        validateJson("/example-w3c-annotation/example16.jsonld")
    }

    @Test
    @Throws(Exception::class)
    fun validateExampleSeventeen() {
        validateJson("/example-w3c-annotation/example17.jsonld")
    }

    @Test
    @Throws(Exception::class)
    fun validateExampleEighteen() {
        validateJson("/example-w3c-annotation/example18.jsonld")
    }

    @Test
    @Throws(Exception::class)
    fun validateExampleNineteen() {
        validateJson("/example-w3c-annotation/example19.jsonld")
    }

    @Test
    @Throws(Exception::class)
    fun validateExampleTwenty() {
        validateJson("/example-w3c-annotation/example20.jsonld")
    }

    @Test
    @Throws(Exception::class)
    fun validateExampleTwentyOne() {
        validateJson("/example-w3c-annotation/example21.jsonld")
    }

    @Test
    @Throws(Exception::class)
    fun validateExampleTwentyTwo() {
        validateJson("/example-w3c-annotation/example22.jsonld")
    }

    @Test
    @Throws(Exception::class)
    fun validateExampleTwentyThree() {
        validateJson("/example-w3c-annotation/example23.jsonld")
    }

    @Test
    @Throws(Exception::class)
    fun validateExampleTwentyFour() {
        validateJson("/example-w3c-annotation/example24.jsonld")
    }

    @Test
    @Throws(Exception::class)
    fun validateExampleTwentyFive() {
        validateJson("/example-w3c-annotation/example25.jsonld")
    }

    @Test
    @Throws(Exception::class)
    fun validateExampleTwentySix() {
        validateJson("/example-w3c-annotation/example26.jsonld")
    }

    @Test
    @Throws(Exception::class)
    fun validateExampleTwentySeven() {
        validateJson("/example-w3c-annotation/example27.jsonld")
    }

    @Test
    @Throws(Exception::class)
    fun validateExampleTwentyEight() {
        validateJson("/example-w3c-annotation/example28.jsonld")
    }

    @Test
    @Throws(Exception::class)
    fun validateExampleTwentyNine() {
        validateJson("/example-w3c-annotation/example29.jsonld")
    }

    @Test
    @Throws(Exception::class)
    fun validateExampleThirty() {
        validateJson("/example-w3c-annotation/example30.jsonld")
    }

    @Test
    @Throws(Exception::class)
    fun validateExampleThirtyOne() {
        validateJson("/example-w3c-annotation/example31.jsonld")
    }

    @Test
    @Throws(Exception::class)
    fun validateExampleThirtyTwo() {
        validateJson("/example-w3c-annotation/example32.jsonld")
    }

    @Test
    @Throws(Exception::class)
    fun validateExampleThirtyThree() {
        validateJson("/example-w3c-annotation/example33.jsonld")
    }

    @Test
    @Throws(Exception::class)
    fun validateExampleThirtyFour() {
        validateJson("/example-w3c-annotation/example34.jsonld")
    }

    @Test
    @Throws(Exception::class)
    fun validateExampleThirtyFive() {
        validateJson("/example-w3c-annotation/example35.jsonld")
    }

    @Test
    @Throws(Exception::class)
    fun validateExampleThirtySix() {
        validateJson("/example-w3c-annotation/example36.jsonld")
    }

    @Test
    @Throws(Exception::class)
    fun validateExampleThirtySeven() {
        validateJson("/example-w3c-annotation/example37.jsonld")
    }

    @Test
    @Throws(Exception::class)
    fun validateExampleThirtyEight() {
        validateJson("/example-w3c-annotation/example38.jsonld")
    }

    @Test
    @Throws(Exception::class)
    fun validateExampleThirtyNine() {
        validateJson("/example-w3c-annotation/example39.jsonld")
    }

    @Test
    @Throws(Exception::class)
    fun validateExampleFourty() {
        validateJson("/example-w3c-annotation/example40.jsonld")
    }

    @Test
    @Throws(Exception::class)
    fun validateExampleComplete() {
        validateJson("/example-w3c-annotation/example-complete.jsonld")
    }
}