package pro.qyoga.tests.assertions

import io.kotest.matchers.shouldBe
import pro.qyoga.app.therapist.clients.journal.CreateJournalEntryRequest
import pro.qyoga.core.clients.journals.api.JournalEntry
import pro.qyoga.core.therapy.therapeutic_tasks.api.TherapeuticTask
import pro.qyoga.platform.spring.sdj.AggregateReferenceTarget


infix fun JournalEntry.shouldMatch(createJournalEntryRequest: CreateJournalEntryRequest) {
    date shouldBe createJournalEntryRequest.date
    (therapeuticTask as AggregateReferenceTarget<TherapeuticTask, Long>).entity.name shouldBe createJournalEntryRequest.therapeuticTaskName
    entryText shouldBe createJournalEntryRequest.journalEntryText
}