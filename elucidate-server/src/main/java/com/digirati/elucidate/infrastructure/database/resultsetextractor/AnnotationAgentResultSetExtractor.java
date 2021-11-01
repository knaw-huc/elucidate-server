package com.digirati.elucidate.infrastructure.database.resultsetextractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.digirati.elucidate.common.infrastructure.util.ResultSetUtils;
import com.digirati.elucidate.model.annotation.agent.AnnotationAgent;

public class AnnotationAgentResultSetExtractor implements ResultSetExtractor<List<AnnotationAgent>> {

    @Override
    public List<AnnotationAgent> extractData(ResultSet rs) throws SQLException, DataAccessException {

        Map<Integer, AnnotationAgent> annotationAgents = new HashMap<>();

        while (rs.next()) {
            Integer pk = ResultSetUtils.getInt(rs, "id");
            AnnotationAgent annotationAgent = annotationAgents.get(pk);
            if (annotationAgent == null) {
                annotationAgent = new AnnotationAgent();
                annotationAgent.setAgentIri(ResultSetUtils.getString(rs, "agentiri"));
                annotationAgent.setCreatedDateTime(ResultSetUtils.getDate(rs, "createddatetime"));
                annotationAgent.setDeleted(ResultSetUtils.getBoolean(rs, "deleted"));
                annotationAgent.setPk(pk);
                annotationAgent.setJsonMap(ResultSetUtils.getJsonMap(rs, "json"));
                annotationAgent.setModifiedDateTime(ResultSetUtils.getDate(rs, "modifieddatetime"));
                annotationAgent.setNickname(ResultSetUtils.getString(rs, "nickname"));
                annotationAgents.put(annotationAgent.getPk(), annotationAgent);
            }

            String email = ResultSetUtils.getString(rs, "email");
            if (StringUtils.isNotBlank(email)) {
                if (annotationAgent.getEmails() == null) {
                    annotationAgent.setEmails(new ArrayList<>());
                }
                annotationAgent.getEmails().add(email);
            }

            Map<String, Object> emailJsonMap = ResultSetUtils.getJsonMap(rs, "emailjson");
            if (emailJsonMap != null && !emailJsonMap.isEmpty()) {
                if (annotationAgent.getEmailJsonMaps() == null) {
                    annotationAgent.setEmailJsonMaps(new ArrayList<>());
                }
                annotationAgent.getEmailJsonMaps().add(emailJsonMap);
            }

            String emailSha1 = ResultSetUtils.getString(rs, "emailsha1");
            if (StringUtils.isNotBlank(emailSha1)) {
                if (annotationAgent.getEmailSha1s() == null) {
                    annotationAgent.setEmailSha1s(new ArrayList<>());
                }
                annotationAgent.getEmailSha1s().add(emailSha1);
            }

            Map<String, Object> emailSha1JsonMap = ResultSetUtils.getJsonMap(rs, "emailsha1json");
            if (emailSha1JsonMap != null && !emailSha1JsonMap.isEmpty()) {
                if (annotationAgent.getEmailSha1JsonMaps() == null) {
                    annotationAgent.setEmailSha1JsonMaps(new ArrayList<>());
                }
                annotationAgent.getEmailSha1JsonMaps().add(emailSha1JsonMap);
            }

            String homepage = ResultSetUtils.getString(rs, "homepage");
            if (StringUtils.isNotBlank(homepage)) {
                if (annotationAgent.getHomepages() == null) {
                    annotationAgent.setHomepages(new ArrayList<>());
                }
                annotationAgent.getHomepages().add(homepage);
            }

            Map<String, Object> homepageJsonMap = ResultSetUtils.getJsonMap(rs, "homepagejson");
            if (homepageJsonMap != null && !homepageJsonMap.isEmpty()) {
                if (annotationAgent.getHomepageJsonMaps() == null) {
                    annotationAgent.setHomepageJsonMaps(new ArrayList<>());
                }
                annotationAgent.getHomepageJsonMaps().add(homepageJsonMap);
            }

            String name = ResultSetUtils.getString(rs, "name");
            if (StringUtils.isNotBlank(name)) {
                if (annotationAgent.getNames() == null) {
                    annotationAgent.setNames(new ArrayList<>());
                }
                annotationAgent.getNames().add(name);
            }

            Map<String, Object> nameJsonMap = ResultSetUtils.getJsonMap(rs, "namejson");
            if (nameJsonMap != null && !nameJsonMap.isEmpty()) {
                if (annotationAgent.getNameJsonMaps() == null) {
                    annotationAgent.setNameJsonMaps(new ArrayList<>());
                }
                annotationAgent.getNameJsonMaps().add(nameJsonMap);
            }

            String type = ResultSetUtils.getString(rs, "type");
            if (StringUtils.isNotBlank(type)) {
                if (annotationAgent.getTypes() == null) {
                    annotationAgent.setTypes(new ArrayList<>());
                }
                annotationAgent.getTypes().add(type);
            }

            String typeJson = ResultSetUtils.getString(rs, "typejson");
            if (StringUtils.isNotBlank(typeJson)) {
                if (annotationAgent.getTypesJsonList() == null) {
                    annotationAgent.setTypesJsonList(new ArrayList<>());
                }
                annotationAgent.getTypesJsonList().add(typeJson);
            }
        }

        return new ArrayList<>(annotationAgents.values());
    }
}
