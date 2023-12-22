package pro.qyoga.app.therapist.clients.journal.create_entry

import org.springframework.format.annotation.DateTimeFormat
import pro.qyoga.core.formats.RUSSIAN_DATE_FORMAT_PATTERN
import java.time.LocalDate


data class CreateJournalEntryRequest(
    @DateTimeFormat(pattern = RUSSIAN_DATE_FORMAT_PATTERN)
    val date: LocalDate,
    val therapeuticTaskName: String,
    val journalEntryText: String
)
