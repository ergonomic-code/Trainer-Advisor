package pro.qyoga.core.clients.journals.internal

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import pro.qyoga.core.clients.journals.api.JournalEntry


@Repository
interface JournalEntriesMRepo : CrudRepository<JournalEntry, Long>