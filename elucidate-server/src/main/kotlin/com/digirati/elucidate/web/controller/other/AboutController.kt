package com.digirati.elucidate.web.controller.other

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import java.time.Instant
import java.util.*

@Controller(AboutController.CONTROLLER_NAME)
@RequestMapping(value = ["/_about2"])
class AboutController {

    companion object {
        const val CONTROLLER_NAME = "aboutController"
        val startDate = Date()

        private val objectMapper = ObjectMapper()
    }

    data class About(
        val version: String,
        val startedAt: Instant,
        val authEnabled: Boolean
    )

    @Autowired
    lateinit var env: Environment

    @RequestMapping(
        method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getAbout(): ResponseEntity<About> =
        ResponseEntity
            .ok()
            .header("X-Message", "Listen carefully, I will say this only once!")
            .body(
                About(
                    version = "v1.0.0",
                    startedAt = startDate.toInstant(),
                    authEnabled = env.getRequiredProperty(
                        "auth.enabled",
                        Boolean::class.java
                    )
                )
            )

}