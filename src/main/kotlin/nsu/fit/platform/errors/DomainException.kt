package nsu.fit.platform.errors

open class DomainException(message: String, cause: Throwable? = null) : Exception(message, cause)
