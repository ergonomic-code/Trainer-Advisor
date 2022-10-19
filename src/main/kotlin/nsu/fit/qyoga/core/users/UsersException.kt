package nsu.fit.qyoga.core.users

import nsu.fit.qyoga.platform.errors.DomainException


sealed class UsersException(message: String, cause: Throwable? = null) : DomainException(message, cause)

class BadCredentials : UsersException("Bad credentials")