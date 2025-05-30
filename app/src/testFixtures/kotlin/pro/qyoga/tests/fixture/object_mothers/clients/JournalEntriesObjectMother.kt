package pro.qyoga.tests.fixture.object_mothers.clients

import pro.qyoga.core.clients.journals.dtos.EditJournalEntryRq
import pro.qyoga.tests.fixture.data.randomRecentLocalDate
import pro.qyoga.tests.fixture.data.randomSentence
import java.time.LocalDate


object JournalEntriesObjectMother {

    fun journalEntry(
        date: LocalDate = randomRecentLocalDate(),
        therapeuticTaskName: String = randomSentence(1, 3),
        text: String = randomSentence(1, 100)
    ) = EditJournalEntryRq(date, null, therapeuticTaskName, text, 1)

    fun journalEntriesWithUniqueDate(): () -> EditJournalEntryRq {
        val datesIterator = generateSequence { randomRecentLocalDate() }
            .distinct()
            .iterator()

        return { journalEntry(date = datesIterator.next()) }
    }

}