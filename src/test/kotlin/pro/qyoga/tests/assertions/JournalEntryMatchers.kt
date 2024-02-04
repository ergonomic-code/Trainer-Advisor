package pro.qyoga.tests.assertions

import io.kotest.matchers.shouldBe
import pro.azhidkov.platform.spring.sdj.erpo.hydration.resolveOrThrow
import pro.qyoga.core.clients.journals.api.EditJournalEntryRequest
import pro.qyoga.core.clients.journals.api.JournalEntry


infix fun JournalEntry.shouldMatch(editJournalEntryRequest: EditJournalEntryRequest) {
    date shouldBe editJournalEntryRequest.date
    therapeuticTask.resolveOrThrow().name shouldBe editJournalEntryRequest.therapeuticTaskName
    entryText shouldBe editJournalEntryRequest.journalEntryText
}