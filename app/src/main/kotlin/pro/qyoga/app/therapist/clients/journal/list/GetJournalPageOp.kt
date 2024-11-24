package pro.qyoga.app.therapist.clients.journal.list

import org.springframework.data.domain.Page
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import pro.qyoga.core.clients.cards.ClientsRepo
import pro.qyoga.core.clients.cards.model.Client
import pro.qyoga.core.clients.journals.JournalEntriesRepo
import pro.qyoga.core.clients.journals.dtos.JournalPageRequest
import pro.qyoga.core.clients.journals.model.JournalEntry

sealed interface GetJournalPageResult {
    data class Success(val client: Client, val page: Page<JournalEntry>) : GetJournalPageResult
    data object ClientNotFound : GetJournalPageResult
}

@Component
class GetJournalPageOp(
    private val clientsRepo: ClientsRepo,
    private val journalEntriesRepo: JournalEntriesRepo
) : (JournalPageRequest) -> GetJournalPageResult {

    override fun invoke(journalPageRequest: JournalPageRequest): GetJournalPageResult {
        val client = clientsRepo.findByIdOrNull(journalPageRequest.clientId)
            ?: return GetJournalPageResult.ClientNotFound

        val journal = journalEntriesRepo.getJournalPage(journalPageRequest)

        return GetJournalPageResult.Success(client, journal)
    }

}