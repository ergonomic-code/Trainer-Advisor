package pro.qyoga.tests.fixture.backgrounds

import org.springframework.data.domain.Slice
import org.springframework.data.jdbc.core.JdbcAggregateOperations
import org.springframework.stereotype.Component
import pro.azhidkov.platform.spring.sdj.ergo.hydration.FetchSpec
import pro.azhidkov.platform.spring.sdj.ergo.hydration.hydrate
import pro.qyoga.app.therapist.clients.journal.edit_entry.create.CreateJournalEntryOp
import pro.qyoga.app.therapist.clients.journal.edit_entry.edit.EditJournalEntryOp
import pro.qyoga.core.clients.cards.model.ClientRef
import pro.qyoga.core.clients.journals.JournalEntriesRepo
import pro.qyoga.core.clients.journals.dtos.EditJournalEntryRq
import pro.qyoga.core.clients.journals.dtos.JournalPageRq
import pro.qyoga.core.clients.journals.model.JournalEntry
import pro.qyoga.core.users.auth.dtos.QyogaUserDetails
import pro.qyoga.tests.fixture.data.randomRecentLocalDate
import pro.qyoga.tests.fixture.object_mothers.clients.JournalEntriesObjectMother.journalEntry
import java.util.*


@Component
class ClientJournalBackgrounds(
    private val createJournalEntryOp: CreateJournalEntryOp,
    private val editJournalEntryOp: EditJournalEntryOp,
    private val journalEntriesRepo: JournalEntriesRepo,
    private val jdbcAggregateOperations: JdbcAggregateOperations
) {

    fun createJournalEntry(
        clientId: UUID,
        editJournalEntryRq: EditJournalEntryRq,
        therapist: QyogaUserDetails
    ): JournalEntry {
        return createJournalEntryOp.createJournalEntry(clientId, editJournalEntryRq, therapist)
    }

    fun createEntries(clientId: UUID, therapist: QyogaUserDetails, count: Int): List<JournalEntry> {
        return generateSequence { randomRecentLocalDate() }
            .distinct()
            .take(count)
            .map { date -> createJournalEntry(clientId, journalEntry(date = date), therapist) }
            .toList()
    }

    fun hydrate(journalEntries: List<JournalEntry>): List<JournalEntry> =
        jdbcAggregateOperations.hydrate(
            journalEntries,
            FetchSpec(JournalEntry::therapeuticTask)
        )

    fun getWholeJournal(clientId: UUID): Slice<JournalEntry> {
        return journalEntriesRepo.getJournalPage(
            JournalPageRq.wholeJournal(
                clientId,
                fetch = listOf(JournalEntry::therapeuticTask)
            )
        )
    }

    fun updateJournalEntry(
        clientRef: ClientRef,
        journalEntryId: Long,
        editJournalEntryRq: EditJournalEntryRq,
        theTherapistUserDetails: QyogaUserDetails
    ) {
        editJournalEntryOp(clientRef, journalEntryId, editJournalEntryRq, theTherapistUserDetails)
    }

}