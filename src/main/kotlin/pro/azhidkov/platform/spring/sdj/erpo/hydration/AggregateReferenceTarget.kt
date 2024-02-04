package pro.azhidkov.platform.spring.sdj.erpo.hydration

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonUnwrapped
import org.springframework.data.jdbc.core.mapping.AggregateReference


data class AggregateReferenceTarget<T : Identifiable<ID>, ID : Any>(
    @JsonUnwrapped val entity: T
) : AggregateReference<T, ID> {

    @JsonIgnore
    override fun getId(): ID = entity.id

}

fun <ID : Any, T : Identifiable<ID>> AggregateReference<T, ID>?.resolveOrThrow(): T =
    (this as? AggregateReferenceTarget<T, ID>)?.entity
        ?: error("$this is not instance of AggregateReferenceTarget")

fun <ID : Any, T : Identifiable<ID>> AggregateReference<T, ID>?.resolveOrNull(): T? =
    (this as? AggregateReferenceTarget<T, ID>)?.entity