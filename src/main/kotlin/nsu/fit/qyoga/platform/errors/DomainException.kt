package nsu.fit.qyoga.platform.errors


open class DomainException(message: String, cause: Throwable? = null) : Exception(message, cause)
