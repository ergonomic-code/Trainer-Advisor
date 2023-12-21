package pro.qyoga.core.clients.journals.api

import java.time.LocalDate


data class JournalPageRequest(
    val clientId: Long,
    val date: LocalDate? = null,
    val pageSize: Int = 10
) {

    companion object {


        fun firstPage(clientId: Long) = JournalPageRequest(clientId)

        fun wholeJournal(clientId: Long) = JournalPageRequest(clientId, null, Int.MAX_VALUE)

    }

}