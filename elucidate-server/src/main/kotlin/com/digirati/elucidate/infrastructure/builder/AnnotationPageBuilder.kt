package com.digirati.elucidate.infrastructure.builder

import com.digirati.elucidate.common.infrastructure.constants.ActivityStreamConstants
import com.digirati.elucidate.common.infrastructure.constants.JSONLDConstants
import com.digirati.elucidate.common.infrastructure.constants.XMLSchemaConstants
import com.digirati.elucidate.common.infrastructure.util.PaginationUtils.calculateLastPage
import com.digirati.elucidate.common.model.annotation.AbstractAnnotation
import com.digirati.elucidate.common.model.annotation.AbstractAnnotationPage
import com.digirati.elucidate.infrastructure.builder.function.AnnotationCollectionIRIBuilder
import com.digirati.elucidate.infrastructure.builder.function.AnnotationPageConverter
import com.digirati.elucidate.infrastructure.builder.function.AnnotationPageIRIBuilder
import com.digirati.elucidate.model.ServiceResponse
import kotlin.math.max
import kotlin.math.min

class AnnotationPageBuilder<A : AbstractAnnotation, P : AbstractAnnotationPage>(
    private val annotationPageConverter: AnnotationPageConverter<P>,
    private val annotationCollectionIriBuilder: AnnotationCollectionIRIBuilder,
    private val annotationPageIriBuilder: AnnotationPageIRIBuilder
) {
    fun buildAnnotationPage(
        annotations: List<A>,
        page: Int,
        embeddedDescriptions: Boolean,
        pageSize: Int
    ): ServiceResponse<P> {
        var annotationSelection = annotations
        val lastPage = calculateLastPage(annotationSelection.size, pageSize)
        val from = min(annotationSelection.size, max(0, page * pageSize))
        val to = min(annotationSelection.size, (page + 1) * pageSize)
        annotationSelection = annotationSelection.subList(from, to)
        val jsonMap: MutableMap<String, Any> = HashMap()
        jsonMap[JSONLDConstants.ATTRIBUTE_TYPE] = object : ArrayList<String>() {
            init {
                add(ActivityStreamConstants.URI_ORDERED_COLLECTION_PAGE)
            }
        }
        val partOfIri = annotationCollectionIriBuilder.buildAnnotationCollectionIri()
        jsonMap[ActivityStreamConstants.URI_PART_OF] =
            object : ArrayList<Map<String, Any>>() {
                init {
                    add(object : HashMap<String, Any>() {
                        init {
                            put(JSONLDConstants.ATTRIBUTE_ID, partOfIri)
                        }
                    })
                }
            }
        jsonMap[ActivityStreamConstants.URI_START_INDEX] =
            object : ArrayList<Map<String, Any>>() {
                init {
                    add(object : HashMap<String, Any>() {
                        init {
                            put(JSONLDConstants.ATTRIBUTE_TYPE, XMLSchemaConstants.URI_NON_NEGATIVE_INTEGER)
                            put(JSONLDConstants.ATTRIBUTE_VALUE, from)
                        }
                    })
                }
            }
        if (page > 0) {
            val prevIri = annotationPageIriBuilder.buildAnnotationPageIri(page - 1, embeddedDescriptions)
            jsonMap[ActivityStreamConstants.URI_PREV] = object : ArrayList<Map<String, Any>>() {
                init {
                    add(object : HashMap<String, Any>() {
                        init {
                            put(JSONLDConstants.ATTRIBUTE_ID, prevIri)
                        }
                    })
                }
            }
        }
        if (page < lastPage) {
            val nextIri = annotationPageIriBuilder.buildAnnotationPageIri(page + 1, embeddedDescriptions)
            jsonMap[ActivityStreamConstants.URI_NEXT] =
                object : ArrayList<Map<String, Any>>() {
                    init {
                        add(object : HashMap<String, Any>() {
                            init {
                                put(JSONLDConstants.ATTRIBUTE_ID, nextIri)
                            }
                        })
                    }
                }
        }
        if (embeddedDescriptions) {
            val annotationDescriptions = convertToDescriptions(annotationSelection)
            jsonMap[ActivityStreamConstants.URI_ITEMS] = object : ArrayList<Map<String, Any>>() {
                init {
                    add(object : HashMap<String, Any>() {
                        init {
                            put(
                                JSONLDConstants.ATTRIBUTE_LIST,
                                object : ArrayList<Map<String, Any>>() {
                                    init {
                                        addAll(annotationDescriptions)
                                    }
                                })
                        }
                    })
                }
            }
        } else {
            val annotationIris = convertToIris(annotationSelection)
            jsonMap[ActivityStreamConstants.URI_ITEMS] = object : ArrayList<Map<String, Any>>() {
                init {
                    add(object : HashMap<String, Any>() {
                        init {
                            put(
                                JSONLDConstants.ATTRIBUTE_LIST,
                                object : ArrayList<Map<String, Any>>() {
                                    init {
                                        addAll(annotationIris)
                                    }
                                })
                        }
                    })
                }
            }
        }
        val annotationPage = annotationPageConverter.convertToAnnotationPage(jsonMap.toMutableMap())
        return ServiceResponse(ServiceResponse.Status.OK, annotationPage)
    }

    private fun convertToDescriptions(annotations: List<A>): List<Map<String, Any>> {
        val descriptions: MutableList<Map<String, Any>> = ArrayList()
        for (annotation in annotations) {
            descriptions.add(annotation.jsonMap!!)
        }
        return descriptions
    }

    private fun convertToIris(annotations: List<A>): List<Map<String, Any>> {
        return annotations.map { a -> mapOf(JSONLDConstants.ATTRIBUTE_ID to a.jsonMap!![JSONLDConstants.ATTRIBUTE_ID]!!) }
//        val iris: MutableList<Map<String, Any>> = ArrayList()
//        for (annotation in annotations) {
//            iris.add(mapOf(JSONLDConstants.ATTRIBUTE_ID to annotation.jsonMap!![JSONLDConstants.ATTRIBUTE_ID]!!))
//        }
//        return iris
    }
}