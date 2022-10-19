package nsu.fit.qyoga.platform.errors


open class ResourceNotFound(
    override val message: String
) : DomainException(message)
