package pro.azhidkov.platform.spring.sdj.erpo

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jdbc.core.JdbcAggregateOperations
import org.springframework.data.jdbc.core.convert.EntityRowMapper
import org.springframework.data.jdbc.core.convert.JdbcConverter
import org.springframework.data.jdbc.repository.support.SimpleJdbcRepository
import org.springframework.data.mapping.PersistentEntity
import org.springframework.data.relational.core.mapping.RelationalMappingContext
import org.springframework.data.relational.core.mapping.RelationalPersistentEntity
import org.springframework.data.relational.core.sql.SqlIdentifier
import org.springframework.data.repository.findByIdOrNull
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.transaction.annotation.Transactional
import pro.azhidkov.platform.spring.jdbc.queryForPage
import pro.azhidkov.platform.spring.sdj.ALL
import pro.azhidkov.platform.spring.sdj.erpo.hydration.FetchSpec
import pro.azhidkov.platform.spring.sdj.erpo.hydration.hydrate
import pro.azhidkov.platform.spring.sdj.mapContent
import pro.azhidkov.platform.spring.sdj.query.QueryBuilder
import pro.azhidkov.platform.spring.sdj.query.query
import kotlin.reflect.KProperty1


class ErgoRepository<T : Any, ID : Any>(
    private val jdbcAggregateTemplate: JdbcAggregateOperations,
    private val namedParameterJdbcOperations: NamedParameterJdbcOperations,
    private val entity: PersistentEntity<T, *>,
    jdbcConverter: JdbcConverter,
    relationalMappingContext: RelationalMappingContext,
) : SimpleJdbcRepository<T, ID>(jdbcAggregateTemplate, entity, jdbcConverter) {

    @Suppress("UNCHECKED_CAST")
    private final val relationalPersistentEntity =
        relationalMappingContext.getRequiredPersistentEntity(entity.type) as RelationalPersistentEntity<T>

    protected val rowMapper = EntityRowMapper(relationalPersistentEntity, jdbcConverter)

    @Transactional
    fun update(id: ID, func: (T) -> T): T? {
        val task = findByIdOrNull(id)
            ?: return null

        val updatedTask = func(task)
        return save(updatedTask)
    }

    fun findById(
        id: ID,
        fetch: Iterable<KProperty1<T, *>> = emptySet(),
    ): T? {
        return findByIdOrNull(id)
            ?.let {
                jdbcAggregateTemplate.hydrate(
                    listOf(it),
                    FetchSpec(fetch)
                ).single()
            }
    }

    fun findOne(
        fetch: Iterable<KProperty1<T, *>> = emptySet(),
        queryBuilder: QueryBuilder.() -> Unit
    ): T? {
        val query = query(queryBuilder)
        return jdbcAggregateTemplate.findOne(query, entity.type)
            .map {
                jdbcAggregateTemplate.hydrate(
                    listOf(it),
                    FetchSpec(fetch)
                ).single()
            }
            .orElse(null)
    }

    fun findOne(
        query: String,
        paramMap: Map<String, Any?>,
        fetch: Iterable<KProperty1<T, *>> = emptySet(),
    ): T? {
        val aggregate = namedParameterJdbcOperations.query(query, paramMap, rowMapper)
            .firstOrNull()
            ?: return null

        return jdbcAggregateTemplate.hydrate(listOf(aggregate), FetchSpec(fetch)).single()
    }

    fun exists(
        queryBuilder: QueryBuilder.() -> Unit
    ): Boolean {
        val query = query(queryBuilder)
        return jdbcAggregateTemplate.exists(query, entity.type)
    }

    fun findAll(
        pageRequest: Pageable = ALL,
        fetch: Iterable<KProperty1<T, *>> = emptySet(),
        queryBuilder: QueryBuilder.() -> Unit = {}
    ): Page<T> {
        val query = query(queryBuilder)
        val res = jdbcAggregateTemplate.findAll(query, entity.type, pageRequest)
        return res.mapContent {
            jdbcAggregateTemplate.hydrate(
                res,
                FetchSpec(fetch)
            )
        }
    }

    fun findSlice(
        pageRequest: Pageable = ALL,
        fetch: Iterable<KProperty1<T, *>> = emptySet(),
        queryBuilder: QueryBuilder.() -> Unit = {}
    ): Iterable<T> {
        val query = query(queryBuilder)
        val res = jdbcAggregateTemplate.findAll(query, entity.type, pageRequest)
            .content
        return res.let {
            jdbcAggregateTemplate.hydrate(
                res,
                FetchSpec(fetch)
            )
        }
    }

    fun findPage(
        query: String,
        paramMap: Map<String, Any?>,
        pageRequest: Pageable = ALL,
        fetch: Iterable<KProperty1<T, *>> = emptySet(),
    ): Page<T> {
        val page = namedParameterJdbcOperations.queryForPage(query, paramMap, pageRequest, rowMapper)
        return page.mapContent { jdbcAggregateTemplate.hydrate(it, FetchSpec(fetch)) }
    }

    fun findAll(
        query: String,
        paramMap: Map<String, Any?>,
        fetch: Iterable<KProperty1<T, *>> = emptySet(),
    ): Collection<T> {
        val rows = namedParameterJdbcOperations.query(query, paramMap, rowMapper)
        return jdbcAggregateTemplate.hydrate(rows, FetchSpec(fetch))
    }

    fun findSlice(
        query: String,
        paramMap: Map<String, Any?>,
        pageRequest: Pageable,
        fetch: Iterable<KProperty1<T, *>> = emptySet(),
    ): Collection<T> {
        val sortOrders =
            pageRequest.sort.map { "${getColumnNameToSortBy(it).reference} ${it.direction.name}" }.joinToString(", ")
        val sliceQuery =
            """SELECT * from ($query) AS data ORDER BY $sortOrders OFFSET ${pageRequest.offset} LIMIT ${pageRequest.pageSize}"""
        val rows = namedParameterJdbcOperations.query(sliceQuery, paramMap, rowMapper)

        return jdbcAggregateTemplate.hydrate(
            rows,
            FetchSpec(fetch)
        )
    }


    private fun getColumnNameToSortBy(order: Sort.Order): SqlIdentifier {
        val propertyToSortBy = relationalPersistentEntity.getPersistentProperty(order.property)
        if (propertyToSortBy != null) {
            return propertyToSortBy.columnName
        }

        TODO("Implement embedded (?) property to column resolution")
    }

}