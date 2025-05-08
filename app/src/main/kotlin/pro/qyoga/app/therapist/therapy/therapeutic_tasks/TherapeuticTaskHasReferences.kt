package pro.qyoga.app.therapist.therapy.therapeutic_tasks

import pro.azhidkov.platform.errors.DomainError
import pro.qyoga.core.clients.journals.model.JournalEntry
import pro.qyoga.core.therapy.therapeutic_tasks.model.TherapeuticTask


class TherapeuticTaskHasReferences(
    val taskRef: TherapeuticTask,
    val references: List<JournalEntry>
) : DomainError(
    "Therapeutic task ${taskRef.id} has ${references.size} references to it",
    errorCode = "therapeutic-task-has-references"
)