package com.digirati.elucidate.web.controller.other

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import java.util.*

@Controller(AboutController.CONTROLLER_NAME)
@RequestMapping(value = ["/_about"])
class AboutController {

    companion object {
        const val CONTROLLER_NAME = "aboutController"
        val startDate = Date()

        private val objectMapper = ObjectMapper()
    }

    @Autowired
    lateinit var env: Environment

    @RequestMapping(
        method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getAbout(): ResponseEntity<Any> {
        val version = "v1.0.0"
        val map: Map<String, Any> = mapOf(
            "version" to version,
            "startedAt" to startDate.toInstant().toString(),
            "authEnabled" to env.getRequiredProperty(
                "auth.enabled",
                Boolean::class.java
            )

        )
        val json = objectMapper.writeValueAsString(map)
        return ResponseEntity.ok(json)
    }

}