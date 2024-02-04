package pro.qyoga.app.therapist.therapy.therapeutic_tasks

import pro.azhidkov.platform.errors.DomainError
import pro.qyoga.core.clients.journals.api.JournalEntry
import pro.qyoga.core.therapy.therapeutic_tasks.model.TherapeuticTaskRef


class TherapeuticTaskHasReferences(
    val taskRef: TherapeuticTaskRef,
    val references: List<JournalEntry>
) : DomainError("Therapeutic task ${taskRef.id} has ${references.size} references to it")