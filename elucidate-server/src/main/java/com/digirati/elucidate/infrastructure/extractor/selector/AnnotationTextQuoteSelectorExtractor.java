package com.digirati.elucidate.infrastructure.extractor.selector;

import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

import com.digirati.elucidate.common.infrastructure.constants.JSONLDConstants;
import com.digirati.elucidate.common.infrastructure.constants.OAConstants;
import com.digirati.elucidate.model.annotation.selector.textquote.AnnotationTextQuoteSelector;

public class AnnotationTextQuoteSelectorExtractor extends AbstractAnnotationSelectorExtractor<AnnotationTextQuoteSelector> {

    @NotNull
    @Override
    protected String getSelectorType() {
        return OAConstants.URI_TEXT_QUOTE_SELECTOR;
    }

    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    protected AnnotationTextQuoteSelector buildSelector(@NotNull Map<String, Object> jsonMap) {

        AnnotationTextQuoteSelector annotationTextQuoteSelector = new AnnotationTextQuoteSelector();

        List<Map<String, Object>> exacts = (List<Map<String, Object>>) jsonMap.get(OAConstants.URI_EXACT);
        if (exacts != null && exacts.size() == 1) {
            Map<String, Object> valueMap = exacts.get(0);
            annotationTextQuoteSelector.setExact((String) valueMap.get(JSONLDConstants.ATTRIBUTE_VALUE));
        }

        List<Map<String, Object>> prefixes = (List<Map<String, Object>>) jsonMap.get(OAConstants.URI_PREFIX);
        if (prefixes != null && prefixes.size() == 1) {
            Map<String, Object> valueMap = prefixes.get(0);
            annotationTextQuoteSelector.setPrefix((String) valueMap.get(JSONLDConstants.ATTRIBUTE_VALUE));
        }

        List<Map<String, Object>> suffixes = (List<Map<String, Object>>) jsonMap.get(OAConstants.URI_SUFFIX);
        if (suffixes != null && suffixes.size() == 1) {
            Map<String, Object> valueMap = suffixes.get(0);
            annotationTextQuoteSelector.setSuffix((String) valueMap.get(JSONLDConstants.ATTRIBUTE_VALUE));
        }

        return annotationTextQuoteSelector;
    }
}
