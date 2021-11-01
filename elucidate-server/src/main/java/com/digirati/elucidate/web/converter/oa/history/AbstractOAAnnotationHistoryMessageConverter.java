package com.digirati.elucidate.web.converter.oa.history;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;

import com.digirati.elucidate.common.service.IRIBuilderService;
import com.digirati.elucidate.model.ServiceResponse;
import com.digirati.elucidate.model.ServiceResponse.Status;
import com.digirati.elucidate.model.annotation.history.OAAnnotationHistory;
import com.digirati.elucidate.service.history.OAAnnotationHistoryService;
import com.digirati.elucidate.web.converter.AbstractMessageConverter;

public abstract class AbstractOAAnnotationHistoryMessageConverter extends AbstractMessageConverter<OAAnnotationHistory> {

    private final IRIBuilderService iriBuilderService;
    private final OAAnnotationHistoryService oaAnnotationHistoryService;

    protected AbstractOAAnnotationHistoryMessageConverter(IRIBuilderService iriBuilderService, OAAnnotationHistoryService oaAnnotationHistoryService, MediaType... supportedMediaTypes) {
        super(supportedMediaTypes);
        this.iriBuilderService = iriBuilderService;
        this.oaAnnotationHistoryService = oaAnnotationHistoryService;
    }

    @Override
    protected boolean supports(@NotNull Class<?> clazz) {
        return OAAnnotationHistory.class.equals(clazz);
    }

    @Override
    protected void decorateHeaders(@NotNull OAAnnotationHistory oaAnnotationHistory, @NotNull HttpOutputMessage outputMessage) {
        decorateMementoDatetimeHeader(oaAnnotationHistory, outputMessage);
        decorateNextVersionHeader(oaAnnotationHistory, outputMessage);
        decoratePreviousVersionHeader(oaAnnotationHistory, outputMessage);
    }

    private void decorateMementoDatetimeHeader(@NotNull OAAnnotationHistory oaAnnotationHistory, @NotNull HttpOutputMessage outputMessage) {
        String dateStr = MEMENTO_DATE_FORMAT.format(oaAnnotationHistory.getCreatedDateTime());
        outputMessage.getHeaders().add("Memento-Datetime", dateStr);
    }

    private void decorateNextVersionHeader(@NotNull OAAnnotationHistory oaAnnotationHistory, @NotNull HttpOutputMessage outputMessage) {

        String collectionId = oaAnnotationHistory.getCollectionId();
        String annotationId = oaAnnotationHistory.getAnnotationId();
        int version = oaAnnotationHistory.getVersion();

        ServiceResponse<OAAnnotationHistory> serviceResponse = oaAnnotationHistoryService.getNextAnnotationVersion(collectionId, annotationId, version);
        Status status = serviceResponse.getStatus();

        if (status.equals(Status.OK)) {
            oaAnnotationHistory = serviceResponse.getObj();
            String urlStr = iriBuilderService.buildOAAnnotationHistoryIri(collectionId, annotationId, oaAnnotationHistory.getVersion());
            String dateStr = MEMENTO_DATE_FORMAT.format(oaAnnotationHistory.getCreatedDateTime());
            outputMessage.getHeaders().add(HttpHeaders.LINK, String.format("<%s>; rel=\"next memento\"; datetime=\"%s\"", urlStr, dateStr));
        }
    }

    private void decoratePreviousVersionHeader(@NotNull OAAnnotationHistory oaAnnotationHistory, @NotNull HttpOutputMessage outputMessage) {

        String collectionId = oaAnnotationHistory.getCollectionId();
        String annotationId = oaAnnotationHistory.getAnnotationId();
        int version = oaAnnotationHistory.getVersion();

        ServiceResponse<OAAnnotationHistory> serviceResponse = oaAnnotationHistoryService.getPreviousAnnotationVersion(collectionId, annotationId, version);
        Status status = serviceResponse.getStatus();

        if (status.equals(Status.OK)) {
            oaAnnotationHistory = serviceResponse.getObj();
            String urlStr = iriBuilderService.buildOAAnnotationHistoryIri(collectionId, annotationId, oaAnnotationHistory.getVersion());
            String dateStr = MEMENTO_DATE_FORMAT.format(oaAnnotationHistory.getCreatedDateTime());
            outputMessage.getHeaders().add(HttpHeaders.LINK, String.format("<%s>; rel=\"prev memento\"; datetime=\"%s\"", urlStr, dateStr));
        }
    }
}
