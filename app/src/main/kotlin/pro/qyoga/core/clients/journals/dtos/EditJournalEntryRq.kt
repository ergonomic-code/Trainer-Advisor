package pro.qyoga.core.clients.journals.dtos

import org.springframework.format.annotation.DateTimeFormat
import pro.qyoga.l10n.RUSSIAN_DATE_FORMAT_PATTERN
import java.time.LocalDate


data class EditJournalEntryRq(
    @DateTimeFormat(pattern = RUSSIAN_DATE_FORMAT_PATTERN)
    val date: LocalDate,
    val therapeuticTaskName: String,
    val journalEntryText: String
)
