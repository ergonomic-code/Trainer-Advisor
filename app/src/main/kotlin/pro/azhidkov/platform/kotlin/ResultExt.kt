package pro.azhidkov.platform.kotlin

import kotlin.Result.Companion.failure
import kotlin.Result.Companion.success

inline fun <reified T : Throwable> Result<*>.isFailureOf(): Boolean = this.exceptionOrNull() is T

fun Result<*>.value(): Any? = if (this.isSuccess) this.getOrThrow() else this.exceptionOrNull()!!

inline fun <R : Any, reified T : Any?> Result<T>.mapSuccessOrNull(transform: (T & Any) -> R): Result<R?> {
    val value = this.getOrNull()

    @Suppress("UNCHECKED_CAST")
    val result = when {
        value != null -> success(transform(value))
        else -> this as Result<R>
    }
    return result
}

inline fun <R : Any, reified T : Any> Result<T>.mapSuccess(transform: (T) -> R): Result<R> {
    val value = this.getOrNull()

    @Suppress("UNCHECKED_CAST")
    val result = when {
        value != null -> success(transform(value))
        else -> this as Result<R>
    }
    return result
}

@Suppress("UNCHECKED_CAST")
inline fun <R : Any, reified T : Any?> Result<T>.mapNull(transform: () -> R): Result<R> =
    when {
        this.isSuccess && this.getOrNull() == null -> success(transform())
        else -> this as Result<R>
    }

inline fun <reified T : Throwable, V : R, R> Result<V>.recoverFailure(block: (T) -> R): Result<R> =
    if (this.exceptionOrNull() is T) success(block(this.exceptionOrNull() as T)) else this

inline fun <reified T : Throwable, V : R, R> Result<V>.tryRecover(block: (T) -> Result<R>): Result<R> =
    if (this.exceptionOrNull() is T) block(this.exceptionOrNull() as T) else this

inline fun <reified T : Throwable, R> Result<R>.mapFailure(block: (T) -> Throwable): Result<R> =
    if (this.exceptionOrNull() is T) failure(block(this.exceptionOrNull() as T)) else this

fun <T> tryExecute(eventsRequest: () -> T): Result<T> =
    try {
        success(eventsRequest())
    } catch (e: Exception) {
        failure(e)
    }
