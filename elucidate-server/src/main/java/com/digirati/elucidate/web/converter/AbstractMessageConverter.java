package com.digirati.elucidate.web.converter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.github.fge.jsonschema.main.JsonValidator;
import com.github.jsonldjava.core.JsonLdOptions;
import com.github.jsonldjava.utils.JsonUtils;
import org.apache.commons.collections.map.ListOrderedMap;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import com.digirati.elucidate.common.infrastructure.constants.JSONLDConstants;
import com.digirati.elucidate.model.JSONLDProfile;
import com.digirati.elucidate.model.JSONLDProfile.Format;

public abstract class AbstractMessageConverter<T> extends AbstractHttpMessageConverter<T> {

    protected static final DateFormat MEMENTO_DATE_FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
    protected static final MediaType APPLICATION_JSON_LD = MediaType.valueOf("application/ld+json");
    protected static final MediaType APPLICATION_TURTLE = MediaType.valueOf("application/x-turtle");

    protected JsonLdOptions jsonLdOptions;

    protected AbstractMessageConverter(MediaType... supportedMediaTypes) {
        super(supportedMediaTypes);
        this.jsonLdOptions = new JsonLdOptions();
    }

    protected abstract void decorateHeaders(T obj, HttpOutputMessage outputMessage);

    protected abstract String getStringRepresentation(T obj, MediaType contentType) throws Exception;

    protected abstract T getObjectRepresentation(String str, MediaType contentType) throws Exception;

    @SuppressWarnings("serial")
    protected JSONLDProfile getJsonLdProfile(MediaType contentType, String... defaultContexts) {

        JSONLDProfile jsonLdProfile = new JSONLDProfile();

        String profile = contentType.getParameter("profile");

        if (StringUtils.isNotBlank(profile)) {
            defaultContexts = StringUtils.split(profile, " ");
        }

        List<Format> formats = new ArrayList<>();
        for (String defaultContext : defaultContexts) {
            formats.add(extractFormat(defaultContext));
        }
        jsonLdProfile.setFormats(formats);

        List<String> contexts = new ArrayList<>();
        for (String defaultContext : defaultContexts) {
            contexts.add(prepareContext(defaultContext));
        }

        jsonLdProfile.setContexts(new HashMap<String, List<String>>() {
            {
                put(JSONLDConstants.ATTRIBUTE_CONTEXT, contexts);
            }
        });

        return jsonLdProfile;
    }


    @SuppressWarnings("unchecked")
    protected Map<String, Object> reorderJsonAttributes(Map<String, Object> jsonMap) {

        ListOrderedMap orderedJsonMap = new ListOrderedMap();
        orderedJsonMap.putAll(jsonMap);

        Object context = orderedJsonMap.get(JSONLDConstants.ATTRIBUTE_CONTEXT);
        if (context != null) {
            orderedJsonMap.remove(JSONLDConstants.ATTRIBUTE_CONTEXT);
            orderedJsonMap.put(0, JSONLDConstants.ATTRIBUTE_CONTEXT, context);
        }

        return orderedJsonMap;
    }

    protected String validate(String jsonStr, JsonNode validationSchema) throws ProcessingException, IOException {

        JsonNode json = JsonLoader.fromString(jsonStr);

        JsonValidator jsonValidator = JsonSchemaFactory.byDefault().getValidator();
        ProcessingReport processingReport = jsonValidator.validate(validationSchema, json);
        if (!processingReport.isSuccess()) {

            ArrayNode jsonArray = JsonNodeFactory.instance.arrayNode();

            for (ProcessingMessage processingMessage : processingReport) {
                jsonArray.add(processingMessage.asJson());
            }

            return JsonUtils.toPrettyString(jsonArray);
        }

        return null;
    }

    private Format extractFormat(String profile) {
        String format = StringUtils.substringAfterLast(profile, "#");
        format = StringUtils.strip(format, "\"");

        if (StringUtils.isNotBlank(format)) {
            format = StringUtils.capitalize(format);
            return Format.valueOf(format);
        }

        return Format.COMPACTED;
    }

    private String prepareContext(String profile) {
        profile = StringUtils.substringBeforeLast(profile, "#");
        return StringUtils.strip(profile, "\"");
    }

    @NotNull
    @Override
    protected T readInternal(@NotNull Class<? extends T> clazz, @NotNull HttpInputMessage inputMessage) throws HttpMessageNotReadableException {
        try {
            MediaType contentType = inputMessage.getHeaders().getContentType();
            String str = consume(inputMessage);
            return getObjectRepresentation(str, contentType);
        } catch (Exception e) {
            throw new HttpMessageNotReadableException(String.format("Unable to read HttpInputMessage [%s] to class [%s]", inputMessage, clazz), e, inputMessage);
        }
    }

    @Override
    protected void writeInternal(@NotNull T obj, @NotNull HttpOutputMessage outputMessage) throws HttpMessageNotWritableException {
        try {
            decorateHeaders(obj, outputMessage);
            MediaType contentType = outputMessage.getHeaders().getContentType();
            String str = getStringRepresentation(obj, contentType);
            stream(str, outputMessage);
        } catch (Exception e) {
            throw new HttpMessageNotWritableException(String.format("Unable to write Object [%s] to HttpOutputMessage [%s]", obj, outputMessage), e);
        }
    }

    private void stream(String str, HttpOutputMessage outputMessage) throws IOException {
        InputStream inputStream = new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8));
        OutputStream messageOutputStream = outputMessage.getBody();
        IOUtils.copy(inputStream, messageOutputStream);
    }

    private String consume(HttpInputMessage inputMessage) throws IOException {
        InputStream messageInputStream = inputMessage.getBody();
        return IOUtils.toString(messageInputStream, Charset.defaultCharset());
    }
}
