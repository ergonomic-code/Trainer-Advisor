package pro.qyoga.app.therapist.clients.journal.list

import org.springframework.data.domain.Page
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import pro.azhidkov.platform.errors.ResourceNotFoundException
import pro.qyoga.core.clients.cards.ClientsRepo
import pro.qyoga.core.clients.cards.model.Client
import pro.qyoga.core.clients.journals.JournalEntriesRepo
import pro.qyoga.core.clients.journals.dtos.JournalPageRq
import pro.qyoga.core.clients.journals.model.JournalEntry

data class ClientJournalPage(val client: Client, val page: Page<JournalEntry>)

@Component
class GetJournalPageOp(
    private val clientsRepo: ClientsRepo,
    private val journalEntriesRepo: JournalEntriesRepo
) : (JournalPageRq) -> ClientJournalPage {

    override fun invoke(journalPageRq: JournalPageRq): ClientJournalPage {
        val client = clientsRepo.findByIdOrNull(journalPageRq.clientId)
            ?: throw ResourceNotFoundException(Client::id, journalPageRq.clientId)

        val journal = journalEntriesRepo.getJournalPage(journalPageRq)

        return ClientJournalPage(client, journal)
    }

}