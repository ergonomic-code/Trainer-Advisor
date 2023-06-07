package nsu.fit.platform.lang

import org.springframework.data.domain.Pageable

fun Pageable.sortDirection(parameterName: String) =
    sort.filter { it.property == parameterName }.map { it.direction }.firstOrNull()
