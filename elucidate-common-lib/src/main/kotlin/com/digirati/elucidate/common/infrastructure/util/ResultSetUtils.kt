package com.digirati.elucidate.common.infrastructure.util

import com.github.jsonldjava.utils.JsonUtils
import org.apache.commons.lang3.StringUtils
import java.io.IOException
import java.sql.ResultSet
import java.sql.SQLException
import java.util.*

object ResultSetUtils {
    @JvmStatic
    @Throws(SQLException::class)
    fun getString(rs: ResultSet, columnName: String): String? {
        return rs.getString(columnName)
    }

    @JvmStatic
    @Throws(SQLException::class)
    fun getBoolean(rs: ResultSet, columnName: String): Boolean {
        return rs.getBoolean(columnName)
    }

    @JvmStatic
    @Throws(SQLException::class)
    fun getDate(rs: ResultSet, columnName: String): Date? {
        return rs.getTimestamp(columnName)
    }

    @JvmStatic
    @Throws(SQLException::class)
    fun getInt(rs: ResultSet, columnName: String): Int {
        return rs.getInt(columnName)
    }

    @JvmStatic
    @Throws(SQLException::class)
    fun <T> getArrayAsSet(rs: ResultSet, columnName: String): Set<T> {
        val array = rs.getArray(columnName) ?: return emptySet()
        return setOf(*array.array as Array<T>)
    }

    @JvmStatic
    @Throws(SQLException::class)
    fun getJsonMap(rs: ResultSet, columnName: String): Map<String, Any>? {
        val jsonStr = getString(rs, columnName)
        return if (StringUtils.isNotBlank(jsonStr)) {
            try {
                when (val jsonObject = JsonUtils.fromString(jsonStr)) {
                    is List<*> -> {
                        (jsonObject as List<Map<String, Any>?>)[0]
                    }
                    is Map<*, *> -> {
                        jsonObject as Map<String, Any>
                    }
                    else -> {
                        throw SQLException("Unexpected JSON Object type [${jsonObject.javaClass}]")
                    }
                }
            } catch (e: IOException) {
                throw SQLException("Unable to parse JSON String [$jsonStr] into Map", e)
            }
        } else null
    }
}