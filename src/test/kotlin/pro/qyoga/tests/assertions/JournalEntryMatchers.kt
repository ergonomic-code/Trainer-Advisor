package pro.qyoga.tests.assertions

import io.kotest.matchers.shouldBe
import pro.qyoga.app.therapist.clients.journal.edit_entry.edit.EditJournalEntryRequest
import pro.qyoga.core.clients.journals.api.JournalEntry
import pro.qyoga.core.therapy.therapeutic_tasks.api.TherapeuticTask
import pro.qyoga.platform.spring.sdj.AggregateReferenceTarget


infix fun JournalEntry.shouldMatch(editJournalEntryRequest: EditJournalEntryRequest) {
    date shouldBe editJournalEntryRequest.date
    (therapeuticTask as AggregateReferenceTarget<TherapeuticTask, Long>).entity.name shouldBe editJournalEntryRequest.therapeuticTaskName
    entryText shouldBe editJournalEntryRequest.journalEntryText
}