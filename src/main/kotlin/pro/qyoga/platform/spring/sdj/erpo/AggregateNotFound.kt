package pro.qyoga.platform.spring.sdj.erpo

import kotlin.reflect.KClass


class AggregateNotFound(id: Any, type: KClass<*>) :
    RuntimeException("Entity of type $type not found by id $id")