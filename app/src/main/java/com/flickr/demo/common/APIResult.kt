package com.flickr.demo.common

sealed class APIResult<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T): APIResult<T>(data)
    class Error<T>(data: T? = null, message: String): APIResult<T>(data, message)
    class Loading<T>(data: T? = null): APIResult<T>(data)
}