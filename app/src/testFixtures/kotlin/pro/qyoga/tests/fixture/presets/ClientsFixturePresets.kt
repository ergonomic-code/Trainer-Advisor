package pro.qyoga.tests.fixture.presets

import org.springframework.stereotype.Component
import pro.qyoga.core.clients.cards.dtos.ClientCardDto
import pro.qyoga.core.clients.cards.model.Client
import pro.qyoga.core.clients.journals.dtos.EditJournalEntryRq
import pro.qyoga.core.clients.journals.model.JournalEntry
import pro.qyoga.core.users.auth.dtos.QyogaUserDetails
import pro.qyoga.tests.fixture.backgrounds.ClientJournalBackgrounds
import pro.qyoga.tests.fixture.backgrounds.ClientsBackgrounds
import pro.qyoga.tests.fixture.object_mothers.clients.ClientsObjectMother
import pro.qyoga.tests.fixture.object_mothers.clients.JournalEntriesObjectMother
import pro.qyoga.tests.fixture.object_mothers.therapists.theTherapistUserDetails

data class ClientWithJournal(
    val client: Client,
    val journal: List<JournalEntry>
)

@Component
class ClientsFixturePresets(
    private val clientsBackgrounds: ClientsBackgrounds,
    private val clientJournalBackgrounds: ClientJournalBackgrounds
) {

    fun createAClientWithJournalEntry(
        therapistUserDetails: QyogaUserDetails = theTherapistUserDetails,
        createClient: () -> ClientCardDto = { ClientsObjectMother.createClientCardDtoMinimal() },
        createEditJournalEntryRequest: () -> EditJournalEntryRq = { JournalEntriesObjectMother.journalEntry() }
    ): ClientWithJournal {
        val client = clientsBackgrounds.createClient(createClient(), therapistUserDetails)
        val journalEntry = clientJournalBackgrounds.createJournalEntry(
            client.id,
            createEditJournalEntryRequest(),
            therapistUserDetails
        )

        return ClientWithJournal(client, listOf(journalEntry))
    }

    fun createAClientsWithJournalEntry(
        therapistUserDetails: QyogaUserDetails = theTherapistUserDetails,
        createClient: () -> ClientCardDto = { ClientsObjectMother.createClientCardDtoMinimal() },
        createEditJournalEntryRequest: () -> EditJournalEntryRq = { JournalEntriesObjectMother.journalEntry() },
        clientsCount: Int = 2
    ): List<ClientWithJournal> {
        return (1..clientsCount).map {
            createAClientWithJournalEntry(
                therapistUserDetails,
                createClient,
                createEditJournalEntryRequest
            )
        }
    }

}