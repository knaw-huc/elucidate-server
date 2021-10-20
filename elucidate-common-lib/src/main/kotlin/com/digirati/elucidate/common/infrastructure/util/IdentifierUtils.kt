package com.digirati.elucidate.common.infrastructure.util

import com.digirati.elucidate.common.infrastructure.constants.ElucidateConstants

object IdentifierUtils {
    fun trimIdentifier(originalId: String): String {
        return originalId.substring(0, ElucidateConstants.MAX_ID_SIZE)
    }
}