package pro.qyoga.core.clients.journals.errors

import org.springframework.dao.DuplicateKeyException
import pro.azhidkov.platform.errors.DomainError
import pro.qyoga.core.clients.journals.model.JournalEntry

class DuplicatedDate(
    val duplicatedEntry: JournalEntry,
    override val cause: DuplicateKeyException
) :
    DomainError(
        "Journal entry for client ${duplicatedEntry.clientRef.id} and date ${duplicatedEntry.date} already exists",
        errorCode = "duplicated-date"
    )