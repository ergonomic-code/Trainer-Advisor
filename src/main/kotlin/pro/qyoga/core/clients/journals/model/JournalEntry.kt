package pro.qyoga.core.clients.journals.model

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.jdbc.core.mapping.AggregateReference
import org.springframework.data.relational.core.mapping.Table
import pro.azhidkov.platform.spring.sdj.erpo.hydration.ref
import pro.qyoga.core.clients.cards.api.Client
import pro.qyoga.core.therapy.therapeutic_tasks.model.TherapeuticTask
import pro.qyoga.core.therapy.therapeutic_tasks.model.TherapeuticTaskRef
import java.time.Instant
import java.time.LocalDate


@Table("journal_entries")
data class JournalEntry(
    val client: AggregateReference<Client, Long>,
    val date: LocalDate,
    val therapeuticTask: TherapeuticTaskRef,
    val entryText: String,

    @Id val id: Long = 0,
    @CreatedDate val createdAt: Instant = Instant.now(),
    @LastModifiedDate val lastModifiedAt: Instant? = null,
    @Version val version: Long = 0,
) {

    fun updateBy(editJournalEntryRequest: EditJournalEntryRequest, therapeuticTask: TherapeuticTask): JournalEntry =
        copy(
            date = editJournalEntryRequest.date,
            therapeuticTask = therapeuticTask.ref(),
            entryText = editJournalEntryRequest.journalEntryText
        )

}