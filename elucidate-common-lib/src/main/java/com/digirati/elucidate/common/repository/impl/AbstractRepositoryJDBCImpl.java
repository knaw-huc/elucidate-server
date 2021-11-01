package com.digirati.elucidate.common.repository.impl;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

public abstract class AbstractRepositoryJDBCImpl {

    protected final Logger LOGGER = Logger.getLogger(getClass());

    private final JdbcTemplate jdbcTemplate;

    protected AbstractRepositoryJDBCImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Nullable
    protected <T> T queryForClass(@NotNull String sql, @NotNull Object[] params, @NotNull int[] sqlTypes, @NotNull Class<T> clazz) {
        LOGGER.info(String.format("Executing SQL query [%s] with parameters [%s] and SQL types [%s] for class [%s]", sql, Arrays.toString(params), Arrays.toString(sqlTypes), clazz));
        return jdbcTemplate.queryForObject(sql, params, sqlTypes, clazz);
    }

    @Nullable
    protected <T> T queryForObject(@NotNull String sql, @NotNull Object[] params, @NotNull int[] sqlTypes, @NotNull RowMapper<T> rowMapper) {
        List<T> results = queryForList(sql, params, sqlTypes, rowMapper);
        if (results.isEmpty()) {
            return null;
        } else if (results.size() == 1) {
            return results.get(0);
        } else {
            throw new IncorrectResultSizeDataAccessException(0, results.size());
        }
    }

    @Nullable
    protected <T> T queryForObject(@NotNull String sql, @NotNull Object[] params, @NotNull int[] sqlTypes, @NotNull ResultSetExtractor<T> resultSetExtractor) {
        LOGGER.info(String.format("Executing SQL query [%s] with parameters [%s] and SQL type [%s] using result set extractor [%s]", sql, Arrays.toString(params), Arrays.toString(sqlTypes), resultSetExtractor));
        return jdbcTemplate.query(sql, params, sqlTypes, resultSetExtractor);
    }

    @NotNull
    protected <T> List<T> queryForList(@NotNull String sql, @NotNull Object[] params, @NotNull int[] sqlTypes, @NotNull RowMapper<T> rowMapper) {
        LOGGER.info(String.format("Executing SQL query [%s] with parameters [%s] and SQL type [%s] using row mapper [%s]", sql, Arrays.toString(params), Arrays.toString(sqlTypes), rowMapper));
        return jdbcTemplate.query(sql, params, sqlTypes, rowMapper);
    }

    protected void update(@NotNull String sql, @NotNull Object[] params, @NotNull int[] sqlTypes) {
        LOGGER.info(String.format("Executing SQL update [%s] with parameters [%s] and SQL types [%s]", sql, Arrays.toString(params), Arrays.toString(sqlTypes)));
        int rowsAffected = jdbcTemplate.update(sql, params, sqlTypes);
        LOGGER.info(String.format("SQL Update [%s] affected [%s] rows", sql, rowsAffected));
    }
}
