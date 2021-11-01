package com.digirati.elucidate.web.converter.security;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.github.jsonldjava.utils.JsonUtils;
import com.google.common.collect.ImmutableMap;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.digirati.elucidate.service.security.SecurityUserReferenceCollection;
import com.digirati.elucidate.web.converter.AbstractMessageConverter;

@Component
public class SecurityUserReferenceCollectionConverter extends AbstractMessageConverter<SecurityUserReferenceCollection> {

    public SecurityUserReferenceCollectionConverter() {
        super(MediaType.APPLICATION_JSON);
    }

    @Override
    protected void decorateHeaders(SecurityUserReferenceCollection obj, @NotNull HttpOutputMessage outputMessage) {
        HttpHeaders headers = outputMessage.getHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(HttpHeaders.ALLOW, "GET,OPTIONS,HEAD");
    }

    @Override
    protected String getStringRepresentation(@NotNull SecurityUserReferenceCollection obj, MediaType contentType) throws Exception {
        Map<String, Object> jsonMap = new HashMap<>();
        List<Map<String, String>> userJsonMaps = obj.getUsers().stream()
                .map(user -> ImmutableMap.of(
                        "uid", user.getUid(),
                        "id", user.getId()
                ))
                .collect(Collectors.toList());

        jsonMap.put("users", userJsonMaps);
        return JsonUtils.toString(jsonMap);
    }

    @NotNull
    @Override
    protected SecurityUserReferenceCollection getObjectRepresentation(String str, MediaType contentType) {
        throw new UnsupportedOperationException("Unable to demarshall a SecurityUserReferenceCollection object.");
    }

    @Override
    protected boolean supports(@NotNull Class<?> clazz) {
        return clazz.isAssignableFrom(SecurityUserReferenceCollection.class);
    }
}
