package pro.qyoga.core.therapy.therapeutic_tasks.api

import pro.qyoga.platform.errors.DomainError


class DuplicatedTherapeuticTaskName(val failedToSaveTask: TherapeuticTask, override val cause: Throwable) :
    DomainError("Therapeutic task with name ${failedToSaveTask.name} already exists")