package pro.qyoga.core.clients.journals.api

import org.springframework.dao.DuplicateKeyException
import pro.azhidkov.platform.errors.DomainError

class DuplicatedDate(
    val duplicatedEntry: JournalEntry,
    override val cause: DuplicateKeyException
) :
    DomainError("Journal entry for client ${duplicatedEntry.client.id} and date ${duplicatedEntry.date} already exists")