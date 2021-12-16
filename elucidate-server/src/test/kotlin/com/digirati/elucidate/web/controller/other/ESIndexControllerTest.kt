package com.digirati.elucidate.web.controller.other

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
    private lateinit var mockMvc: MockMvc

    @Before
    fun setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build()
    }

    @Test
    fun testBase() {
        mockMvc.get("/_es")
            .andExpect {
                status { isOk() }
                content { string("""{"about":"/_es/about","es_url":["http://example.org/elasticsearch-url"]}""") }
                header { string("content-type", "application/json") }
            }
            .andDo { print() }
    }

    @Test
    fun testAbout() {
        mockMvc.get("/_es/about")
            .andExpect {
                status { isOk() }
                content { string("about") }
                header { string("content-type", "text/plain;charset=utf-8") }
            }
            .andDo { print() }
    }
}