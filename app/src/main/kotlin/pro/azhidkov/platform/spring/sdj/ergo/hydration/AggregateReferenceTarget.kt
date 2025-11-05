package pro.azhidkov.platform.spring.sdj.ergo.hydration

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonUnwrapped
import org.springframework.data.jdbc.core.mapping.AggregateReference


data class AggregateReferenceTarget<T : Identifiable<ID>, ID : Any>(
    @JsonUnwrapped val entity: T
) : AggregateReference<T, ID> {

    @JsonIgnore
    override fun getId(): ID = entity.id

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AggregateReference<*, *>) return false

        return if (other is AggregateReferenceTarget<*, *>) {
            entity == other.entity
        } else {
            getId() == other.getId()
        }
    }

    override fun hashCode(): Int {
        return entity.id.hashCode()
    }

}

@Suppress("UNCHECKED_CAST")
fun <R : AggregateReference<T, ID>?, ID : Any, T : Identifiable<ID>> R.resolveOrThrow(): T =
    (this as? AggregateReferenceTarget<T, ID>)?.entity
        ?: error("$this is not instance of AggregateReferenceTarget")

fun <ID : Any, T : Identifiable<ID>> AggregateReference<T, ID>?.resolveOrNull(): T? =
    (this as? AggregateReferenceTarget<T, ID>)?.entity
