package com.digirati.elucidate.web.controller.other

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.put
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@RunWith(SpringRunner::class)
@SpringBootTest(
    classes = [ESIndexController::class],
    properties = [
        "elasticsearch.protocol=http",
        "elasticsearch.host=localhost",
        "elasticsearch.port=9200"
    ]
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
                content {
                    json(
                        """{
                    |"reindex":"/_es/reindex",
                    |"es_url":"http://localhost:9200"
                    |}""".trimMargin()
                    )
                }
                header { string("content-type", "application/json") }
            }
            .andDo { print() }
    }

    @Test
    fun testReIndex() {
        mockMvc.put("/_es/reindex")
            .andExpect {
                status { isOk() }
                content { string("reindexing started") }
            }
            .andDo { print() }
    }
}