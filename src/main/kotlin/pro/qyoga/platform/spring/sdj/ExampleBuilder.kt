/**
 * Доменно специфичный язык построения примеров для Spring Data Query by Example.
 *
 * Точкой входа является функция example, которая на вход получает объект-пробу и лямбду конфигурации матчинга.
 *
 * Объект-пробу можно сгенерировать динамически с помощью функции probeFrom.
 */
package pro.qyoga.platform.spring.sdj

import org.springframework.data.domain.Example
import org.springframework.data.domain.ExampleMatcher
import java.time.*
import kotlin.reflect.KClass
import kotlin.reflect.KProperty0
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.primaryConstructor


/**
 * @see probeFrom
 */
inline fun <reified T : Any> probeFrom(source: Any): T {
    val klass = T::class
    return probeFrom(source, klass)
}

/**
 * Генерирует целевой объект-пробу на основе исходного объекта.
 * В целевой объект будут перенесены те свойства исходного, имена которых совпадают в исходном и целевом объекте
 * и значения в целевом объекте отличны от null.
 */
fun <T : Any> probeFrom(source: Any, klass: KClass<T>): T {
    val constr = klass.primaryConstructor ?: klass.constructors.first()
    val args = constr.parameters.associateWith { arg ->
        @Suppress("UNCHECKED_CAST") val property =
            source::class.declaredMemberProperties.find { it.name == arg.name } as KProperty1<Any, Any>?
        var value = property?.invoke(source)
        value = when {
            value == null && arg.type.isMarkedNullable -> null
            value == null -> defaultValue(arg.type.classifier as KClass<*>)
            else -> value
        }
        value
    }
    return constr.callBy(args)
}

/**
 * Создаёт QBE-пример на базе объекта-пробы и лямбды конфигурации матчинга.
 * В лямбде-конфигурации доступны методы класса [ExampleBuilder]
 */
fun <T : Any> example(probe: T, body: ExampleBuilder.() -> Unit): Example<T> {
    val builder = ExampleBuilder(probe)
    builder.body()
    return Example.of(probe, builder.matcher())
}

class ExampleBuilder(probe: Any) {

    private val fields = probe::class.declaredMemberProperties.associateBy { it.name }

    private val filters = HashMap<String, ExampleMatcher.GenericPropertyMatcher>()

    /**
     * В случае если значение переданного свойства отлично от `null`, добавляет соответствующий матчер к примеру.
     */
    fun withMatcher(field: KProperty0<Any?>, matcher: ExampleMatcher.GenericPropertyMatcher) {
        if (field.invoke() != null) {
            filters[field.name] = matcher
        }
    }

    internal fun matcher(): ExampleMatcher {
        var matcher = filters.entries.fold(ExampleMatcher.matching()) { matcher, filter ->
            matcher.withMatcher(filter.key, filter.value)
        }
        matcher = matcher.withIgnorePaths(*(fields.keys - filters.keys).toTypedArray())
        return matcher
    }

}

private fun defaultValue(type: KClass<*>): Any {
    return when {
        type == Byte::class -> 0.toByte()
        type == Short::class -> 0.toShort()
        type == Int::class -> 0
        type == Long::class -> 0L
        type == Double::class -> 0.0
        type == Float::class -> 0.0F
        type == String::class -> ""

        type.isSubclassOf(Enum::class) -> type.java.enumConstants.first()

        type.isSubclassOf(List::class) -> emptyList<Any>()
        type.isSubclassOf(Set::class) -> emptySet<Any>()
        type.isSubclassOf(Map::class) -> emptyMap<Any, Any>()

        type == Duration::class -> Duration.ZERO
        type == Instant::class -> Instant.MIN
        type == LocalDate::class -> LocalDate.MIN
        type == LocalDateTime::class -> LocalDateTime.MIN
        type == OffsetDateTime::class -> OffsetDateTime.MIN
        else -> throw IllegalArgumentException("Unknown type: $type")
    }
}

