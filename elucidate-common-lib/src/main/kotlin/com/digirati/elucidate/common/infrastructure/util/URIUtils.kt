package com.digirati.elucidate.common.infrastructure.util

import org.apache.http.client.utils.URIBuilder
import java.util.*

object URIUtils {
    private val DEFAULT_PORTS: Map<String, Int> = mapOf(
            "http" to 80,
            "https" to 443
    )

    @JvmStatic
    fun buildBaseUrl(baseScheme: String, baseHost: String?, basePort: Int, basePath: String?): String {
        val builder = URIBuilder()
        builder.scheme = baseScheme
        builder.host = baseHost
        if (!DEFAULT_PORTS.containsKey(baseScheme.lowercase(Locale.getDefault())) || DEFAULT_PORTS[baseScheme] != basePort) {
            builder.port = basePort
        }
        builder.path = basePath
        return builder.toString()
    }
}