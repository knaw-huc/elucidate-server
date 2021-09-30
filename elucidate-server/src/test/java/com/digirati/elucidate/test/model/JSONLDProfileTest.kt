package com.digirati.elucidate.test.model

import com.digirati.elucidate.common.infrastructure.constants.JSONLDConstants
import com.digirati.elucidate.model.JSONLDProfile
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.hamcrest.Matchers.equalTo
import org.junit.Assert.assertNotNull
import org.junit.Test

class JSONLDProfileTest {
    @Test
    fun testFormatCompacted() {
        val format = JSONLDProfile.Format.COMPACTED
        assertNotNull(format)
        assertThat(format, Matchers.`is`(equalTo(JSONLDProfile.Format.COMPACTED)))
    }

    @Test
    fun testFormatExpanded() {
        val format = JSONLDProfile.Format.EXPANDED
        assertNotNull(format)
        assertThat(format, Matchers.`is`(equalTo(JSONLDProfile.Format.EXPANDED)))
    }

    @Test
    fun testFormatFlattened() {
        val format = JSONLDProfile.Format.FLATTENED
        assertNotNull(format)
        assertThat(format, Matchers.`is`(equalTo(JSONLDProfile.Format.FLATTENED)))
    }

    @Test
    fun testJsonLdProfile() {
        val contexts = buildContexts()
        val formats = buildFormats()
        val jsonLdProfile = JSONLDProfile()
        jsonLdProfile.contexts = contexts
        jsonLdProfile.formats = formats
        assertThat(contexts, Matchers.`is`(equalTo(jsonLdProfile.contexts)))
        assertThat(formats, Matchers.`is`(equalTo(jsonLdProfile.formats)))
    }

    private fun buildContexts(): Map<String, List<String>> {
        return object : HashMap<String, List<String>>() {
            init {
                put(JSONLDConstants.ATTRIBUTE_CONTEXT, object : ArrayList<String>() {
                    init {
                        add(TEST_CONTEXT_1)
                        add(TEST_CONTEXT_2)
                    }
                })
            }
        }
    }

    private fun buildFormats(): List<JSONLDProfile.Format> {
        return object : ArrayList<JSONLDProfile.Format>() {
            init {
                this.addAll(listOf(*JSONLDProfile.Format.values()))
            }
        }
    }

    companion object {
        private const val TEST_CONTEXT_1 = "http://www.digirati.com/context1.jsonld"
        private const val TEST_CONTEXT_2 = "http://www.digirati.com/context2.jsonld"
    }
}