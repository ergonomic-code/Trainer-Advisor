package pro.azhidkov.platform.spring.sdj.ergo.hydration

import org.springframework.data.jdbc.core.mapping.AggregateReference


interface Identifiable<T : Any> {

    val id: T

}

fun <E : Identifiable<T>, T : Any> E.ref(): AggregateReference<E, T> = AggregateReferenceTarget(this)