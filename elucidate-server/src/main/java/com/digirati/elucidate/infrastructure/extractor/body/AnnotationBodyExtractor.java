package com.digirati.elucidate.infrastructure.extractor.body;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.digirati.elucidate.common.infrastructure.constants.JSONLDConstants;
import com.digirati.elucidate.common.infrastructure.constants.OAConstants;
import com.digirati.elucidate.model.annotation.body.AnnotationBody;

public class AnnotationBodyExtractor {

    @NotNull
    @SuppressWarnings("unchecked")
    public List<AnnotationBody> extractBodies(@NotNull Map<String, Object> jsonMap) {

        List<AnnotationBody> annotationBodies = new ArrayList<>();

        List<Map<String, Object>> bodyJsonMaps = (List<Map<String, Object>>) jsonMap.get(OAConstants.URI_HAS_BODY);
        if (bodyJsonMaps != null && !bodyJsonMaps.isEmpty()) {

            for (Map<String, Object> bodyJsonMap : bodyJsonMaps) {

                AnnotationBody annotationBody = new AnnotationBody();
                annotationBody.setJsonMap(bodyJsonMap);

                String bodyIri = (String) bodyJsonMap.get(JSONLDConstants.ATTRIBUTE_ID);
                annotationBody.setBodyIri(stripFragment(bodyIri));

                List<Map<String, Object>> sources = (List<Map<String, Object>>) bodyJsonMap.get(OAConstants.URI_HAS_SOURCE);
                if (sources != null && sources.size() == 1) {

                    String sourceIri = (String) sources.get(0).get(JSONLDConstants.ATTRIBUTE_ID);
                    annotationBody.setSourceIri(sourceIri);
                }

                annotationBodies.add(annotationBody);
            }
        }

        return annotationBodies;
    }

    @Nullable
    private String stripFragment(@NotNull String bodyIriStr) {
        if (StringUtils.isNotBlank(bodyIriStr)) {
            try {
                URI bodyIri = new URI(bodyIriStr);
                return new URI(bodyIri.getScheme(), bodyIri.getUserInfo(), bodyIri.getHost(), bodyIri.getPort(), bodyIri.getPath(), bodyIri.getQuery(), null).toString();
            } catch (URISyntaxException e) {
                return bodyIriStr;
            }
        }

        return null;
    }
}
