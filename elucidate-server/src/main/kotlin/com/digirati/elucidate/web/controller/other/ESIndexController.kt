package com.digirati.elucidate.web.controller.other

import com.fasterxml.jackson.databind.ObjectMapper
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.client.create
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

    companion object {
        const val CONTROLLER_NAME = "esIndexController"

        private const val REQUEST_PATH_BASE = ""
        private const val REQUEST_PATH_REINDEX = "/reindex"

        private val objectMapper = ObjectMapper()
    }

    lateinit var env: Environment
        private set

    lateinit var esProtocol: String
        private set

    lateinit var esHost: String
        private set

    lateinit var esPort: Integer
        private set

    lateinit var esClient: RestHighLevelClient
        private set

    @Autowired
    fun setEnv(environment: Environment) {
        env = environment
        esProtocol = env.getRequiredProperty("elasticsearch.protocol", String::class.java)
        esHost = env.getRequiredProperty("elasticsearch.host", String::class.java)
        esPort = env.getRequiredProperty("elasticsearch.port", Integer::class.java)
        esClient = create(host = esHost, port = esPort.toInt(), https = (esProtocol == "https"))
    }

    @RequestMapping(
        value = [REQUEST_PATH_BASE],
        method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getBase(): ResponseEntity<Any> {
        val esUrl = "${esProtocol}://${esHost}:${esPort}"
        val map: Map<String, Any> = mapOf(
            "reindex" to "/_es$REQUEST_PATH_REINDEX",
            "es_url" to esUrl
        )
        val json = objectMapper.writeValueAsString(map)
        return ResponseEntity.ok(json)
    }

    @RequestMapping(
        value = [REQUEST_PATH_REINDEX],
        method = [RequestMethod.PUT]
    )
    fun startReIndex(): ResponseEntity<String> {
        val message = "reindexing started"
        return ResponseEntity.ok(message)
    }

}