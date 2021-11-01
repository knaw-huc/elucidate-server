package com.digirati.elucidate.infrastructure.database.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.RowMapper;

import com.digirati.elucidate.common.infrastructure.util.ResultSetUtils;
import com.digirati.elucidate.model.security.SecurityUser;

public class SecurityUserRowMapper implements RowMapper<SecurityUser> {
    @Override
    public SecurityUser mapRow(@NotNull ResultSet rs, int rowNum) throws SQLException {
        SecurityUser user = new SecurityUser();
        user.setPk(ResultSetUtils.getInt(rs, "id"));
        user.setUid(ResultSetUtils.getString(rs, "uid"));
        user.setId(ResultSetUtils.getString(rs, "security_user_id"));
        return user;
    }
}
