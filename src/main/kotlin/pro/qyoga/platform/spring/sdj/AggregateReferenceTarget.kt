package pro.qyoga.platform.spring.sdj

import org.springframework.data.jdbc.core.mapping.AggregateReference


data class AggregateReferenceTarget<T : Identifiable<ID>, ID : Any>(
    val entity: T
) : AggregateReference<T, ID> {

    override fun getId(): ID = entity.id

}