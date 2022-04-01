package com.digirati.elucidate.web.controller.other

import org.hamcrest.CoreMatchers.containsString
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@RunWith(SpringRunner::class)
@SpringBootTest(
    classes = [AboutController::class],
    properties = ["auth.enabled=false"]
)
class AboutControllerTest {

    @Autowired
    lateinit var webApplicationContext: WebApplicationContext
    private lateinit var mockMvc: MockMvc

    @Before
    fun setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build()
    }

    @Test
    fun testAbout() {
        mockMvc.get("/_about2")
            .andExpect {
                status { isOk() }
                content { string(containsString(""""authEnabled":false""")) }
                header { string("content-type", "application/json") }
            }
            .andDo { print() }
    }

}