package pro.qyoga.app.therapist.clients.journal

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.stereotype.Component
import pro.qyoga.core.clients.cards.api.Client
import pro.qyoga.core.clients.cards.api.ClientsService
import pro.qyoga.core.clients.journals.api.JournalEntry
import pro.qyoga.core.clients.journals.api.JournalPageRequest
import pro.qyoga.core.clients.journals.api.JournalsService
import pro.qyoga.core.clients.journals.api.hydrate
import pro.qyoga.core.therapy.therapeutic_tasks.api.TherapeuticTasksService

sealed interface GetJournalPageResult {
    data class Success(val client: Client, val page: Page<JournalEntry>) : GetJournalPageResult
    data object ClientNotFound : GetJournalPageResult
}

@Component
class GetJournalPageWorkflow(
    private val clientsService: ClientsService,
    private val journalsService: JournalsService,
    private val therapeuticTasksService: TherapeuticTasksService
) {

    fun getJournalPage(journalPageRequest: JournalPageRequest): GetJournalPageResult {
        val client = clientsService.findClient(journalPageRequest.clientId)
            ?: return GetJournalPageResult.ClientNotFound

        var journal = journalsService.getJournalPage(journalPageRequest)

        journal = PageImpl(
            journal.content.hydrate(therapeuticTasksService::findAllById),
            journal.pageable,
            journal.totalElements
        )

        return GetJournalPageResult.Success(client, journal)
    }

}