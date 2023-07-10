package com.moviemagic.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.RuntimeException

abstract class BaseUseCase<in P, R>(
) {

    suspend operator fun invoke(params: P): UseCaseResult<R> {
        return try {
            withContext(Dispatchers.IO) {
                execute(params).let {
                    UseCaseResult.Success(it)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            handleError(e)
        }
    }

    @Throws(RuntimeException::class)
    protected abstract suspend fun execute(params: P): R

    protected open fun handleError(e: Exception): UseCaseResult.Error = UseCaseResult.Error(UseCaseError("Server error"))

}