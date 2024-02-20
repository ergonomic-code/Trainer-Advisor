package pro.qyoga.core.therapy.therapeutic_tasks.errors

import pro.azhidkov.platform.errors.DomainError
import pro.qyoga.core.therapy.therapeutic_tasks.model.TherapeuticTask


class DuplicatedTherapeuticTaskName(val failedToSaveTask: TherapeuticTask, override val cause: Throwable) :
    DomainError("Therapeutic task with name ${failedToSaveTask.name} already exists")