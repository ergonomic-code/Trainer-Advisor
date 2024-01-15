package pro.qyoga.platform.spring.sdj.erpo

import pro.qyoga.core.therapy.therapeutic_tasks.api.TherapeuticTask
import kotlin.reflect.KClass


class AggregateNotFound(id: Any, type: KClass<TherapeuticTask>) :
    RuntimeException("Entity of type $type not found by id $id")