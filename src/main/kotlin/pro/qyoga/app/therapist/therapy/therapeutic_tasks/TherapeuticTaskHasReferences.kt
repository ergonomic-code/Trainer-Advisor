package pro.qyoga.app.therapist.therapy.therapeutic_tasks

import org.springframework.data.jdbc.core.mapping.AggregateReference
import pro.qyoga.core.clients.journals.api.JournalEntry
import pro.qyoga.core.therapy.therapeutic_tasks.api.TherapeuticTask
import pro.qyoga.platform.errors.DomainError


class TherapeuticTaskHasReferences(
    val task: AggregateReference<TherapeuticTask, Long>,
    val references: List<JournalEntry>
) : DomainError("Therapeutic task ${task.id} has ${references.size} references to it")