package pro.qyoga.platform.spring.sdj.erpo

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.jdbc.core.JdbcAggregateOperations
import org.springframework.data.jdbc.core.convert.JdbcConverter
import org.springframework.data.jdbc.repository.support.SimpleJdbcRepository
import org.springframework.data.mapping.PersistentEntity
import org.springframework.data.repository.findByIdOrNull
import org.springframework.transaction.annotation.Transactional
import pro.qyoga.platform.spring.sdj.ALL
import pro.qyoga.platform.spring.sdj.erpo.hydration.FetchSpec
import pro.qyoga.platform.spring.sdj.erpo.hydration.hydrate
import pro.qyoga.platform.spring.sdj.mapContent
import pro.qyoga.platform.spring.sdj.query.QueryBuilder
import pro.qyoga.platform.spring.sdj.query.query
import kotlin.reflect.KProperty1


class ErgoRepository<T : Any, ID : Any>(
    val jdbcAggregateTemplate: JdbcAggregateOperations,
    val entity: PersistentEntity<T, *>,
    jdbcConverter: JdbcConverter
) : SimpleJdbcRepository<T, ID>(jdbcAggregateTemplate, entity, jdbcConverter) {

    @Transactional
    fun update(id: ID, func: (T) -> T): T {
        val task = findByIdOrNull(id)
            ?: throw AggregateNotFound(id, entity.type.kotlin)

        val updatedTask = func(task)
        return save(updatedTask)
    }

    fun findById(
        id: ID,
        fetch: Iterable<KProperty1<T, *>> = emptySet(),
    ): T? {
        return findByIdOrNull(id)
            ?.let { jdbcAggregateTemplate.hydrate(listOf(it), FetchSpec(fetch)).single() }
    }

    fun findOne(
        fetch: Iterable<KProperty1<T, *>> = emptySet(),
        queryBuilder: QueryBuilder.() -> Unit
    ): T? {
        val query = query(queryBuilder)
        return jdbcAggregateTemplate.findOne(query, entity.type)
            .map { jdbcAggregateTemplate.hydrate(listOf(it), FetchSpec(fetch)).single() }
            .orElse(null)
    }

    fun exists(
        queryBuilder: QueryBuilder.() -> Unit
    ): Boolean {
        val query = query(queryBuilder)
        return jdbcAggregateTemplate.exists(query, entity.type)
    }

    fun findAll(
        pageRequest: PageRequest = ALL,
        fetch: Iterable<KProperty1<T, *>> = emptySet(),
        queryBuilder: QueryBuilder.() -> Unit = {}
    ): Page<T> {
        val query = query(queryBuilder)
        val res = jdbcAggregateTemplate.findAll(query, entity.type, pageRequest)
        return res.mapContent { jdbcAggregateTemplate.hydrate(res, FetchSpec(fetch)) }
    }

}