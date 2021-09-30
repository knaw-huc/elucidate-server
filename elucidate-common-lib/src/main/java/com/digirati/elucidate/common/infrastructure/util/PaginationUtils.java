package com.digirati.elucidate.common.infrastructure.util;

import static java.lang.Math.floor;
import static java.lang.Math.max;

public class PaginationUtils {
    public static int calculateLastPage(int totalItems, int pageSize) {
        return (int) floor(max(0, ((double) totalItems - 1)) / pageSize);
    }
}
