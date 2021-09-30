package com.digirati.elucidate.infrastructure.database.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.digirati.elucidate.common.infrastructure.util.ResultSetUtils;
import com.digirati.elucidate.model.annotation.selector.css.AnnotationCSSSelector;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.RowMapper;

public class AnnotationCSSSelectorRowMapper implements RowMapper<AnnotationCSSSelector> {

    @Override
    public AnnotationCSSSelector mapRow(@NotNull ResultSet rs, int rowNum) throws SQLException {
        AnnotationCSSSelector annotationCssSelector = new AnnotationCSSSelector();
        annotationCssSelector.setPk(ResultSetUtils.getInt(rs, "id"));
        annotationCssSelector.setBodyiri(ResultSetUtils.getString(rs, "bodyiri"));
        annotationCssSelector.setBodySourceIri(ResultSetUtils.getString(rs, "bodysourceiri"));
        annotationCssSelector.setTargetIri(ResultSetUtils.getString(rs, "targetiri"));
        annotationCssSelector.setTargetSourceIri(ResultSetUtils.getString(rs, "targetsourceiri"));
        annotationCssSelector.setAnnotationId(ResultSetUtils.getString(rs, "annotationid"));
        annotationCssSelector.setCollectionId(ResultSetUtils.getString(rs, "collectionid"));
        annotationCssSelector.setCreatedDateTime(ResultSetUtils.getDate(rs, "createddatetime"));
        annotationCssSelector.setDeleted(ResultSetUtils.getBoolean(rs, "deleted"));
        annotationCssSelector.setJsonMap(ResultSetUtils.getJsonMap(rs, "json"));
        annotationCssSelector.setModifiedDateTime(ResultSetUtils.getDate(rs, "modifieddatetime"));
        annotationCssSelector.setValue(ResultSetUtils.getString(rs, "value"));
        return annotationCssSelector;
    }
}
