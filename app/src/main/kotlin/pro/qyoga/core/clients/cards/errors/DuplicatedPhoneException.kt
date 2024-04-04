package pro.qyoga.core.clients.cards.errors

import org.springframework.dao.DuplicateKeyException
import pro.azhidkov.platform.errors.DomainError
import pro.qyoga.core.clients.cards.model.Client


class DuplicatedPhoneException(
    entity: Client,
    override val cause: DuplicateKeyException
) : DomainError("Client with phone ${entity.phoneNumber} for therapist ${entity.therapistId} already exists")