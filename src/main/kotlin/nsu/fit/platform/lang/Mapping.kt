package nsu.fit.platform.lang

import kotlin.reflect.full.memberProperties

fun dataClassToMap(dataClass: Any): Map<String, Any?> =
    dataClass::class.memberProperties
        .associate { it.name to it.getter.call(dataClass) }
