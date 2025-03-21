package pro.qyoga.core.clients.journals.model

import pro.azhidkov.platform.spring.sdj.ergo.hydration.ref
import pro.qyoga.core.clients.journals.dtos.EditJournalEntryRq
import pro.qyoga.core.therapy.therapeutic_tasks.model.TherapeuticTask

fun JournalEntry.updatedBy(
    editJournalEntryRq: EditJournalEntryRq,
    therapeuticTask: TherapeuticTask
): JournalEntry =
    copy(
        date = editJournalEntryRq.date,
        therapeuticTask = therapeuticTask.ref(),
        entryText = editJournalEntryRq.journalEntryText
    )