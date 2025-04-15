package pro.qyoga.tests.assertions

import io.kotest.matchers.shouldBe
import pro.azhidkov.platform.spring.sdj.ergo.hydration.resolveOrThrow
import pro.qyoga.core.clients.journals.dtos.EditJournalEntryRq
import pro.qyoga.core.clients.journals.model.JournalEntry


infix fun JournalEntry.shouldMatch(editJournalEntryRq: EditJournalEntryRq) {
    date shouldBe editJournalEntryRq.date
    therapeuticTask.resolveOrThrow().name shouldBe editJournalEntryRq.therapeuticTaskName
    entryText shouldBe editJournalEntryRq.journalEntryText
}

infix fun EditJournalEntryRq.shouldMatch(journalEntry: JournalEntry) {
    date shouldBe journalEntry.date
    therapeuticTaskName shouldBe journalEntry.therapeuticTask.resolveOrThrow().name
    journalEntryText shouldBe journalEntryText
}