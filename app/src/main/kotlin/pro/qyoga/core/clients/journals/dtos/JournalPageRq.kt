package pro.qyoga.core.clients.journals.dtos

import pro.qyoga.core.clients.journals.model.JournalEntry
import java.time.LocalDate
import java.util.*
import kotlin.reflect.KProperty1


data class JournalPageRq(
    val clientId: UUID,
    val date: LocalDate? = null,
    val pageSize: Int = DEFAULT_PAGE_SIZE,
    val fetch: Iterable<KProperty1<JournalEntry, *>> = emptySet()
) {

    companion object {

        const val DEFAULT_PAGE_SIZE = 10

        fun firstPage(clientId: UUID, fetch: Iterable<KProperty1<JournalEntry, *>> = emptySet()) =
            JournalPageRq(clientId, fetch = fetch)

        fun page(clientId: UUID, after: LocalDate, fetch: Iterable<KProperty1<JournalEntry, *>> = emptySet()) =
            JournalPageRq(clientId, date = after, fetch = fetch)

        fun wholeJournal(clientId: UUID, fetch: Iterable<KProperty1<JournalEntry, *>> = emptySet()) =
            JournalPageRq(clientId, null, Int.MAX_VALUE, fetch)

    }

}