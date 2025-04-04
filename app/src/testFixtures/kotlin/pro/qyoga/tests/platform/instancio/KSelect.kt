package pro.qyoga.tests.platform.instancio

import org.instancio.Select
import org.instancio.TargetSelector
import kotlin.reflect.KProperty1
import kotlin.reflect.jvm.javaField

class KSelect {
    companion object {
        fun <T, V> field(property: KProperty1<T, V>): TargetSelector {
            val field = property.javaField!!
            return Select.field(field.declaringClass, field.name)
        }
    }
}