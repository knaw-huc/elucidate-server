package com.digirati.elucidate.model

class ServiceResponse<T>
@JvmOverloads
constructor(val status: Status, obj: T? = null) {

    enum class Status {
        OK, NOT_FOUND, CACHE_KEY_MISS, ILLEGAL_MODIFICATION, NON_CONFORMANT, DELETED, ALREADY_EXISTS, UNAUTHORIZED
    }

    val obj: T?

    init {
        this.obj = obj
    }
}