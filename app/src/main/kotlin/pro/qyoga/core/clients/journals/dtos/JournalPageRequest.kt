package pro.qyoga.core.clients.journals.dtos

import pro.qyoga.core.clients.journals.model.JournalEntry
import java.time.LocalDate
import java.util.*
import kotlin.reflect.KProperty1


data class JournalPageRequest(
    val clientId: UUID,
    val date: LocalDate? = null,
    val pageSize: Int = 10,
    val fetch: Iterable<KProperty1<JournalEntry, *>> = emptySet()
) {

    companion object {

        fun firstPage(clientId: UUID, fetch: Iterable<KProperty1<JournalEntry, *>> = emptySet()) =
            JournalPageRequest(clientId, fetch = fetch)

        fun wholeJournal(clientId: UUID, fetch: Iterable<KProperty1<JournalEntry, *>> = emptySet()) =
            JournalPageRequest(clientId, null, Int.MAX_VALUE, fetch)

    }

}