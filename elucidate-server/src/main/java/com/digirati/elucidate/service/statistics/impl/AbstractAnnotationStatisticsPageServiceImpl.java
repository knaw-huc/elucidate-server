package com.digirati.elucidate.service.statistics.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.digirati.elucidate.common.infrastructure.constants.ActivityStreamConstants;
import com.digirati.elucidate.common.infrastructure.constants.JSONLDConstants;
import com.digirati.elucidate.common.infrastructure.constants.RDFConstants;
import com.digirati.elucidate.common.infrastructure.constants.SearchConstants;
import com.digirati.elucidate.common.infrastructure.constants.URLConstants;
import com.digirati.elucidate.common.infrastructure.constants.XMLSchemaConstants;
import com.digirati.elucidate.common.infrastructure.util.PaginationUtils;
import com.digirati.elucidate.model.ServiceResponse;
import com.digirati.elucidate.model.ServiceResponse.Status;
import com.digirati.elucidate.model.statistics.AbstractStatisticsPage;
import com.digirati.elucidate.repository.AnnotationStatisticsRepository;
import com.digirati.elucidate.service.statistics.AbstractAnnotationStatisticsPageService;

public abstract class AbstractAnnotationStatisticsPageServiceImpl<S extends AbstractStatisticsPage> implements AbstractAnnotationStatisticsPageService<S> {

    protected final Logger LOGGER = Logger.getLogger(getClass());

    private final AnnotationStatisticsRepository statisticsRepository;
    private final int pageSize;

    @Autowired
    protected AbstractAnnotationStatisticsPageServiceImpl(AnnotationStatisticsRepository statisticsRepository, int pageSize) {
        this.statisticsRepository = statisticsRepository;
        this.pageSize = pageSize;
    }

    protected abstract S convertToStatisticsPage(Map<String, Object> jsonMap);

    protected abstract String buildPageIri(String type, String field, int page);

    @Override
    public ServiceResponse<S> buildBodyStatisticsPage(String field, int page) {

        List<Pair<String, Integer>> counts;

        switch (field) {
            case SearchConstants.FIELD_ID:
                counts = statisticsRepository.getBodyIdCounts();
                break;
            case SearchConstants.FIELD_SOURCE:
                counts = statisticsRepository.getBodySourceCounts();
                break;
            default:
                LOGGER.warn(String.format("Invalid search parameter [%s] with value [%s] - one of [%s] or [%s] must be provided", URLConstants.PARAM_FIELD, field, SearchConstants.FIELD_ID, SearchConstants.FIELD_SOURCE));
                return new ServiceResponse<>(Status.NON_CONFORMANT, null);
        }

        return buildStatisticsPage("body", counts, field, page);
    }

    @Override
    public ServiceResponse<S> buildTargetStatisticsPage(String field, int page) {

        List<Pair<String, Integer>> counts;

        switch (field) {
            case SearchConstants.FIELD_ID:
                counts = statisticsRepository.getTargetIdCounts();
                break;
            case SearchConstants.FIELD_SOURCE:
                counts = statisticsRepository.getTargetSourceCounts();
                break;
            default:
                LOGGER.warn(String.format("Invalid search parameter [%s] with value [%s] - one of [%s] or [%s] must be provided", URLConstants.PARAM_FIELD, field, SearchConstants.FIELD_ID, SearchConstants.FIELD_SOURCE));
                return new ServiceResponse<>(Status.NON_CONFORMANT, null);
        }

        return buildStatisticsPage("target", counts, field, page);
    }

    @SuppressWarnings("serial")
    public ServiceResponse<S> buildStatisticsPage(String type, List<Pair<String, Integer>> counts, String field, int page) {

        int lastPage = PaginationUtils.calculateLastPage(counts.size(), pageSize);
        int from = Math.min(counts.size(), Math.max(0, page * pageSize));
        int to = Math.min(counts.size(), (page + 1) * pageSize);
        counts = counts.subList(from, to);

        Map<String, Object> jsonMap = new HashMap<>();

        jsonMap.put(ActivityStreamConstants.URI_START_INDEX, new ArrayList<Map<String, Object>>() {
            {
                add(new HashMap<String, Object>() {
                    {
                        put(JSONLDConstants.ATTRIBUTE_TYPE, XMLSchemaConstants.URI_NON_NEGATIVE_INTEGER);
                        put(JSONLDConstants.ATTRIBUTE_VALUE, from);
                    }
                });
            }
        });

        if (page > 0) {
            String prevIri = buildPageIri(type, field, page - 1);
            jsonMap.put(ActivityStreamConstants.URI_PREV, new ArrayList<Map<String, Object>>() {
                {
                    add(new HashMap<String, Object>() {
                        {
                            put(JSONLDConstants.ATTRIBUTE_ID, prevIri);
                        }
                    });
                }
            });
        }

        if (page < lastPage) {
            String nextIri = buildPageIri(type, field, page + 1);
            jsonMap.put(ActivityStreamConstants.URI_NEXT, new ArrayList<Map<String, Object>>() {
                {
                    add(new HashMap<String, Object>() {
                        {
                            put(JSONLDConstants.ATTRIBUTE_ID, nextIri);
                        }
                    });
                }
            });
        }

        List<Map<String, Object>> jsonMaps = convertToJsonMaps(counts);
        jsonMap.put(ActivityStreamConstants.URI_ITEMS, new ArrayList<Map<String, Object>>() {
            {
                add(new HashMap<String, Object>() {
                    {
                        put(JSONLDConstants.ATTRIBUTE_LIST, new ArrayList<Map<String, Object>>() {
                            {
                                addAll(jsonMaps);
                            }
                        });
                    }
                });
            }
        });

        S statisticsPage = convertToStatisticsPage(jsonMap);
        return new ServiceResponse<>(Status.OK, statisticsPage);
    }

    @SuppressWarnings("serial")
    private List<Map<String, Object>> convertToJsonMaps(List<Pair<String, Integer>> counts) {
        List<Map<String, Object>> jsonMaps = new ArrayList<>();
        for (Pair<String, Integer> count : counts) {
            jsonMaps.add(new HashMap<String, Object>() {
                {
                    put(RDFConstants.URI_VALUE, new ArrayList<Map<String, Object>>() {
                        {
                            add(new HashMap<String, Object>() {
                                {
                                    put(JSONLDConstants.ATTRIBUTE_VALUE, count.getKey());
                                }
                            });
                        }
                    });

                    put(ActivityStreamConstants.URI_TOTAL_ITEMS, new ArrayList<Map<String, Object>>() {
                        {
                            add(new HashMap<String, Object>() {
                                {
                                    put(JSONLDConstants.ATTRIBUTE_TYPE, XMLSchemaConstants.URI_NON_NEGATIVE_INTEGER);
                                    put(JSONLDConstants.ATTRIBUTE_VALUE, count.getValue());
                                }
                            });
                        }
                    });
                }
            });
        }
        return jsonMaps;
    }
}
