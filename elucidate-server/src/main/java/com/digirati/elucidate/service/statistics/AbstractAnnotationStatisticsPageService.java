package com.digirati.elucidate.service.statistics;

import org.jetbrains.annotations.Nullable;

import com.digirati.elucidate.model.ServiceResponse;
import com.digirati.elucidate.model.statistics.AbstractStatisticsPage;

public interface AbstractAnnotationStatisticsPageService<S extends AbstractStatisticsPage> {

    @Nullable
    ServiceResponse<S> buildBodyStatisticsPage(String field, int page);

    @Nullable
    ServiceResponse<S> buildTargetStatisticsPage(String field, int page);
}
