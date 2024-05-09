package pro.qyoga.core.users.auth.errors

import org.springframework.dao.DuplicateKeyException
import pro.azhidkov.platform.errors.DomainError
import pro.qyoga.core.users.auth.model.User


class DuplicatedEmailException(
    duplicatedEntry: User,
    override val cause: DuplicateKeyException
) : DomainError("User with email ${duplicatedEntry.email} already exists")