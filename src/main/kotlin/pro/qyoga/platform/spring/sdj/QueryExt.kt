package pro.qyoga.platform.spring.sdj

import org.springframework.data.relational.core.query.Criteria
import kotlin.reflect.KProperty1

fun where(entityProperty: KProperty1<*, *>) =
    Criteria.where(entityProperty.name)
