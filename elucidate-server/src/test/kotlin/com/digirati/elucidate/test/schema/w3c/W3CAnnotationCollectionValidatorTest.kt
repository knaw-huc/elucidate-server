package com.digirati.elucidate.test.schema.w3c

import com.digirati.elucidate.test.schema.AbstractSchemaValidatorTest

class W3CAnnotationCollectionValidatorTest : AbstractSchemaValidatorTest() {
    override val schemaFileName: String
        get() = "/schema/w3c-annotation-collection-schema.json"
}