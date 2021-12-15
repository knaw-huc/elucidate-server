package com.digirati.elucidate.web.controller.other

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@Controller(ESIndexController.CONTROLLER_NAME)
@RequestMapping(value = ["/_es"])
class ESIndexController {

    var objectMapper = ObjectMapper()

    @Autowired
    lateinit var env: Environment

    @RequestMapping(
        value = [REQUEST_PATH_BASE],
        method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getBase(): ResponseEntity<Any> {
        val map: Map<String, Any> = mapOf(
            "about" to "/_es$REQUEST_PATH_ABOUT",
            "es_url" to env.getRequiredProperty(
                "elasticsearch.url",
                Array<String>::class.java
            )
        )
        val json = objectMapper.writeValueAsString(map)
        return ResponseEntity.ok(json)
    }

    @RequestMapping(
        value = [REQUEST_PATH_ABOUT],
        method = [RequestMethod.GET],
        produces = [MediaType.TEXT_PLAIN_VALUE + ";charset=utf-8"]
    )
    fun getAbout(): ResponseEntity<String> {
        val body = "about"
        return ResponseEntity.ok(body)
    }

    companion object {
        const val CONTROLLER_NAME = "esIndexController"

        private const val REQUEST_PATH_BASE = ""
        private const val REQUEST_PATH_ABOUT = "/about"
    }
}