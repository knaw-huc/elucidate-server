package com.digirati.elucidate.web.controller.other

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@RunWith(SpringRunner::class)
@SpringBootTest(
    classes = [ESIndexController::class],
    properties = ["elasticsearch.url=http://example.org/elasticsearch-url"]
)
class ESIndexControllerTest {

    @Autowired
    lateinit var webApplicationContext: WebApplicationContext
    lateinit var mockMvc: MockMvc

    @Before
    fun setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build()
    }

    @Test
    fun testBase() {
        val r = mockMvc.get("/_es")
            .andExpect {
                status { isOk() }
                content { string("""{"about":"/_es/about","es_url":["http://example.org/elasticsearch-url"]}""") }
            }
            .andDo { print() }
            .andReturn()
        assertThat(r.response.contentType).isEqualTo("application/json")
    }

    @Test
    fun testAbout() {
        val r = mockMvc.get("/_es/about")
            .andExpect {
                status { isOk() }
                content { string("about") }
            }
            .andDo { print() }
            .andReturn()
        assertThat(r.response.contentType).isEqualTo("text/plain;charset=utf-8")
    }
}