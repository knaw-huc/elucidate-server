package com.digirati.elucidate.web.converter.validationerror;

import com.digirati.elucidate.model.ValidationError;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class JSONLDValidationErrorMessageConverter extends AbstractValidationErrorMessageConverter {

    public JSONLDValidationErrorMessageConverter() {
        super(APPLICATION_JSON_LD);
    }

    @Override
    protected String getStringRepresentation(ValidationError validationError, MediaType contentType) {
        return validationError.getJsonError();
    }

    @Override
    protected ValidationError getObjectRepresentation(String str, MediaType contentType) {
        throw new UnsupportedOperationException(String.format("Conversion from [%s] not supported", ValidationError.class));
    }
}
