package com.digirati.elucidate.infrastructure.database.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.digirati.elucidate.common.infrastructure.util.ResultSetUtils;
import com.digirati.elucidate.model.annotation.AnnotationReference;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.RowMapper;

public class AnnotationReferenceRowMapper implements RowMapper<AnnotationReference> {

    @Override
    public AnnotationReference mapRow(@NotNull ResultSet rs, int rowNum) throws SQLException {
        String id = ResultSetUtils.getString(rs, "id");
        String collectionId = ResultSetUtils.getString(rs, "collectionid");

        return new AnnotationReference(collectionId, id);
    }
}
