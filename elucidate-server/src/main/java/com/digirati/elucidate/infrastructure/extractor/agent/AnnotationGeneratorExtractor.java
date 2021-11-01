package com.digirati.elucidate.infrastructure.extractor.agent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

import com.digirati.elucidate.common.infrastructure.constants.ActivityStreamConstants;
import com.digirati.elucidate.common.infrastructure.constants.FOAFConstants;
import com.digirati.elucidate.common.infrastructure.constants.JSONLDConstants;
import com.digirati.elucidate.model.annotation.agent.AnnotationAgent;

public class AnnotationGeneratorExtractor {

    @NotNull
    @SuppressWarnings("unchecked")
    public List<AnnotationAgent> extractGenerators(@NotNull Map<String, Object> jsonMap) {

        List<AnnotationAgent> annotationAgents = new ArrayList<>();

        List<Map<String, Object>> generatorJsonMaps = (List<Map<String, Object>>) jsonMap.get(ActivityStreamConstants.URI_GENERATOR);
        if (generatorJsonMaps != null && !generatorJsonMaps.isEmpty()) {

            for (Map<String, Object> generatorJsonMap : generatorJsonMaps) {

                AnnotationAgent annotationAgent = new AnnotationAgent();
                annotationAgent.setJsonMap(generatorJsonMap);

                String agentIri = (String) generatorJsonMap.get(JSONLDConstants.ATTRIBUTE_ID);
                annotationAgent.setAgentIri(agentIri);

                List<String> types = (List<String>) generatorJsonMap.get(JSONLDConstants.ATTRIBUTE_TYPE);
                annotationAgent.setTypes(types);
                annotationAgent.setTypesJsonList(types);

                List<Map<String, Object>> nicknameJsonMaps = (List<Map<String, Object>>) generatorJsonMap.get(FOAFConstants.URI_NICK);
                if (nicknameJsonMaps != null && nicknameJsonMaps.size() == 1) {

                    String nickname = (String) nicknameJsonMaps.get(0).get(JSONLDConstants.ATTRIBUTE_VALUE);
                    annotationAgent.setNickname(nickname);
                }

                List<Map<String, Object>> nameJsonMaps = (List<Map<String, Object>>) generatorJsonMap.get(FOAFConstants.URI_NAME);
                if (nameJsonMaps != null && !nameJsonMaps.isEmpty()) {

                    List<String> names = new ArrayList<>();
                    List<Map<String, Object>> namesJsonMaps = new ArrayList<>();

                    for (Map<String, Object> nameJsonMap : nameJsonMaps) {

                        String name = (String) nameJsonMap.get(JSONLDConstants.ATTRIBUTE_VALUE);
                        names.add(name);
                        namesJsonMaps.add(nameJsonMap);
                    }

                    annotationAgent.setNames(names);
                    annotationAgent.setNameJsonMaps(namesJsonMaps);
                }

                List<Map<String, Object>> emailJsonMaps = (List<Map<String, Object>>) generatorJsonMap.get(FOAFConstants.URI_MBOX);
                if (emailJsonMaps != null && !emailJsonMaps.isEmpty()) {

                    List<String> emails = new ArrayList<>();
                    List<Map<String, Object>> emailsJsonMaps = new ArrayList<>();

                    for (Map<String, Object> emailJsonMap : emailJsonMaps) {

                        String email = (String) emailJsonMap.get(JSONLDConstants.ATTRIBUTE_VALUE);
                        emails.add(email);
                        emailsJsonMaps.add(emailJsonMap);
                    }

                    annotationAgent.setEmails(emails);
                    annotationAgent.setEmailJsonMaps(emailsJsonMaps);
                }

                List<Map<String, Object>> emailSha1JsonMaps = (List<Map<String, Object>>) generatorJsonMap.get(FOAFConstants.UIRI_MBOX_SHA1SUM);
                if (emailSha1JsonMaps != null && !emailSha1JsonMaps.isEmpty()) {

                    List<String> emailSha1s = new ArrayList<>();
                    List<Map<String, Object>> emailSha1sJsonMaps = new ArrayList<>();

                    for (Map<String, Object> emailSha1JsonMap : emailSha1JsonMaps) {

                        String emailSha1 = (String) emailSha1JsonMap.get(JSONLDConstants.ATTRIBUTE_VALUE);
                        emailSha1s.add(emailSha1);
                        emailSha1sJsonMaps.add(emailSha1JsonMap);
                    }

                    annotationAgent.setEmailSha1s(emailSha1s);
                    annotationAgent.setEmailSha1JsonMaps(emailSha1sJsonMaps);
                }

                List<Map<String, Object>> homepageJsonMaps = (List<Map<String, Object>>) generatorJsonMap.get(FOAFConstants.URI_HOMEPAGE);
                if (homepageJsonMaps != null && !homepageJsonMaps.isEmpty()) {

                    List<String> homepages = new ArrayList<>();
                    List<Map<String, Object>> homepagesJsonMaps = new ArrayList<>();

                    for (Map<String, Object> homepageJsonMap : homepageJsonMaps) {

                        String homepage = (String) homepageJsonMap.get(JSONLDConstants.ATTRIBUTE_ID);
                        homepages.add(homepage);
                        homepagesJsonMaps.add(homepageJsonMap);
                    }

                    annotationAgent.setHomepages(homepages);
                    annotationAgent.setHomepageJsonMaps(homepagesJsonMaps);
                }

                annotationAgents.add(annotationAgent);
            }
        }

        return annotationAgents;
    }
}
