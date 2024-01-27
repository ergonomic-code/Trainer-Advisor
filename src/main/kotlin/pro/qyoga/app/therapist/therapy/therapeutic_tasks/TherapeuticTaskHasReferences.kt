package pro.qyoga.app.therapist.therapy.therapeutic_tasks

import pro.qyoga.core.clients.journals.api.JournalEntry
import pro.qyoga.core.therapy.therapeutic_tasks.model.TherapeuticTaskRef
import pro.qyoga.platform.errors.DomainError


class TherapeuticTaskHasReferences(
    val taskRef: TherapeuticTaskRef,
    val references: List<JournalEntry>
) : DomainError("Therapeutic task ${taskRef.id} has ${references.size} references to it")