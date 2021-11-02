package com.digirati.elucidate.common.infrastructure.database.rowmapper

import com.digirati.elucidate.common.infrastructure.util.ResultSetUtils.getArrayAsSet
import com.digirati.elucidate.common.infrastructure.util.ResultSetUtils.getBoolean
import com.digirati.elucidate.common.infrastructure.util.ResultSetUtils.getDate
import com.digirati.elucidate.common.infrastructure.util.ResultSetUtils.getInt
import com.digirati.elucidate.common.infrastructure.util.ResultSetUtils.getJsonMap
import com.digirati.elucidate.common.infrastructure.util.ResultSetUtils.getString
import com.digirati.elucidate.common.model.annotation.w3c.W3CAnnotation
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet
import java.sql.SQLException

class W3CAnnotationRowMapper : RowMapper<W3CAnnotation> {
    @Throws(SQLException::class)
    override fun mapRow(rs: ResultSet, rowNum: Int): W3CAnnotation =
        W3CAnnotation().apply {
            pk = getInt(rs, "id")
            annotationId = getString(rs, "annotationid")
            cacheKey = getString(rs, "cachekey")
            collectionId = getString(rs, "collectionid")
            createdDateTime = getDate(rs, "createddatetime")
            isDeleted = getBoolean(rs, "deleted")
            jsonMap = getJsonMap(rs, "json")?.toMutableMap()
            modifiedDateTime = getDate(rs, "modifieddatetime")
            ownerId = getInt(rs, "ownerid")
            groups = getArrayAsSet(rs, "group_ids")
        }
}