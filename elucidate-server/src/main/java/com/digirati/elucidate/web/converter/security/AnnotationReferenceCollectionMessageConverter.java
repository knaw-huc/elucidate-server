package com.digirati.elucidate.web.converter.security;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.github.jsonldjava.utils.JsonUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.digirati.elucidate.common.service.IRIBuilderService;
import com.digirati.elucidate.model.annotation.AnnotationReferenceCollection;
import com.digirati.elucidate.web.converter.AbstractMessageConverter;

@Component
public class AnnotationReferenceCollectionMessageConverter extends
        AbstractMessageConverter<AnnotationReferenceCollection> {

    private final IRIBuilderService iriBuilder;

    @Autowired
    public AnnotationReferenceCollectionMessageConverter(IRIBuilderService iriBuilder) {
        super(MediaType.APPLICATION_JSON);
        this.iriBuilder = iriBuilder;
    }

    @Override
    protected void decorateHeaders(AnnotationReferenceCollection obj, @NotNull HttpOutputMessage outputMessage) {
        HttpHeaders headers = outputMessage.getHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(HttpHeaders.ALLOW, "GET,OPTIONS,HEAD");
    }

    @Override
    protected String getStringRepresentation(@NotNull AnnotationReferenceCollection obj, MediaType contentType)
            throws Exception {
        Map<String, Object> jsonMap = new HashMap<>();
        List<String> iris = obj.getAnnotations().stream()
                .map(ref -> iriBuilder.buildW3CAnnotationIri(ref.getCollectionId(), ref.getId()))
                .collect(Collectors.toList());

        jsonMap.put("annotations", iris);
        return JsonUtils.toString(jsonMap);
    }

    @NotNull
    @Override
    protected AnnotationReferenceCollection getObjectRepresentation(String str, MediaType contentType) {
        throw new UnsupportedOperationException("AnnotatationReferenceCollection is not deserializable");
    }

    @Override
    protected boolean supports(@NotNull Class<?> clazz) {
        return clazz.isAssignableFrom(AnnotationReferenceCollection.class);
    }
}
