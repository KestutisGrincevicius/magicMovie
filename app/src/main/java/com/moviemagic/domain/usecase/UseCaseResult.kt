package com.moviemagic.domain.usecase

import kotlinx.coroutines.flow.MutableStateFlow
import java.lang.Exception

sealed class UseCaseResult<out R> {

    data class Success<out T>(val data: T): UseCaseResult<T>()

    data class Error(val error: UseCaseError): UseCaseResult<Nothing>()

}

val UseCaseResult<*>.succeeded
    get() = this is UseCaseResult.Success

val <T> UseCaseResult<T>.data: T?
    get() = (this as? UseCaseResult.Success)?.data

fun <T> UseCaseResult<T>.successOr(fallback: T): T {
    return (this as? UseCaseResult.Success<T>)?.data ?: fallback
}

open class UseCaseError(open val errorMessage: String): Exception()