package pro.qyoga.core.clients.journals.api

import pro.azhidkov.platform.errors.DomainError

class EntryNotFound(clientId: Long, entryId: Long) : DomainError(
    message = "Entry $entryId for client $clientId not found"
)
