package com.sanmiaderibigbe.snap2pay.api

class Resource<T>(val status: Status, val data: T?, val message: String?) {


    companion object {
        fun <T> success(data: T): Resource<T> =
            Resource(
                Status.SUCCESS,
                data,
                message = null
            )

        fun <T> error(message: String?, data: T?): Resource<T> =
            Resource(
                Status.ERROR,
                data,
                message
            )

        fun <T> loading(data: T): Resource<T> =
            Resource(
                Status.LOADING,
                data,
                message = null
            )

        fun <T> loaded(data: T): Resource<T> =
            Resource(
                Status.LOADED,
                data,
                message = null
            )
    }

}


enum class Status {
    LOADING,
    ERROR,
    LOADED,
    SUCCESS

}