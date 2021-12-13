package com.digirati.elucidate.common.service.impl

import com.digirati.elucidate.common.infrastructure.constants.URLConstants.PARAM_CREATOR
import com.digirati.elucidate.common.infrastructure.constants.URLConstants.PARAM_DESC
import com.digirati.elucidate.common.infrastructure.constants.URLConstants.PARAM_FIELD
import com.digirati.elucidate.common.infrastructure.constants.URLConstants.PARAM_FIELDS
import com.digirati.elucidate.common.infrastructure.constants.URLConstants.PARAM_GENERATOR
import com.digirati.elucidate.common.infrastructure.constants.URLConstants.PARAM_IRIS
import com.digirati.elucidate.common.infrastructure.constants.URLConstants.PARAM_LEVELS
import com.digirati.elucidate.common.infrastructure.constants.URLConstants.PARAM_PAGE
import com.digirati.elucidate.common.infrastructure.constants.URLConstants.PARAM_RANGE_END
import com.digirati.elucidate.common.infrastructure.constants.URLConstants.PARAM_RANGE_START
import com.digirati.elucidate.common.infrastructure.constants.URLConstants.PARAM_SINCE
import com.digirati.elucidate.common.infrastructure.constants.URLConstants.PARAM_STRICT
import com.digirati.elucidate.common.infrastructure.constants.URLConstants.PARAM_T
import com.digirati.elucidate.common.infrastructure.constants.URLConstants.PARAM_TARGET_ID
import com.digirati.elucidate.common.infrastructure.constants.URLConstants.PARAM_TYPE
import com.digirati.elucidate.common.infrastructure.constants.URLConstants.PARAM_TYPES
import com.digirati.elucidate.common.infrastructure.constants.URLConstants.PARAM_VALUE
import com.digirati.elucidate.common.infrastructure.constants.URLConstants.PARAM_XYWH
import com.digirati.elucidate.common.infrastructure.exception.InvalidIRIException
import com.digirati.elucidate.common.infrastructure.util.URIUtils.buildBaseUrl
import com.digirati.elucidate.common.model.annotation.AnnotationFormat
import com.digirati.elucidate.common.model.annotation.prefix
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
    @Value("\${base.host}") baseHost: String?,
    @Value("\${base.port}") basePort: Int,
    @Value("\${base.path}") basePath: String
) : IRIBuilderService {

    private val baseUrl: String

    override fun buildCollectionIri(format: AnnotationFormat, collectionId: String): String =
        buildIri("${format.prefix()}/$collectionId/")

    override fun buildCollectionPageIri(
        format: AnnotationFormat,
        collectionId: String,
        page: Int,
        embeddedDescriptions: Boolean
    ): String {
        val param = if (embeddedDescriptions) PARAM_DESC else PARAM_IRIS
        return buildIri("${format.prefix()}/$collectionId/", mapOf(PARAM_PAGE to page, param to 1))
    }

    override fun buildAnnotationIri(format: AnnotationFormat, collectionId: String, annotationId: String): String =
        buildIri("${format.prefix()}/$collectionId/$annotationId")

    override fun buildAnnotationHistoryIri(
        format: AnnotationFormat,
        collectionId: String,
        annotationId: String,
        version: Int
    ): String =
        buildIri("${format.prefix()}/services/history/$collectionId/$annotationId/$version")

    //------------------------------------------------//

    override fun buildSearchByBodyIri(
        format: AnnotationFormat,
        fields: List<String>,
        value: String,
        strict: Boolean?,
        xywh: String?,
        t: String?,
        creatorIri: String?,
        generatorIri: String?
    ): String {
        val params = mutableMapOf<String, Any>(
            PARAM_FIELDS to StringUtils.join(fields, ","),
            PARAM_VALUE to value
        )
        if (strict != null && strict) {
            params[PARAM_STRICT] = strict
        }
        if (StringUtils.isNotBlank(xywh)) {
            params[PARAM_XYWH] = xywh!!
        }
        if (StringUtils.isNotBlank(t)) {
            params[PARAM_T] = t!!
        }
        if (StringUtils.isNotBlank(creatorIri)) {
            params[PARAM_CREATOR] = creatorIri!!
        }
        if (StringUtils.isNotBlank(generatorIri)) {
            params[PARAM_GENERATOR] = generatorIri!!
        }
        return buildIri("${format.prefix()}/services/search/body", params)
    }

    override fun buildSearchByBodyPageIri(
        format: AnnotationFormat,
        fields: List<String>,
        value: String,
        strict: Boolean?,
        xywh: String?,
        t: String?,
        creatorIri: String?,
        generatorIri: String?,
        page: Int,
        embeddedDescriptions: Boolean
    ): String {
        val params = mutableMapOf<String, Any>(
            PARAM_FIELDS to StringUtils.join(fields, ","),
            PARAM_VALUE to value
        )
        if (strict != null && strict) {
            params[PARAM_STRICT] = strict
        }
        if (StringUtils.isNotBlank(xywh)) {
            params[PARAM_XYWH] = xywh!!
        }
        if (StringUtils.isNotBlank(t)) {
            params[PARAM_T] = t!!
        }
        if (StringUtils.isNotBlank(creatorIri)) {
            params[PARAM_CREATOR] = creatorIri!!
        }
        if (StringUtils.isNotBlank(generatorIri)) {
            params[PARAM_GENERATOR] = generatorIri!!
        }
        params[PARAM_PAGE] = page
        if (embeddedDescriptions) {
            params[PARAM_DESC] = 1
        } else {
            params[PARAM_IRIS] = 1
        }
        return buildIri("${format.prefix()}/services/search/body", params)
    }

    override fun buildSearchByTargetIri(
        format: AnnotationFormat,
        fields: List<String>,
        value: String,
        strict: Boolean?,
        xywh: String?,
        t: String?,
        creatorIri: String?,
        generatorIri: String?
    ): String {
        val params = mutableMapOf<String, Any>(
            PARAM_FIELDS to StringUtils.join(fields, ","),
            PARAM_VALUE to value
        )
        if (strict != null && strict) {
            params[PARAM_STRICT] = strict
        }
        if (StringUtils.isNotBlank(xywh)) {
            params[PARAM_XYWH] = xywh!!
        }
        if (StringUtils.isNotBlank(t)) {
            params[PARAM_T] = t!!
        }
        if (StringUtils.isNotBlank(creatorIri)) {
            params[PARAM_CREATOR] = creatorIri!!
        }
        if (StringUtils.isNotBlank(generatorIri)) {
            params[PARAM_GENERATOR] = generatorIri!!
        }
        return buildIri("${format.prefix()}/services/search/target", params)
    }

    override fun buildSearchByTargetPageIri(
        format: AnnotationFormat,
        fields: List<String>,
        value: String,
        strict: Boolean?,
        xywh: String?,
        t: String?,
        creatorIri: String?,
        generatorIri: String?,
        page: Int,
        embeddedDescriptions: Boolean
    ): String {
        val params = object : HashMap<String, Any>() {
            init {
                put(PARAM_FIELDS, StringUtils.join(fields, ","))
                put(PARAM_VALUE, value)
                if (strict != null && strict) {
                    put(PARAM_STRICT, strict)
                }
                if (StringUtils.isNotBlank(xywh)) {
                    put(PARAM_XYWH, xywh!!)
                }
                if (StringUtils.isNotBlank(t)) {
                    put(PARAM_T, t!!)
                }
                if (StringUtils.isNotBlank(creatorIri)) {
                    put(PARAM_CREATOR, creatorIri!!)
                }
                if (StringUtils.isNotBlank(generatorIri)) {
                    put(PARAM_GENERATOR, generatorIri!!)
                }
                put(PARAM_PAGE, page)
                if (embeddedDescriptions) {
                    put(PARAM_DESC, 1)
                } else {
                    put(PARAM_IRIS, 1)
                }
            }
        }
        return buildIri("${format.prefix()}/services/search/target", params)
    }

    override fun buildSearchByCreatorIri(
        format: AnnotationFormat,
        levels: List<String>,
        type: String,
        value: String,
        strict: Boolean?
    ): String {
        val params = object : HashMap<String, Any>() {
            init {
                put(PARAM_LEVELS, StringUtils.join(levels, ","))
                put(PARAM_TYPE, type)
                put(PARAM_VALUE, value)
                if (strict != null && strict) {
                    put(PARAM_STRICT, strict)
                }
            }
        }
        return buildIri("${format.prefix()}/services/search/creator", params)
    }

    override fun buildSearchByCreatorPageIri(
        format: AnnotationFormat,
        levels: List<String>,
        type: String,
        value: String,
        strict: Boolean?,
        page: Int,
        embeddedDescriptions: Boolean
    ): String {
        val params = object : HashMap<String, Any>() {
            init {
                put(PARAM_LEVELS, StringUtils.join(levels, ","))
                put(PARAM_TYPE, type)
                put(PARAM_VALUE, value)
                if (strict != null && strict) {
                    put(PARAM_STRICT, strict)
                }
                put(PARAM_PAGE, page)
                if (embeddedDescriptions) {
                    put(PARAM_DESC, 1)
                } else {
                    put(PARAM_IRIS, 1)
                }
            }
        }
        return buildIri("${format.prefix()}/services/search/creator", params)
    }

    override fun buildSearchByGeneratorIri(
        format: AnnotationFormat,
        levels: List<String>,
        type: String,
        value: String,
        strict: Boolean?
    ): String {
        val params = mutableMapOf<String, Any>(
            PARAM_LEVELS to StringUtils.join(levels, ","),
            PARAM_TYPE to type,
            PARAM_VALUE to value
        )
        if (strict != null && strict) {
            params[PARAM_STRICT] = strict
        }
        return buildIri("${format.prefix()}/services/search/generator", params)
    }

    override fun buildSearchByGeneratorPageIri(
        format: AnnotationFormat,
        levels: List<String>,
        type: String,
        value: String,
        strict: Boolean?,
        page: Int,
        embeddedDescriptions: Boolean
    ): String {
        val params = object : HashMap<String, Any>() {
            init {
                put(PARAM_LEVELS, StringUtils.join(levels, ","))
                put(PARAM_TYPE, type)
                put(PARAM_VALUE, value)
                if (strict != null && strict) {
                    put(PARAM_STRICT, strict)
                }
                put(PARAM_PAGE, page)
                if (embeddedDescriptions) {
                    put(PARAM_DESC, 1)
                } else {
                    put(PARAM_IRIS, 1)
                }
            }
        }
        return buildIri("${format.prefix()}/services/search/generator", params)
    }

    override fun buildSearchByRangeIri(
        format: AnnotationFormat,
        targetId: String,
        rangeStart: Float,
        rangeEnd: Float
    ): String =
        buildIri(
            "${format.prefix()}/services/search/range",
            mapOf<String, Any>(
                PARAM_TARGET_ID to targetId,
                PARAM_RANGE_START to rangeStart,
                PARAM_RANGE_END to rangeEnd
            )
        )

    override fun buildSearchByRangePageIri(
        format: AnnotationFormat,
        targetId: String,
        rangeStart: Float,
        rangeEnd: Float,
        page: Int,
        embeddedDescriptions: Boolean
    ): String {
        val params = mutableMapOf<String, Any>(
            PARAM_TARGET_ID to targetId,
            PARAM_RANGE_START to rangeStart,
            PARAM_RANGE_END to rangeEnd,
            PARAM_PAGE to page
        )
        if (embeddedDescriptions) {
            params[PARAM_DESC] = 1
        } else {
            params[PARAM_IRIS] = 1
        }
        return buildIri("${format.prefix()}/services/search/range", params)
    }

    override fun buildSearchByOverlapIri(
        format: AnnotationFormat,
        targetId: String,
        rangeStart: Float,
        rangeEnd: Float
    ): String =
        buildIri(
            "${format.prefix()}/services/search/overlap",
            mapOf<String, Any>(
                PARAM_TARGET_ID to targetId,
                PARAM_RANGE_START to rangeStart,
                PARAM_RANGE_END to rangeEnd
            )
        )

    override fun buildSearchByOverlapPageIri(
        format: AnnotationFormat,
        targetId: String,
        rangeStart: Float,
        rangeEnd: Float,
        page: Int,
        embeddedDescriptions: Boolean
    ): String {
        val params = mutableMapOf(
            PARAM_TARGET_ID to targetId,
            PARAM_RANGE_START to rangeStart,
            PARAM_RANGE_END to rangeEnd,
            PARAM_PAGE to page
        )
        if (embeddedDescriptions) {
            params[PARAM_DESC] = 1
        } else {
            params[PARAM_IRIS] = 1
        }
        return buildIri("${format.prefix()}/services/search/overlap", params)
    }

    override fun buildSearchByTemporalIri(
        format: AnnotationFormat,
        levels: List<String>,
        types: List<String>,
        since: Date
    ): String =
        buildIri(
            "${format.prefix()}/services/search/temporal",
            mapOf(
                PARAM_LEVELS to StringUtils.join(levels, ","),
                PARAM_TYPES to StringUtils.join(types, ","),
                PARAM_SINCE to toString(since)
            )
        )

    override fun buildSearchByTemporalPageIri(
        format: AnnotationFormat,
        levels: List<String>,
        types: List<String>,
        since: Date,
        page: Int,
        embeddedDescriptions: Boolean
    ): String {
        val params = mutableMapOf<String, Any>(
            PARAM_LEVELS to StringUtils.join(levels, ","),
            PARAM_TYPES to StringUtils.join(types, ","),
            PARAM_SINCE to toString(since),
            PARAM_PAGE to page
        )
        if (embeddedDescriptions) {
            params[PARAM_DESC] = 1
        } else {
            params[PARAM_IRIS] = 1
        }
        return buildIri("${format.prefix()}/services/search/temporal", params)
    }

    override fun buildStatisticsPageIri(format: AnnotationFormat, type: String, field: String, page: Int): String =
        buildIri(
            "${format.prefix()}/services/stats/$type",
            mapOf(
                PARAM_FIELD to field,
                PARAM_PAGE to page
            )
        )

    private fun buildIri(id: String, params: Map<String, Any>? = null): String =
        try {
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
        private val DATE_FORMAT: DateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME
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