package pro.qyoga.app.therapist.clients.journal.list

import org.springframework.data.domain.Page
import org.springframework.stereotype.Component
import pro.qyoga.core.clients.cards.api.Client
import pro.qyoga.core.clients.cards.api.ClientsService
import pro.qyoga.core.clients.journals.api.JournalEntry
import pro.qyoga.core.clients.journals.api.JournalPageRequest
import pro.qyoga.core.clients.journals.api.JournalsService

sealed interface GetJournalPageResult {
    data class Success(val client: Client, val page: Page<JournalEntry>) : GetJournalPageResult
    data object ClientNotFound : GetJournalPageResult
}

@Component
class GetJournalPageWorkflow(
    private val clientsService: ClientsService,
    private val journalsService: JournalsService
) : (JournalPageRequest) -> GetJournalPageResult {

    override fun invoke(journalPageRequest: JournalPageRequest): GetJournalPageResult {
        val client = clientsService.findClient(journalPageRequest.clientId)
            ?: return GetJournalPageResult.ClientNotFound

        val journal = journalsService.getJournalPage(journalPageRequest)

        return GetJournalPageResult.Success(client, journal)
    }

}