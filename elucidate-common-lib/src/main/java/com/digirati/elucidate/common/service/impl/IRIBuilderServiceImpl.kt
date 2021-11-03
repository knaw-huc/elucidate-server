package com.digirati.elucidate.common.service.impl

import com.digirati.elucidate.common.infrastructure.constants.URLConstants
import com.digirati.elucidate.common.infrastructure.exception.InvalidIRIException
import com.digirati.elucidate.common.infrastructure.util.URIUtils.buildBaseUrl
import com.digirati.elucidate.common.service.IRIBuilderService
import org.apache.commons.lang3.StringUtils
import org.apache.http.client.utils.URIBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.net.URISyntaxException
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

@Service(IRIBuilderServiceImpl.SERVICE_NAME)
class IRIBuilderServiceImpl
@Autowired constructor(
    @Value("\${base.scheme}") baseScheme: String,
    @Value("\${base.host}") baseHost: String,
    @Value("\${base.port}") basePort: Int,
    @Value("\${base.path}") basePath: String
) : IRIBuilderService {

    private val baseUrl: String

    override fun buildW3CAnnotationIri(collectionId: String, annotationId: String): String =
        buildIri("w3c/$collectionId/$annotationId", null)

    override fun buildW3CCollectionIri(collectionId: String): String =
        buildIri("w3c/$collectionId/", null)

    override fun buildW3CPageIri(collectionId: String, page: Int, embeddedDescriptions: Boolean): String {
        val param = if (embeddedDescriptions) URLConstants.PARAM_DESC else URLConstants.PARAM_IRIS
        return buildIri("w3c/$collectionId/", mapOf(URLConstants.PARAM_PAGE to page, param to 1))
    }

    override fun buildW3CCollectionBodySearchIri(
        fields: List<String>,
        value: String,
        strict: Boolean,
        xywh: String,
        t: String,
        creatorIri: String,
        generatorIri: String
    ): String {
        return buildIri("w3c/services/search/body", object : HashMap<String, Any>() {
            init {
                put(URLConstants.PARAM_FIELDS, StringUtils.join(fields, ","))
                put(URLConstants.PARAM_VALUE, value)
                if (strict) {
                    put(URLConstants.PARAM_STRICT, strict)
                }
                if (StringUtils.isNotBlank(xywh)) {
                    put(URLConstants.PARAM_XYWH, xywh)
                }
                if (StringUtils.isNotBlank(t)) {
                    put(URLConstants.PARAM_T, t)
                }
                if (StringUtils.isNotBlank(creatorIri)) {
                    put(URLConstants.PARAM_CREATOR, creatorIri)
                }
                if (StringUtils.isNotBlank(generatorIri)) {
                    put(URLConstants.PARAM_GENERATOR, generatorIri)
                }
            }
        })
    }

    override fun buildW3CPageBodySearchIri(
        fields: List<String>,
        value: String,
        strict: Boolean,
        xywh: String,
        t: String,
        creatorIri: String,
        generatorIri: String,
        page: Int,
        embeddedDescriptions: Boolean
    ): String {
        return buildIri("w3c/services/search/body", object : HashMap<String, Any>() {
            init {
                put(URLConstants.PARAM_FIELDS, StringUtils.join(fields, ","))
                put(URLConstants.PARAM_VALUE, value)
                if (strict) {
                    put(URLConstants.PARAM_STRICT, strict)
                }
                if (StringUtils.isNotBlank(xywh)) {
                    put(URLConstants.PARAM_XYWH, xywh)
                }
                if (StringUtils.isNotBlank(t)) {
                    put(URLConstants.PARAM_T, t)
                }
                if (StringUtils.isNotBlank(creatorIri)) {
                    put(URLConstants.PARAM_CREATOR, creatorIri)
                }
                if (StringUtils.isNotBlank(generatorIri)) {
                    put(URLConstants.PARAM_GENERATOR, generatorIri)
                }
                put(URLConstants.PARAM_PAGE, page)
                if (embeddedDescriptions) {
                    put(URLConstants.PARAM_DESC, 1)
                } else {
                    put(URLConstants.PARAM_IRIS, 1)
                }
            }
        })
    }

    override fun buildW3CCollectionTargetSearchIri(
        fields: List<String>,
        value: String,
        strict: Boolean,
        xywh: String,
        t: String,
        creatorIri: String,
        generatorIri: String
    ): String {
        return buildIri("w3c/services/search/target", object : HashMap<String, Any>() {
            init {
                put(URLConstants.PARAM_FIELDS, StringUtils.join(fields, ","))
                put(URLConstants.PARAM_VALUE, value)
                if (strict) {
                    put(URLConstants.PARAM_STRICT, strict)
                }
                if (StringUtils.isNotBlank(xywh)) {
                    put(URLConstants.PARAM_XYWH, xywh)
                }
                if (StringUtils.isNotBlank(t)) {
                    put(URLConstants.PARAM_T, t)
                }
                if (StringUtils.isNotBlank(creatorIri)) {
                    put(URLConstants.PARAM_CREATOR, creatorIri)
                }
                if (StringUtils.isNotBlank(generatorIri)) {
                    put(URLConstants.PARAM_GENERATOR, generatorIri)
                }
            }
        })
    }

    override fun buildW3CPageTargetSearchIri(
        fields: List<String>,
        value: String,
        strict: Boolean,
        xywh: String,
        t: String,
        creatorIri: String,
        generatorIri: String,
        page: Int,
        embeddedDescriptions: Boolean
    ): String {
        return buildIri("w3c/services/search/target", object : HashMap<String, Any>() {
            init {
                put(URLConstants.PARAM_FIELDS, StringUtils.join(fields, ","))
                put(URLConstants.PARAM_VALUE, value)
                if (strict) {
                    put(URLConstants.PARAM_STRICT, strict)
                }
                if (StringUtils.isNotBlank(xywh)) {
                    put(URLConstants.PARAM_XYWH, xywh)
                }
                if (StringUtils.isNotBlank(t)) {
                    put(URLConstants.PARAM_T, t)
                }
                if (StringUtils.isNotBlank(creatorIri)) {
                    put(URLConstants.PARAM_CREATOR, creatorIri)
                }
                if (StringUtils.isNotBlank(generatorIri)) {
                    put(URLConstants.PARAM_GENERATOR, generatorIri)
                }
                put(URLConstants.PARAM_PAGE, page)
                if (embeddedDescriptions) {
                    put(URLConstants.PARAM_DESC, 1)
                } else {
                    put(URLConstants.PARAM_IRIS, 1)
                }
            }
        })
    }

    override fun buildW3CCollectionCreatorSearchIri(
        levels: List<String>,
        type: String,
        value: String,
        strict: Boolean
    ): String {
        return buildIri("w3c/services/search/creator", object : HashMap<String, Any>() {
            init {
                put(URLConstants.PARAM_LEVELS, StringUtils.join(levels, ","))
                put(URLConstants.PARAM_TYPE, type)
                put(URLConstants.PARAM_VALUE, value)
                if (strict) {
                    put(URLConstants.PARAM_STRICT, strict)
                }
            }
        })
    }

    override fun buildW3CPageCreatorSearchIri(
        levels: List<String>,
        type: String,
        value: String,
        strict: Boolean,
        page: Int,
        embeddedDescriptions: Boolean
    ): String {
        return buildIri("w3c/services/search/creator", object : HashMap<String, Any>() {
            init {
                put(URLConstants.PARAM_LEVELS, StringUtils.join(levels, ","))
                put(URLConstants.PARAM_TYPE, type)
                put(URLConstants.PARAM_VALUE, value)
                if (strict) {
                    put(URLConstants.PARAM_STRICT, strict)
                }
                put(URLConstants.PARAM_PAGE, page)
                if (embeddedDescriptions) {
                    put(URLConstants.PARAM_DESC, 1)
                } else {
                    put(URLConstants.PARAM_IRIS, 1)
                }
            }
        })
    }

    override fun buildW3CCollectionGeneratorSearchIri(
        levels: List<String>,
        type: String,
        value: String,
        strict: Boolean
    ): String {
        return buildIri("w3c/services/search/generator", object : HashMap<String, Any>() {
            init {
                put(URLConstants.PARAM_LEVELS, StringUtils.join(levels, ","))
                put(URLConstants.PARAM_TYPE, type)
                put(URLConstants.PARAM_VALUE, value)
                if (strict) {
                    put(URLConstants.PARAM_STRICT, strict)
                }
            }
        })
    }

    override fun buildW3CPageGeneratorSearchIri(
        levels: List<String>,
        type: String,
        value: String,
        strict: Boolean,
        page: Int,
        embeddedDescriptions: Boolean
    ): String {
        return buildIri("w3c/services/search/generator", object : HashMap<String, Any>() {
            init {
                put(URLConstants.PARAM_LEVELS, StringUtils.join(levels, ","))
                put(URLConstants.PARAM_TYPE, type)
                put(URLConstants.PARAM_VALUE, value)
                if (strict) {
                    put(URLConstants.PARAM_STRICT, strict)
                }
                put(URLConstants.PARAM_PAGE, page)
                if (embeddedDescriptions) {
                    put(URLConstants.PARAM_DESC, 1)
                } else {
                    put(URLConstants.PARAM_IRIS, 1)
                }
            }
        })
    }

    override fun buildW3CStatisticsPageIri(type: String, field: String, page: Int): String {
        return buildIri(String.format("w3c/services/stats/%s", type), object : HashMap<String, Any>() {
            init {
                put(URLConstants.PARAM_FIELD, field)
                put(URLConstants.PARAM_PAGE, page)
            }
        })
    }

    override fun buildW3CAnnotationHistoryIri(collectionId: String, annotationId: String, version: Int): String {
        return buildIri(String.format("w3c/services/history/%s/%s/%s", collectionId, annotationId, version), null)
    }

    override fun buildW3CPageTemporalSearchIri(
        levels: List<String>,
        types: List<String>,
        since: Date,
        page: Int,
        embeddedDescriptions: Boolean
    ): String {
        return buildIri("w3c/services/search/temporal", object : HashMap<String, Any>() {
            init {
                put(URLConstants.PARAM_LEVELS, StringUtils.join(levels, ","))
                put(URLConstants.PARAM_TYPES, StringUtils.join(types, ","))
                put(URLConstants.PARAM_SINCE, toString(since))
                put(URLConstants.PARAM_PAGE, page)
                if (embeddedDescriptions) {
                    put(URLConstants.PARAM_DESC, 1)
                } else {
                    put(URLConstants.PARAM_IRIS, 1)
                }
            }
        })
    }

    override fun buildOAAnnotationIri(collectionId: String, annotationId: String): String {
        return buildIri(String.format("oa/%s/%s", collectionId, annotationId), null)
    }

    override fun buildOACollectionIri(collectionId: String): String {
        var collectionId = collectionId
        collectionId = String.format("oa/%s/", collectionId)
        return buildIri(collectionId, null)
    }

    override fun buildOAPageIri(collectionId: String, page: Int, embeddedDescriptions: Boolean): String {
        var collectionId = collectionId
        collectionId = String.format("oa/%s/", collectionId)
        return buildIri(collectionId, object : HashMap<String, Any>() {
            init {
                put(URLConstants.PARAM_PAGE, page)
                if (embeddedDescriptions) {
                    put(URLConstants.PARAM_DESC, 1)
                } else {
                    put(URLConstants.PARAM_IRIS, 1)
                }
            }
        })
    }

    override fun buildOACollectionBodySearchIri(
        fields: List<String>,
        value: String,
        strict: Boolean,
        xywh: String,
        t: String,
        creatorIri: String,
        generatorIri: String
    ): String {
        return buildIri("oa/services/search/body", object : HashMap<String, Any>() {
            init {
                put(URLConstants.PARAM_FIELDS, StringUtils.join(fields, ","))
                put(URLConstants.PARAM_VALUE, value)
                if (strict) {
                    put(URLConstants.PARAM_STRICT, strict)
                }
                if (StringUtils.isNotBlank(xywh)) {
                    put(URLConstants.PARAM_XYWH, xywh)
                }
                if (StringUtils.isNotBlank(t)) {
                    put(URLConstants.PARAM_T, t)
                }
                if (StringUtils.isNotBlank(creatorIri)) {
                    put(URLConstants.PARAM_CREATOR, creatorIri)
                }
                if (StringUtils.isNotBlank(generatorIri)) {
                    put(URLConstants.PARAM_GENERATOR, generatorIri)
                }
            }
        })
    }

    override fun buildOAPageBodySearchIri(
        fields: List<String>,
        value: String,
        strict: Boolean,
        xywh: String,
        t: String,
        creatorIri: String,
        generatorIri: String,
        page: Int,
        embeddedDescriptions: Boolean
    ): String {
        return buildIri("oa/services/search/body", object : HashMap<String, Any>() {
            init {
                put(URLConstants.PARAM_FIELDS, StringUtils.join(fields, ","))
                put(URLConstants.PARAM_VALUE, value)
                if (strict) {
                    put(URLConstants.PARAM_STRICT, strict)
                }
                if (StringUtils.isNotBlank(xywh)) {
                    put(URLConstants.PARAM_XYWH, xywh)
                }
                if (StringUtils.isNotBlank(t)) {
                    put(URLConstants.PARAM_T, t)
                }
                if (StringUtils.isNotBlank(creatorIri)) {
                    put(URLConstants.PARAM_CREATOR, creatorIri)
                }
                if (StringUtils.isNotBlank(generatorIri)) {
                    put(URLConstants.PARAM_GENERATOR, generatorIri)
                }
                put(URLConstants.PARAM_PAGE, page)
                if (embeddedDescriptions) {
                    put(URLConstants.PARAM_DESC, 1)
                } else {
                    put(URLConstants.PARAM_IRIS, 1)
                }
            }
        })
    }

    override fun buildOACollectionTargetSearchIri(
        fields: List<String>,
        value: String,
        strict: Boolean,
        xywh: String,
        t: String,
        creatorIri: String,
        generatorIri: String
    ): String {
        return buildIri("oa/services/search/target", object : HashMap<String, Any>() {
            init {
                put(URLConstants.PARAM_FIELDS, StringUtils.join(fields, ","))
                put(URLConstants.PARAM_VALUE, value)
                if (strict) {
                    put(URLConstants.PARAM_STRICT, strict)
                }
                if (StringUtils.isNotBlank(xywh)) {
                    put(URLConstants.PARAM_XYWH, xywh)
                }
                if (StringUtils.isNotBlank(t)) {
                    put(URLConstants.PARAM_T, t)
                }
                if (StringUtils.isNotBlank(creatorIri)) {
                    put(URLConstants.PARAM_CREATOR, creatorIri)
                }
                if (StringUtils.isNotBlank(generatorIri)) {
                    put(URLConstants.PARAM_GENERATOR, generatorIri)
                }
            }
        })
    }

    override fun buildOAPageTargetSearchIri(
        fields: List<String>,
        value: String,
        strict: Boolean,
        xywh: String,
        t: String,
        creatorIri: String,
        generatorIri: String,
        page: Int,
        embeddedDescriptions: Boolean
    ): String {
        return buildIri("oa/services/search/target", object : HashMap<String, Any>() {
            init {
                put(URLConstants.PARAM_FIELDS, StringUtils.join(fields, ","))
                put(URLConstants.PARAM_VALUE, value)
                if (strict) {
                    put(URLConstants.PARAM_STRICT, strict)
                }
                if (StringUtils.isNotBlank(xywh)) {
                    put(URLConstants.PARAM_XYWH, xywh)
                }
                if (StringUtils.isNotBlank(t)) {
                    put(URLConstants.PARAM_T, t)
                }
                if (StringUtils.isNotBlank(creatorIri)) {
                    put(URLConstants.PARAM_CREATOR, creatorIri)
                }
                if (StringUtils.isNotBlank(generatorIri)) {
                    put(URLConstants.PARAM_GENERATOR, generatorIri)
                }
                put(URLConstants.PARAM_PAGE, page)
                if (embeddedDescriptions) {
                    put(URLConstants.PARAM_DESC, 1)
                } else {
                    put(URLConstants.PARAM_IRIS, 1)
                }
            }
        })
    }

    override fun buildOACollectionCreatorSearchIri(
        levels: List<String>,
        type: String,
        value: String,
        strict: Boolean
    ): String {
        return buildIri("oa/services/search/creator", object : HashMap<String, Any>() {
            init {
                put(URLConstants.PARAM_LEVELS, StringUtils.join(levels, ","))
                put(URLConstants.PARAM_TYPE, type)
                put(URLConstants.PARAM_VALUE, value)
                if (strict) {
                    put(URLConstants.PARAM_STRICT, strict)
                }
            }
        })
    }

    override fun buildOAPageCreatorSearchIri(
        levels: List<String>,
        type: String,
        value: String,
        strict: Boolean,
        page: Int,
        embeddedDescriptions: Boolean
    ): String {
        return buildIri("oa/services/search/creator", object : HashMap<String, Any>() {
            init {
                put(URLConstants.PARAM_LEVELS, StringUtils.join(levels, ","))
                put(URLConstants.PARAM_TYPE, type)
                put(URLConstants.PARAM_VALUE, value)
                if (strict) {
                    put(URLConstants.PARAM_STRICT, strict)
                }
                put(URLConstants.PARAM_PAGE, page)
                if (embeddedDescriptions) {
                    put(URLConstants.PARAM_DESC, 1)
                } else {
                    put(URLConstants.PARAM_IRIS, 1)
                }
            }
        })
    }

    override fun buildOACollectionGeneratorSearchIri(
        levels: List<String>,
        type: String,
        value: String,
        strict: Boolean
    ): String {
        return buildIri("oa/services/search/generator", object : HashMap<String, Any>() {
            init {
                put(URLConstants.PARAM_LEVELS, StringUtils.join(levels, ","))
                put(URLConstants.PARAM_TYPE, type)
                put(URLConstants.PARAM_VALUE, value)
                if (strict) {
                    put(URLConstants.PARAM_STRICT, strict)
                }
            }
        })
    }

    override fun buildOAPageGeneratorSearchIri(
        levels: List<String>,
        type: String,
        value: String,
        strict: Boolean,
        page: Int,
        embeddedDescriptions: Boolean
    ): String {
        return buildIri("oa/services/search/generator", object : HashMap<String, Any>() {
            init {
                put(URLConstants.PARAM_LEVELS, StringUtils.join(levels, ","))
                put(URLConstants.PARAM_TYPE, type)
                put(URLConstants.PARAM_VALUE, value)
                if (strict) {
                    put(URLConstants.PARAM_STRICT, strict)
                }
                put(URLConstants.PARAM_PAGE, page)
                if (embeddedDescriptions) {
                    put(URLConstants.PARAM_DESC, 1)
                } else {
                    put(URLConstants.PARAM_IRIS, 1)
                }
            }
        })
    }

    override fun buildOAStatisticsPageIri(type: String, field: String, page: Int): String {
        return buildIri(String.format("oa/services/stats/%s", type), object : HashMap<String, Any>() {
            init {
                put(URLConstants.PARAM_FIELD, field)
                put(URLConstants.PARAM_PAGE, page)
            }
        })
    }

    override fun buildOAAnnotationHistoryIri(collectionId: String, annotationId: String, version: Int): String {
        return buildIri(String.format("oa/services/history/%s/%s/%s", collectionId, annotationId, version), null)
    }

    override fun buildOACollectionTemporalSearchIri(
        levels: List<String>,
        types: List<String>,
        since: Date
    ): String {
        return buildIri("oa/services/search/temporal", object : HashMap<String, Any>() {
            init {
                put(URLConstants.PARAM_LEVELS, StringUtils.join(levels, ","))
                put(URLConstants.PARAM_TYPES, StringUtils.join(types, ","))
                put(URLConstants.PARAM_SINCE, toString(since))
            }
        })
    }

    override fun buildOAPageTemporalSearchIri(
        levels: List<String>,
        types: List<String>,
        since: Date,
        page: Int,
        embeddedDescriptions: Boolean
    ): String {
        return buildIri("oa/services/search/temporal", object : HashMap<String, Any>() {
            init {
                put(URLConstants.PARAM_LEVELS, StringUtils.join(levels, ","))
                put(URLConstants.PARAM_TYPES, StringUtils.join(types, ","))
                put(URLConstants.PARAM_SINCE, toString(since))
                put(URLConstants.PARAM_PAGE, page)
                if (embeddedDescriptions) {
                    put(URLConstants.PARAM_DESC, 1)
                } else {
                    put(URLConstants.PARAM_IRIS, 1)
                }
            }
        })
    }

    override fun buildOACollectionOverlapSearchIri(lowerLevel: Int, upperLevel: Int): String {
        val params = mapOf(
            URLConstants.PARAM_LOWER_LEVEL to lowerLevel,
            URLConstants.PARAM_UPPER_LEVEL to upperLevel
        )
        return buildIri("oa/services/search/overlap", params)
    }

    override fun buildOAPageOverlapSearchIri(
        lowerLevel: Int,
        upperLevel: Int,
        page: Int,
        embeddedDescriptions: Boolean
    ): String {
        val params = mutableMapOf(
            URLConstants.PARAM_LOWER_LEVEL to lowerLevel,
            URLConstants.PARAM_UPPER_LEVEL to upperLevel,
            URLConstants.PARAM_PAGE to page
        )
        if (embeddedDescriptions) {
            params[URLConstants.PARAM_DESC] = 1
        } else {
            params[URLConstants.PARAM_IRIS] = 1
        }
        return buildIri("oa/services/search/overlap", params)
    }

    override fun buildW3CCollectionTemporalSearchIri(
        levels: List<String>,
        types: List<String>,
        since: Date
    ): String =
        buildIri(
            "w3c/services/search/temporal",
            mapOf(
                URLConstants.PARAM_LEVELS to StringUtils.join(levels, ","),
                URLConstants.PARAM_TYPES to StringUtils.join(types, ","),
                URLConstants.PARAM_SINCE to toString(since)
            )
        )

    private fun buildIri(id: String, params: Map<String, Any>?): String = try {
        val builder = URIBuilder(baseUrl)
        builder.path = "${builder.path}/$id"
        if (params != null && params.isNotEmpty()) {
            for ((key, value) in params) {
                builder.addParameter(key, value.toString())
            }
        }
        builder.toString()
    } catch (e: URISyntaxException) {
        throw InvalidIRIException(
            "An error occurred building IRI with base URL [$baseUrl] with ID [$id] and parameters [$params]", e
        )
    }

    companion object {
        val DATE_FORMAT = DateTimeFormatter.ISO_DATE_TIME
        const val SERVICE_NAME = "iriBuilderServiceImpl"

        fun toString(since: Date): String =
            DATE_FORMAT.format(
                since.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime()
            )
    }

    init {
        baseUrl = buildBaseUrl(baseScheme, baseHost, basePort, basePath)
    }
}