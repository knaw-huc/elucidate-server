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

    override fun buildW3CAnnotationIri(collectionId: String, annotationId: String): String =
        buildIri("w3c/$collectionId/$annotationId")

    override fun buildW3CCollectionIri(collectionId: String): String =
        buildIri("w3c/$collectionId/")

    override fun buildW3CPageIri(collectionId: String, page: Int, embeddedDescriptions: Boolean): String {
        val param = if (embeddedDescriptions) PARAM_DESC else PARAM_IRIS
        return buildIri("w3c/$collectionId/", mapOf(PARAM_PAGE to page, param to 1))
    }

    override fun buildW3CCollectionBodySearchIri(
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
        return buildIri("w3c/services/search/body", params)
    }

    override fun buildW3CPageBodySearchIri(
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
        return buildIri("w3c/services/search/body", params)
    }

    override fun buildW3CCollectionTargetSearchIri(
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
        return buildIri("w3c/services/search/target", params)
    }

    override fun buildW3CPageTargetSearchIri(
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
        return buildIri("w3c/services/search/target", params)
    }

    override fun buildW3CCollectionCreatorSearchIri(
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
        return buildIri("w3c/services/search/creator", params)
    }

    override fun buildW3CPageCreatorSearchIri(
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
        return buildIri("w3c/services/search/creator", params)
    }

    override fun buildW3CCollectionGeneratorSearchIri(
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
        return buildIri("w3c/services/search/generator", params)
    }

    override fun buildW3CPageGeneratorSearchIri(
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
        return buildIri("w3c/services/search/generator", params)
    }

    override fun buildW3CStatisticsPageIri(type: String, field: String, page: Int): String =
        buildIri(
            "w3c/services/stats/$type",
            mapOf(
                PARAM_FIELD to field,
                PARAM_PAGE to page
            )
        )

    // search by range

    override fun buildW3CCollectionRangeSearchIri(targetId: String, rangeStart: Int, rangeEnd: Int): String =
        buildIri(
            "w3c/services/search/range",
            mapOf<String, Any>(
                PARAM_TARGET_ID to targetId,
                PARAM_RANGE_START to rangeStart,
                PARAM_RANGE_END to rangeEnd
            )
        )

    override fun buildW3CPageRangeSearchIri(
        targetId: String,
        rangeStart: Int,
        rangeEnd: Int,
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
        return buildIri("w3c/services/search/range", params)
    }

    override fun buildOACollectionRangeSearchIri(targetId: String, rangeStart: Int, rangeEnd: Int): String =
        buildIri(
            "oa/services/search/range",
            mapOf<String, Any>(
                PARAM_TARGET_ID to targetId,
                PARAM_RANGE_START to rangeStart,
                PARAM_RANGE_END to rangeEnd
            )
        )


    override fun buildOAPageRangeSearchIri(
        targetId: String,
        rangeStart: Int,
        rangeEnd: Int,
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
        return buildIri("oa/services/search/range", params)
    }

    // search by overlap

    override fun buildW3CCollectionOverlapSearchIri(targetId: String, rangeStart: Int, rangeEnd: Int): String =
        buildIri(
            "w3c/services/search/overlap",
            mapOf<String, Any>(
                PARAM_TARGET_ID to targetId,
                PARAM_RANGE_START to rangeStart,
                PARAM_RANGE_END to rangeEnd
            )
        )

    override fun buildW3CPageOverlapSearchIri(
        targetId: String,
        rangeStart: Int,
        rangeEnd: Int,
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
        return buildIri("w3c/services/search/overlap", params)
    }

    // history

    override fun buildW3CAnnotationHistoryIri(collectionId: String, annotationId: String, version: Int): String =
        buildIri("w3c/services/history/$collectionId/$annotationId/$version")


    // search by temporal

    override fun buildW3CPageTemporalSearchIri(
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
        return buildIri("w3c/services/search/temporal", params)
    }

    override fun buildOAAnnotationIri(collectionId: String, annotationId: String): String =
        buildIri("oa/$collectionId/$annotationId")

    override fun buildOACollectionIri(collectionId: String): String =
        buildIri("oa/$collectionId/")

    override fun buildOAPageIri(collectionId: String, page: Int, embeddedDescriptions: Boolean): String {
        val params = object : HashMap<String, Any>() {
            init {
                put(PARAM_PAGE, page)
                if (embeddedDescriptions) {
                    put(PARAM_DESC, 1)
                } else {
                    put(PARAM_IRIS, 1)
                }
            }
        }
        return buildIri("oa/$collectionId/", params)
    }

    override fun buildOACollectionBodySearchIri(
        fields: List<String>,
        value: String,
        strict: Boolean?,
        xywh: String?,
        t: String?,
        creatorIri: String?,
        generatorIri: String?
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
            }
        }
        return buildIri("oa/services/search/body", params)
    }

    override fun buildOAPageBodySearchIri(
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
        return buildIri("oa/services/search/body", params)
    }

    override fun buildOACollectionTargetSearchIri(
        fields: List<String>,
        value: String,
        strict: Boolean?,
        xywh: String?,
        t: String?,
        creatorIri: String?,
        generatorIri: String?
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
            }
        }
        return buildIri("oa/services/search/target", params)
    }

    override fun buildOAPageTargetSearchIri(
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
        return buildIri("oa/services/search/target", params)
    }

    override fun buildOACollectionCreatorSearchIri(
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
        return buildIri("oa/services/search/creator", params)
    }

    override fun buildOAPageCreatorSearchIri(
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
        return buildIri("oa/services/search/creator", params)
    }

    override fun buildOACollectionGeneratorSearchIri(
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
        return buildIri("oa/services/search/generator", params)
    }

    override fun buildOAPageGeneratorSearchIri(
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
        return buildIri("oa/services/search/generator", params)
    }

    override fun buildOAStatisticsPageIri(type: String, field: String, page: Int): String {
        val params = object : HashMap<String, Any>() {
            init {
                put(PARAM_FIELD, field)
                put(PARAM_PAGE, page)
            }
        }
        return buildIri("oa/services/stats/$type", params)
    }

    override fun buildOAAnnotationHistoryIri(collectionId: String, annotationId: String, version: Int): String =
        buildIri("oa/services/history/$collectionId/$annotationId/$version")

    override fun buildOACollectionTemporalSearchIri(
        levels: List<String>,
        types: List<String>,
        since: Date
    ): String {
        val params = object : HashMap<String, Any>() {
            init {
                put(PARAM_LEVELS, StringUtils.join(levels, ","))
                put(PARAM_TYPES, StringUtils.join(types, ","))
                put(PARAM_SINCE, toString(since))
            }
        }
        return buildIri("oa/services/search/temporal", params)
    }

    override fun buildOAPageTemporalSearchIri(
        levels: List<String>,
        types: List<String>,
        since: Date,
        page: Int,
        embeddedDescriptions: Boolean
    ): String {
        val params = object : HashMap<String, Any>() {
            init {
                put(PARAM_LEVELS, StringUtils.join(levels, ","))
                put(PARAM_TYPES, StringUtils.join(types, ","))
                put(PARAM_SINCE, toString(since))
                put(PARAM_PAGE, page)
                if (embeddedDescriptions) {
                    put(PARAM_DESC, 1)
                } else {
                    put(PARAM_IRIS, 1)
                }
            }
        }
        return buildIri("oa/services/search/temporal", params)
    }

    override fun buildOACollectionOverlapSearchIri(targetId: String, rangeStart: Int, rangeEnd: Int): String {
        val params = mapOf(
            PARAM_TARGET_ID to targetId,
            PARAM_RANGE_START to rangeStart,
            PARAM_RANGE_END to rangeEnd
        )
        return buildIri("oa/services/search/overlap", params)
    }

    override fun buildOAPageOverlapSearchIri(
        targetId: String,
        rangeStart: Int,
        rangeEnd: Int,
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
                PARAM_LEVELS to StringUtils.join(levels, ","),
                PARAM_TYPES to StringUtils.join(types, ","),
                PARAM_SINCE to toString(since)
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