package pro.qyoga.core.clients.journals.dtos

import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.format.annotation.DateTimeFormat
import pro.azhidkov.platform.spring.sdj.ergo.hydration.resolveOrThrow
import pro.qyoga.core.clients.journals.model.JournalEntry
import pro.qyoga.l10n.RUSSIAN_DATE_FORMAT_PATTERN
import java.time.LocalDate


data class EditJournalEntryRq(
    @DateTimeFormat(pattern = RUSSIAN_DATE_FORMAT_PATTERN)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = RUSSIAN_DATE_FORMAT_PATTERN)
    val date: LocalDate,
    val therapeuticTaskName: String,
    val journalEntryText: String,
    val version: Long
) {

    constructor(journalEntry: JournalEntry) : this(
        journalEntry.date,
        journalEntry.therapeuticTask.resolveOrThrow().name,
        journalEntry.entryText,
        journalEntry.version
    )

}