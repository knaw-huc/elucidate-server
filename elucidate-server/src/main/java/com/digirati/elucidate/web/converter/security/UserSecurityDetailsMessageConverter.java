package com.digirati.elucidate.web.converter.security;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.github.jsonldjava.utils.JsonUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.digirati.elucidate.infrastructure.security.UserSecurityDetails;
import com.digirati.elucidate.model.security.SecurityUser;
import com.digirati.elucidate.web.converter.AbstractMessageConverter;

@Component
public class UserSecurityDetailsMessageConverter extends AbstractMessageConverter<UserSecurityDetails> {

    public UserSecurityDetailsMessageConverter() {
        super(MediaType.APPLICATION_JSON);
    }

    @Override
    protected void decorateHeaders(UserSecurityDetails obj, @NotNull HttpOutputMessage outputMessage) {
        HttpHeaders headers = outputMessage.getHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(HttpHeaders.ALLOW, "GET,OPTIONS,HEAD");
    }

    @Override
    protected String getStringRepresentation(@NotNull UserSecurityDetails obj, MediaType contentType) throws Exception {
        Map<String, Object> jsonMap = new HashMap<>();
        List<Map<String, Object>> groupMaps = obj.getGroups()
                .stream()
                .map(group -> {
                    Map<String, Object> groupMap = new HashMap<>();
                    groupMap.put("id", group.getId());
                    groupMap.put("label", group.getLabel());

                    return groupMap;
                })
                .collect(Collectors.toList());

        SecurityUser user = obj.getUser();
        jsonMap.put("id", user.getId());
        jsonMap.put("uid", user.getUid());
        jsonMap.put("groups", groupMaps);

        return JsonUtils.toPrettyString(jsonMap);
    }

    @NotNull
    @Override
    protected UserSecurityDetails getObjectRepresentation(String str, MediaType contentType) {
        throw new UnsupportedOperationException(String.format(
                "Conversion from Content Type [%s] to [%s] is not supported",
                contentType,
                SecurityUser.class
        ));
    }

    @Override
    protected boolean supports(@NotNull Class<?> clazz) {
        return clazz == UserSecurityDetails.class;
    }
}
