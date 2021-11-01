package com.digirati.elucidate.web.converter.oa.annotation;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.github.jsonldjava.core.JsonLdProcessor;
import com.github.jsonldjava.utils.JsonUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpMediaTypeNotSupportedException;

import com.digirati.elucidate.common.model.annotation.oa.OAAnnotation;
import com.digirati.elucidate.common.service.IRIBuilderService;
import com.digirati.elucidate.model.JSONLDProfile;
import com.digirati.elucidate.model.JSONLDProfile.Format;
import com.digirati.elucidate.service.history.OAAnnotationHistoryService;

@Component
public class JSONLDOAAnnotationMessageConverter extends AbstractOAAnnotationMessageConverter {

    @NotNull
    private final String[] defaultContexts;

    @Autowired
    public JSONLDOAAnnotationMessageConverter(IRIBuilderService iriBuilderService, OAAnnotationHistoryService oaAnnotationHistoryService, @NotNull @Value("${annotation.oa.contexts}") String[] defaultContexts) {
        super(iriBuilderService, oaAnnotationHistoryService, APPLICATION_JSON_LD);
        this.defaultContexts = Arrays.copyOf(defaultContexts, defaultContexts.length);
    }

    @Override
    protected boolean canRead(@Nullable MediaType mediaType) {
        if (mediaType == null) {
            return true;
        }
        for (MediaType supportedMediaType : getSupportedMediaTypes()) {
            if (supportedMediaType.isCompatibleWith(mediaType)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected boolean canWrite(@Nullable MediaType mediaType) {
        if (mediaType == null || MediaType.ALL.equals(mediaType)) {
            return true;
        }
        for (MediaType supportedMediaType : getSupportedMediaTypes()) {
            if (supportedMediaType.isCompatibleWith(mediaType)) {
                return true;
            }
        }
        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected String getStringRepresentation(@NotNull OAAnnotation oaAnnotation, @NotNull MediaType contentType) throws Exception {
        Map<String, Object> jsonMap = oaAnnotation.getJsonMap();

        JSONLDProfile jsonLdProfile = getJsonLdProfile(contentType, defaultContexts);

        Format format = jsonLdProfile.getFormats().get(0);
        if (format.equals(Format.COMPACTED)) {
            jsonMap = JsonLdProcessor.compact(jsonMap, jsonLdProfile.getContexts(), jsonLdOptions);
        } else if (format.equals(Format.EXPANDED)) {
            List<Object> jsonList = JsonLdProcessor.expand(jsonMap, jsonLdOptions);
            jsonMap = (Map<String, Object>) jsonList.get(0);
        } else if (format.equals(Format.FLATTENED)) {
            jsonMap = (Map<String, Object>) JsonLdProcessor.flatten(jsonMap, jsonLdProfile.getContexts(), jsonLdOptions);
        } else {
            throw new HttpMediaTypeNotSupportedException(contentType, getSupportedMediaTypes());
        }

        jsonMap = reorderJsonAttributes(jsonMap);
        return JsonUtils.toPrettyString(jsonMap);
    }

    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    protected OAAnnotation getObjectRepresentation(@NotNull String jsonStr, MediaType contentType) throws Exception {
        Map<String, Object> jsonMap = (Map<String, Object>) JsonUtils.fromString(jsonStr);
        List<Object> jsonList = JsonLdProcessor.expand(jsonMap, jsonLdOptions);
        jsonMap = (Map<String, Object>) jsonList.get(0);

        OAAnnotation oaAnnotation = new OAAnnotation();
        oaAnnotation.setJsonMap(jsonMap);
        return oaAnnotation;
    }
}
