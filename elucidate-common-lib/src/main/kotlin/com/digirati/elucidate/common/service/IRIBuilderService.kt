package com.digirati.elucidate.common.service

import com.digirati.elucidate.common.model.annotation.AnnotationFormat
import java.util.*

interface IRIBuilderService {
    fun buildAnnotationIri(format: AnnotationFormat, collectionId: String, annotationId: String): String

    fun buildOAAnnotationHistoryIri(collectionId: String, annotationId: String, version: Int): String

    fun buildOACollectionBodySearchIri(
        fields: List<String>,
        value: String,
        strict: Boolean?,
        xywh: String?,
        t: String?,
        creatorIri: String?,
        generatorIri: String?
    ): String

    fun buildOACollectionCreatorSearchIri(
        levels: List<String>,
        type: String,
        value: String,
        strict: Boolean?
    ): String

    fun buildOACollectionGeneratorSearchIri(
        levels: List<String>,
        type: String,
        value: String,
        strict: Boolean?
    ): String

    fun buildOACollectionIri(collectionId: String): String

    fun buildOACollectionOverlapSearchIri(targetId: String, rangeStart: Int, rangeEnd: Int): String

    fun buildOACollectionTargetSearchIri(
        fields: List<String>,
        value: String,
        strict: Boolean?,
        xywh: String?,
        t: String?,
        creatorIri: String?,
        generatorIri: String?
    ): String

    fun buildOACollectionTemporalSearchIri(levels: List<String>, types: List<String>, since: Date): String

    fun buildOAPageBodySearchIri(
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

    fun buildOAPageCreatorSearchIri(
        levels: List<String>,
        type: String,
        value: String,
        strict: Boolean?,
        page: Int,
        embeddedDescriptions: Boolean
    ): String

    fun buildOAPageGeneratorSearchIri(
        levels: List<String>,
        type: String,
        value: String,
        strict: Boolean?,
        page: Int,
        embeddedDescriptions: Boolean
    ): String

    fun buildOAPageIri(collectionId: String, page: Int, embeddedDescriptions: Boolean): String

    fun buildOAPageOverlapSearchIri(
        targetId: String,
        rangeStart: Int,
        rangeEnd: Int,
        page: Int,
        embeddedDescriptions: Boolean
    ): String

    fun buildOAPageTargetSearchIri(
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

    fun buildOAPageTemporalSearchIri(
        levels: List<String>,
        types: List<String>,
        since: Date,
        page: Int,
        embeddedDescriptions: Boolean
    ): String

    fun buildOAStatisticsPageIri(type: String, field: String, page: Int): String

    fun buildW3CAnnotationHistoryIri(collectionId: String, annotationId: String, version: Int): String

    fun buildW3CCollectionBodySearchIri(
        fields: List<String>,
        value: String,
        strict: Boolean?,
        xywh: String?,
        t: String?,
        creatorIri: String?,
        generatorIri: String?
    ): String

    fun buildW3CCollectionCreatorSearchIri(
        levels: List<String>,
        type: String,
        value: String,
        strict: Boolean?
    ): String

    fun buildW3CCollectionGeneratorSearchIri(
        levels: List<String>,
        type: String,
        value: String,
        strict: Boolean?
    ): String

    fun buildW3CCollectionIri(collectionId: String): String

    fun buildW3CCollectionTargetSearchIri(
        fields: List<String>,
        value: String,
        strict: Boolean?,
        xywh: String?,
        t: String?,
        creatorIri: String?,
        generatorIri: String?
    ): String

    fun buildW3CCollectionTemporalSearchIri(levels: List<String>, types: List<String>, since: Date): String

    fun buildW3CPageBodySearchIri(
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

    fun buildW3CPageCreatorSearchIri(
        levels: List<String>,
        type: String,
        value: String,
        strict: Boolean?,
        page: Int,
        embeddedDescriptions: Boolean
    ): String

    fun buildW3CPageGeneratorSearchIri(
        levels: List<String>,
        type: String,
        value: String,
        strict: Boolean?,
        page: Int,
        embeddedDescriptions: Boolean
    ): String

    fun buildW3CPageIri(collectionId: String, page: Int, embeddedDescriptions: Boolean): String

    fun buildW3CPageTargetSearchIri(
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

    fun buildW3CPageTemporalSearchIri(
        levels: List<String>,
        types: List<String>,
        since: Date,
        page: Int,
        embeddedDescriptions: Boolean
    ): String

    fun buildW3CStatisticsPageIri(type: String, field: String, page: Int): String

    fun buildW3CCollectionOverlapSearchIri(targetId: String, rangeStart: Int, rangeEnd: Int): String

    fun buildW3CPageOverlapSearchIri(
        targetId: String,
        rangeStart: Int,
        rangeEnd: Int,
        page: Int,
        embeddedDescriptions: Boolean
    ): String

    fun buildW3CCollectionRangeSearchIri(targetId: String, rangeStart: Int, rangeEnd: Int): String

    fun buildW3CPageRangeSearchIri(
        targetId: String,
        rangeStart: Int,
        rangeEnd: Int,
        page: Int,
        embeddedDescriptions: Boolean
    ): String

    fun buildOACollectionRangeSearchIri(targetId: String, rangeStart: Int, rangeEnd: Int): String

    fun buildOAPageRangeSearchIri(
        targetId: String,
        rangeStart: Int,
        rangeEnd: Int,
        page: Int,
        embeddedDescriptions: Boolean
    ): String
}