package pro.qyoga.platform.spring.sdj.erpo.hydration

import org.springframework.data.jdbc.core.mapping.AggregateReference


data class AggregateReferenceTarget<T : Identifiable<ID>, ID : Any>(
    val entity: T
) : AggregateReference<T, ID> {

    override fun getId(): ID = entity.id

}

fun <ID : Any, T : Identifiable<ID>> AggregateReference<T, ID>?.resolveOrThrow(): T =
    (this as? AggregateReferenceTarget<T, ID>)?.entity
        ?: error("$this is not instance of AggregateReferenceTarget")
