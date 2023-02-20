package nsu.fit.platform.errors

open class ResourceNotFound(
    override val message: String
) : DomainException(message)
