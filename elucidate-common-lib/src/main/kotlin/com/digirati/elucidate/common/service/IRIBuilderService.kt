package com.digirati.elucidate.common.service

import com.digirati.elucidate.common.model.annotation.AnnotationFormat
import java.util.*

interface IRIBuilderService {

    fun buildCollectionIri(format: AnnotationFormat, collectionId: String): String

    fun buildCollectionPageIri(
        format: AnnotationFormat,
        collectionId: String,
        page: Int,
        embeddedDescriptions: Boolean
    ): String

    fun buildAnnotationIri(format: AnnotationFormat, collectionId: String, annotationId: String): String

    fun buildAnnotationHistoryIri(
        format: AnnotationFormat,
        collectionId: String,
        annotationId: String,
        version: Int
    ): String

    fun buildSearchByBodyIri(
        format: AnnotationFormat,
        fields: List<String>,
        value: String,
        strict: Boolean?,
        xywh: String?,
        t: String?,
        creatorIri: String?,
        generatorIri: String?
    ): String

    fun buildSearchByBodyPageIri(
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
    ): String

    fun buildSearchByTargetIri(
        format: AnnotationFormat,
        fields: List<String>,
        value: String,
        strict: Boolean?,
        xywh: String?,
        t: String?,
        creatorIri: String?,
        generatorIri: String?
    ): String

    fun buildSearchByTargetPageIri(
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
    ): String

    fun buildSearchByCreatorIri(
        format: AnnotationFormat,
        levels: List<String>,
        type: String,
        value: String,
        strict: Boolean?
    ): String

    fun buildSearchByCreatorPageIri(
        format: AnnotationFormat,
        levels: List<String>,
        type: String,
        value: String,
        strict: Boolean?,
        page: Int,
        embeddedDescriptions: Boolean
    ): String

    fun buildSearchByGeneratorIri(
        format: AnnotationFormat,
        levels: List<String>,
        type: String,
        value: String,
        strict: Boolean?
    ): String

    fun buildSearchByGeneratorPageIri(
        format: AnnotationFormat,
        levels: List<String>,
        type: String,
        value: String,
        strict: Boolean?,
        page: Int,
        embeddedDescriptions: Boolean
    ): String

    fun buildSearchByRangeIri(format: AnnotationFormat, targetId: String, rangeStart: Int, rangeEnd: Int): String

    fun buildSearchByRangePageIri(
        format: AnnotationFormat,
        targetId: String,
        rangeStart: Int,
        rangeEnd: Int,
        page: Int,
        embeddedDescriptions: Boolean
    ): String

    fun buildSearchByOverlapIri(format: AnnotationFormat, targetId: String, rangeStart: Int, rangeEnd: Int): String

    fun buildSearchByOverlapPageIri(
        format: AnnotationFormat,
        targetId: String,
        rangeStart: Int,
        rangeEnd: Int,
        page: Int,
        embeddedDescriptions: Boolean
    ): String

    fun buildSearchByTemporalIri(
        format: AnnotationFormat,
        levels: List<String>,
        types: List<String>,
        since: Date
    ): String

    fun buildSearchByTemporalPageIri(
        format: AnnotationFormat,
        levels: List<String>,
        types: List<String>,
        since: Date,
        page: Int,
        embeddedDescriptions: Boolean
    ): String

    fun buildStatisticsPageIri(format: AnnotationFormat, type: String, field: String, page: Int): String
}