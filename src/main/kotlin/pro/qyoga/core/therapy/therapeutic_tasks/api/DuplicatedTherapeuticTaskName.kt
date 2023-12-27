package pro.qyoga.core.therapy.therapeutic_tasks.api

import pro.qyoga.platform.errors.DomainError


class DuplicatedTherapeuticTaskName(val name: String, override val cause: Throwable) :
    DomainError("Therapetuic task with name $name already exists")