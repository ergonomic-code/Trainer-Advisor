package pro.qyoga.core.clients.journals.api

import java.time.LocalDate
import kotlin.reflect.KProperty1


data class JournalPageRequest(
    val clientId: Long,
    val date: LocalDate? = null,
    val pageSize: Int = 10,
    val fetch: Iterable<KProperty1<JournalEntry, *>> = emptySet()
) {

    companion object {

        fun firstPage(clientId: Long, fetch: Iterable<KProperty1<JournalEntry, *>> = emptySet()) =
            JournalPageRequest(clientId, fetch = fetch)

        fun wholeJournal(clientId: Long, fetch: Iterable<KProperty1<JournalEntry, *>> = emptySet()) =
            JournalPageRequest(clientId, null, Int.MAX_VALUE, fetch)

    }

}