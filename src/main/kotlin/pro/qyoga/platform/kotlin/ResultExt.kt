package pro.qyoga.platform.kotlin

inline fun <reified T : Throwable> Result<*>.isFailureOf(): Boolean = this.exceptionOrNull() is T

fun Result<*>.value(): Any? = if (this.isSuccess) this.getOrThrow() else this.exceptionOrNull()!!