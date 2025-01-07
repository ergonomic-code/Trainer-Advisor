package pro.qyoga.core.clients.journals.model

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.relational.core.mapping.Table
import pro.qyoga.core.clients.cards.model.ClientRef
import pro.qyoga.core.therapy.therapeutic_tasks.model.TherapeuticTaskRef
import java.time.Instant
import java.time.LocalDate


@Table("journal_entries")
data class JournalEntry(
    val clientRef: ClientRef,
    val date: LocalDate,
    val therapeuticTask: TherapeuticTaskRef?,
    val entryText: String,

    @Id val id: Long = 0,
    @CreatedDate val createdAt: Instant = Instant.now(),
    @LastModifiedDate val lastModifiedAt: Instant? = null,
    @Version val version: Long = 0,
) {

    object Fetch {
        val summaryRefs = listOf(JournalEntry::therapeuticTask)
        val client = listOf(JournalEntry::clientRef)
    }

}