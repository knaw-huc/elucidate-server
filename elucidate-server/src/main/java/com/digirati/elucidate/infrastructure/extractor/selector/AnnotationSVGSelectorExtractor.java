package com.digirati.elucidate.infrastructure.extractor.selector;

import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

import com.digirati.elucidate.common.infrastructure.constants.JSONLDConstants;
import com.digirati.elucidate.common.infrastructure.constants.OAConstants;
import com.digirati.elucidate.common.infrastructure.constants.RDFConstants;
import com.digirati.elucidate.model.annotation.selector.svg.AnnotationSVGSelector;

public class AnnotationSVGSelectorExtractor extends AbstractAnnotationSelectorExtractor<AnnotationSVGSelector> {

    @NotNull
    @Override
    protected String getSelectorType() {
        return OAConstants.URI_SVG_SELECTOR;
    }

    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    protected AnnotationSVGSelector buildSelector(@NotNull Map<String, Object> jsonMap) {

        AnnotationSVGSelector annotationSvgSelector = new AnnotationSVGSelector();

        List<Map<String, Object>> values = (List<Map<String, Object>>) jsonMap.get(RDFConstants.URI_VALUE);
        if (values != null && values.size() == 1) {
            Map<String, Object> valueMap = values.get(0);
            annotationSvgSelector.setValue((String) valueMap.get(JSONLDConstants.ATTRIBUTE_VALUE));
        }

        return annotationSvgSelector;
    }
}
