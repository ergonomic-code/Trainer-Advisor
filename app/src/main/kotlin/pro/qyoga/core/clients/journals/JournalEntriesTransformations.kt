package pro.qyoga.core.clients.journals.model

import pro.azhidkov.platform.spring.sdj.ergo.hydration.ref
import pro.qyoga.core.clients.journals.dtos.EditJournalEntryRequest
import pro.qyoga.core.therapy.therapeutic_tasks.model.TherapeuticTask

fun JournalEntry.updatedBy(
    editJournalEntryRequest: EditJournalEntryRequest,
    therapeuticTask: TherapeuticTask
): JournalEntry =
    copy(
        date = editJournalEntryRequest.date,
        therapeuticTask = therapeuticTask.ref(),
        entryText = editJournalEntryRequest.journalEntryText
    )