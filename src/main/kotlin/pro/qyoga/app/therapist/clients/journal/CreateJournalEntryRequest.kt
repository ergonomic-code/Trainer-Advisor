package pro.qyoga.app.therapist.clients.journal

import org.springframework.format.annotation.DateTimeFormat
import pro.qyoga.core.formats.russianDateFormatPattern
import java.time.LocalDate


data class CreateJournalEntryRequest(
    @DateTimeFormat(pattern = russianDateFormatPattern)
    val date: LocalDate,
    val therapeuticTaskName: String,
    val therapeuticTaskId: Long?,
    val journalEntryText: String
)
