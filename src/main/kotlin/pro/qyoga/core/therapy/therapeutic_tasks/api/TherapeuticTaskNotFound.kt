package pro.qyoga.core.therapy.therapeutic_tasks.api

import pro.qyoga.platform.errors.DomainError

class TherapeuticTaskNotFound(taskId: Long) : DomainError("Therapeutic task with id $taskId not found")