package pro.qyoga.platform.spring.sdj

import org.springframework.data.jdbc.core.mapping.AggregateReference


interface Identifiable<T : Any> {

    val id: T

}

fun <E : Identifiable<T>, T : Any> E.ref(): AggregateReference<E, T> = AggregateReferenceTarget(this)