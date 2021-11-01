package com.digirati.elucidate.web.converter.validationerror;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.digirati.elucidate.model.ValidationError;

@Component
public class JSONLDValidationErrorMessageConverter extends AbstractValidationErrorMessageConverter {

    public JSONLDValidationErrorMessageConverter() {
        super(APPLICATION_JSON_LD);
    }

    @NotNull
    @Override
    protected String getStringRepresentation(@NotNull ValidationError validationError, MediaType contentType) {
        return validationError.getJsonError();
    }

    @NotNull
    @Override
    protected ValidationError getObjectRepresentation(String str, MediaType contentType) {
        throw new UnsupportedOperationException(String.format("Conversion from [%s] not supported", ValidationError.class));
    }
}
