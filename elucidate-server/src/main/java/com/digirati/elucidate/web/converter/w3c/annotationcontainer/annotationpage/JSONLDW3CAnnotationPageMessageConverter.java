package com.digirati.elucidate.web.converter.w3c.annotationcontainer.annotationpage;

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

import com.digirati.elucidate.common.model.annotation.w3c.W3CAnnotationPage;
import com.digirati.elucidate.model.JSONLDProfile;
import com.digirati.elucidate.model.JSONLDProfile.Format;

@Component
public class JSONLDW3CAnnotationPageMessageConverter extends AbstractW3CAnnotationPageMessageConverter {

    @NotNull
    private final String[] defaultContexts;

    @Autowired
    public JSONLDW3CAnnotationPageMessageConverter(@NotNull @Value("${annotation.page.w3c.contexts}") String[] defaultContexts) {
        super(APPLICATION_JSON_LD);
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
    protected String getStringRepresentation(@NotNull W3CAnnotationPage w3cAnnotationPage, @NotNull MediaType contentType) throws Exception {
        Map<String, Object> jsonMap = w3cAnnotationPage.getJsonMap();

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
    protected W3CAnnotationPage getObjectRepresentation(String str, MediaType contentType) {
        throw new UnsupportedOperationException(String.format("Conversion from Content Type [%s] to [%s] is not supported", contentType, W3CAnnotationPage.class));
    }
}
