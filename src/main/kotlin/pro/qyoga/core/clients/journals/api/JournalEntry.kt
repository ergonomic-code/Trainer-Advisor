package pro.qyoga.core.clients.journals.api

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.jdbc.core.mapping.AggregateReference
import org.springframework.data.relational.core.mapping.Table
import pro.qyoga.core.clients.cards.api.Client
import pro.qyoga.core.therapy.therapeutic_tasks.api.TherapeuticTask
import pro.qyoga.platform.spring.sdj.AggregateReferenceTarget
import java.time.Instant
import java.time.LocalDate


@Table("journal_entries")
data class JournalEntry(
    val client: AggregateReference<Client, Long>,
    val date: LocalDate,
    val therapeuticTask: AggregateReference<TherapeuticTask, Long>,
    val entryText: String,

    @Id val id: Long = 0,
    @CreatedDate val createdAt: Instant = Instant.now(),
    @LastModifiedDate val lastModifiedAt: Instant? = null,
    @Version val version: Long = 0,
)

fun List<JournalEntry>.hydrate(fetchTherapeuticTasks: (List<Long>) -> Map<Long, TherapeuticTask>): List<JournalEntry> {
    val tasks = fetchTherapeuticTasks(this.map { it.therapeuticTask.id!! })
    return map {
        val task = tasks[it.therapeuticTask.id!!]
        checkNotNull(task) { "Reference to not existing task by ${it.therapeuticTask.id}" }
        it.copy(therapeuticTask = AggregateReferenceTarget(task))
    }
}