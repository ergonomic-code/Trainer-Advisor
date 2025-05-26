package pro.qyoga.core.clients.journals.dtos

import pro.azhidkov.platform.spring.sdj.ergo.hydration.resolveOrThrow
import pro.qyoga.core.clients.journals.model.JournalEntry
import pro.qyoga.core.therapy.therapeutic_tasks.model.TherapeuticTaskRef
import java.time.LocalDate


data class EditJournalEntryRq(
    val date: LocalDate,
    val therapeuticTask: TherapeuticTaskRef?,
    val therapeuticTaskTitle: String,
    val journalEntryText: String,
    val version: Long
) {

    constructor(journalEntry: JournalEntry) : this(
        journalEntry.date,
        journalEntry.therapeuticTask,
        journalEntry.therapeuticTask.resolveOrThrow().name,
        journalEntry.entryText,
        journalEntry.version
    )

}