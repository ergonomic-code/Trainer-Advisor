package pro.qyoga.app.therapist.clients.journal.edit_entry.shared

import pro.qyoga.platform.errors.DomainError

class EntryNotFound(clientId: Long, entryId: Long) : DomainError(
    message = "Entry $entryId for client $clientId not found"
)
