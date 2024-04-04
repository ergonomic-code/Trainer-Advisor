package pro.azhidkov.platform.kotlin

inline fun <reified T : Throwable> Result<*>.isFailureOf(): Boolean = this.exceptionOrNull() is T

fun Result<*>.value(): Any? = if (this.isSuccess) this.getOrThrow() else this.exceptionOrNull()!!

inline fun <R : Any, reified T : Any?> Result<T>.mapSuccessOrNull(transform: (T & Any) -> R): Result<R?> {
    val value = this.getOrNull()

    @Suppress("UNCHECKED_CAST")
    val result = when {
        value != null -> Result.success(transform(value))
        else -> this as Result<R>
    }
    return result
}

inline fun <R : Any, reified T : Any> Result<T>.mapSuccess(transform: (T) -> R): Result<R> {
    val value = this.getOrNull()

    @Suppress("UNCHECKED_CAST")
    val result = when {
        value != null -> Result.success(transform(value))
        else -> this as Result<R>
    }
    return result
}

@Suppress("UNCHECKED_CAST")
inline fun <R : Any, reified T : Any?> Result<T>.mapNull(transform: () -> R): Result<R> =
    when {
        this.isSuccess && this.getOrNull() == null -> Result.success(transform())
        else -> this as Result<R>
    }

inline fun <reified T : Throwable, V : R, R> Result<V>.mapFailure(block: (T) -> R): Result<R> =
    if (this.exceptionOrNull() is T) Result.success(block(this.exceptionOrNull() as T)) else this