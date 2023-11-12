package pro.qyoga.platform.spring.sdj

import org.springframework.data.domain.Sort
import kotlin.reflect.KProperty1

fun sortBy(property: KProperty1<*, *>) = Sort.by(property.name)
