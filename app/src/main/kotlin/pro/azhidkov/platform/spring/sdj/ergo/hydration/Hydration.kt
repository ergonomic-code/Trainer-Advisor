package pro.azhidkov.platform.spring.sdj.ergo.hydration

import org.springframework.data.jdbc.core.JdbcAggregateOperations
import org.springframework.data.jdbc.core.mapping.AggregateReference
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

data class PropertyFetchSpec<T : Any?, V>(
    val property: KProperty1<T, V?>,
    val fetchSpec: FetchSpec<*> = FetchSpec(
        emptyList<PropertyFetchSpec<Any, Any>>()
    )
)

data class FetchSpec<T : Any?>(
    val propertyFetchSpecs: List<PropertyFetchSpec<T, *>>
) {

    constructor(vararg propertyFetchSpec: KProperty1<T, *>) : this(propertyFetchSpec.map {
        PropertyFetchSpec(
            it
        )
    })

    constructor(propertyFetchSpec: Iterable<KProperty1<T, *>>) : this(propertyFetchSpec.map {
        PropertyFetchSpec(
            it
        )
    })

}

fun <T : Any> JdbcAggregateOperations.hydrate(
    entities: Iterable<T>,
    fetchSpec: FetchSpec<T>
): List<T> {
    if (fetchSpec.propertyFetchSpecs.isEmpty()) {
        return (entities as? List<T>) ?: entities.toList()
    }

    val refs: Map<KProperty1<*, AggregateReference<*, *>?>, Map<Any, Any>> =
        fetchSpec.propertyFetchSpecs.filter {
            detectRefType(
                it.property
            ) != null
        }
            .associate { it: PropertyFetchSpec<T, *> ->
                val property = it.property as KProperty1<*, AggregateReference<*, *>?>
                property to fetchPropertyRefs(entities, it)
            }

    if (refs.isEmpty()) {
        return entities.toList()
    }

    return entities.map {
        hydrateEntity(it, refs)
    }
}

private fun <T : Any> JdbcAggregateOperations.fetchPropertyRefs(
    entities: Iterable<T>,
    propertyFetchSpec: PropertyFetchSpec<T, *>
): Map<Any, Any> {
    val property = propertyFetchSpec.property
    val ids = fetchIds(entities, property)
    val targetType = (property.returnType.arguments[0].type!!.classifier!! as KClass<*>).java
    val refs = hydrate(this.findAllById(ids, targetType), propertyFetchSpec.fetchSpec as FetchSpec<Any>)
        .associateBy { (it as Identifiable<*>).id }
    return refs
}

private fun <T : Any> fetchIds(
    entities: Iterable<T>,
    property: KProperty1<T, Any?>
): Set<Any?> = when (detectRefType(property)) {

    RefType.SCALAR ->
        entities
            .map { (property.getter.invoke(it) as AggregateReference<*, *>?)?.id }
            .toSet()

    else -> error("Unsupported property type: $property")
}

private fun <T : Any> hydrateEntity(entity: T, refs: Map<KProperty1<*, AggregateReference<*, *>?>, Map<Any, Any>>): T {
    val constructorParams = entity::class.primaryConstructor!!.parameters
    val paramValues = constructorParams.associateWith { param ->
        val prop =
            entity::class.memberProperties.find { prop -> param.name == prop.name }!! as KProperty1<T, AggregateReference<*, *>?>
        val currentValue: Any? = prop.invoke(entity)
        val newValue = if (prop in refs) {
            val id = (currentValue as AggregateReference<*, *>?)?.id
            if (id != null) {
                val ref = refs[prop]!![id] as Identifiable<*>?
                checkNotNull(ref) { "Aggregate ${prop.returnType.arguments[0]} not found by id=$id" }
                AggregateReferenceTarget(ref)
            } else {
                null
            }
        } else {
            currentValue
        }
        newValue
    }
    return entity::class.primaryConstructor!!.callBy(paramValues)
}

private fun detectRefType(property: KProperty1<*, *>): RefType? = when {
    property.returnType.jvmErasure.isSubclassOf(AggregateReference::class) ->
        RefType.SCALAR

    else ->
        null
}

enum class RefType {
    SCALAR,
}
