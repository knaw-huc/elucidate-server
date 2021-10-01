package com.digirati.elucidate.common.infrastructure.util

import kotlin.math.floor

object PaginationUtils {
    @JvmStatic
    fun calculateLastPage(totalItems: Int, pageSize: Int): Int {
        return floor(0.0.coerceAtLeast(totalItems.toDouble() - 1) / pageSize).toInt()
    }
}