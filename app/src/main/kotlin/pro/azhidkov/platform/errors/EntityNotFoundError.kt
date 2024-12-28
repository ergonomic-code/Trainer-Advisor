package pro.azhidkov.platform.errors

import kotlin.reflect.KClass


class EntityNotFoundError(
    val entityType: KClass<*>,
    val key: Any
) : DomainError("${entityType.simpleName} not found by $key")