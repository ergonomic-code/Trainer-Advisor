package pro.azhidkov.platform.spring.sdj.erpo

import org.springframework.dao.DuplicateKeyException
import org.springframework.data.domain.*
import org.springframework.data.jdbc.core.JdbcAggregateOperations
import org.springframework.data.jdbc.core.convert.EntityRowMapper
import org.springframework.data.jdbc.core.convert.JdbcConverter
import org.springframework.data.jdbc.repository.support.SimpleJdbcRepository
import org.springframework.data.relational.core.conversion.DbActionExecutionException
import org.springframework.data.relational.core.mapping.RelationalMappingContext
import org.springframework.data.relational.core.mapping.RelationalPersistentEntity
import org.springframework.data.relational.core.query.Query
import org.springframework.data.relational.core.sql.SqlIdentifier
import org.springframework.data.repository.findByIdOrNull
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.transaction.annotation.Transactional
import pro.azhidkov.platform.spring.jdbc.queryForPage
import pro.azhidkov.platform.spring.sdj.erpo.hydration.FetchSpec
import pro.azhidkov.platform.spring.sdj.erpo.hydration.hydrate
import pro.azhidkov.platform.spring.sdj.findOneBy
import pro.azhidkov.platform.spring.sdj.mapContent
import pro.azhidkov.platform.spring.sdj.query.QueryBuilder
import pro.azhidkov.platform.spring.sdj.query.query
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1


class ErgoRepository<T : Any, ID : Any>(
    private val jdbcAggregateTemplate: JdbcAggregateOperations,
    private val namedParameterJdbcOperations: NamedParameterJdbcOperations,
    type: KClass<T>,
    jdbcConverter: JdbcConverter,
    relationalMappingContext: RelationalMappingContext,
) : SimpleJdbcRepository<T, ID>(
    jdbcAggregateTemplate,
    relationalMappingContext.getRelationPersistentEntity(type), jdbcConverter
) {

    private final val relationalPersistentEntity: RelationalPersistentEntity<T> =
        relationalMappingContext.getRelationPersistentEntity(type)

    protected val rowMapper = EntityRowMapper(relationalPersistentEntity, jdbcConverter)

    protected fun <S : T> saveAndMapDuplicatedKeyException(aggregate: T, map: (DuplicateKeyException) -> Throwable): S {
        val res = runCatching { super.save(aggregate) }
        when (val ex = (res.exceptionOrNull() as? DbActionExecutionException)?.cause as? DuplicateKeyException) {
            is DuplicateKeyException -> throw map(ex)
        }

        @Suppress("UNCHECKED_CAST")
        return res.getOrThrow() as S
    }

    fun upsert(upsertQuery: String, vararg params: Pair<String, Any?>): ID {
        val keyHolder = GeneratedKeyHolder()

        @Suppress("SqlSourceToSinkFlow")
        namedParameterJdbcOperations.update(
            upsertQuery,
            MapSqlParameterSource(params.toMap()),
            keyHolder
        )

        @Suppress("UNCHECKED_CAST")
        return keyHolder.keys?.values?.first() as ID
    }

    @Transactional
    fun updateById(id: ID, func: (T) -> T): T? {
        val aggregate = findByIdOrNull(id)
            ?: return null

        val updatedAggregate = func(aggregate)
        return save(updatedAggregate)
    }

    @Transactional
    fun updateOne(query: Query, func: (T) -> T): T? {
        val aggregate = findOne(query)
            ?: return null

        val updatedAggregate = func(aggregate)
        return save(updatedAggregate)
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
        return jdbcAggregateTemplate.findOne(query, relationalPersistentEntity.type)
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
        @Suppress("SqlSourceToSinkFlow")
        val aggregate = namedParameterJdbcOperations.query(query, paramMap, rowMapper)
            .firstOrNull()
            ?: return null

        return jdbcAggregateTemplate.hydrate(listOf(aggregate), FetchSpec(fetch)).single()
    }

    fun findOne(
        query: Query,
        fetch: Iterable<KProperty1<T, *>> = emptySet(),
    ): T? {
        val aggregate = jdbcAggregateTemplate.findOneBy(query, relationalPersistentEntity.type)
            ?: return null

        return jdbcAggregateTemplate.hydrate(listOf(aggregate), FetchSpec(fetch)).single()
    }

    fun exists(
        queryBuilder: QueryBuilder.() -> Unit
    ): Boolean {
        val query = query(queryBuilder)
        return jdbcAggregateTemplate.exists(query, relationalPersistentEntity.type)
    }

    fun findPage(
        pageRequest: Pageable,
        fetch: Iterable<KProperty1<T, *>> = emptySet(),
        queryBuilder: QueryBuilder.() -> Unit = {}
    ): Page<T> {
        val query = query(queryBuilder)
        val res = jdbcAggregateTemplate.findAll(query, relationalPersistentEntity.type, pageRequest)
        return res.mapContent {
            jdbcAggregateTemplate.hydrate(
                res,
                FetchSpec(fetch)
            )
        }
    }

    fun findSlice(
        pageRequest: Pageable,
        fetch: Iterable<KProperty1<T, *>> = emptySet(),
        queryBuilder: QueryBuilder.() -> Unit = {}
    ): Slice<T> {
        val query = query(queryBuilder)
        val res = jdbcAggregateTemplate.findAll(
            query,
            relationalPersistentEntity.type,
            PageRequest.of(pageRequest.pageNumber, pageRequest.pageSize + 1, pageRequest.sort)
        )

        val hydratedPage = res.content
            .take(pageRequest.pageSize)
            .let { jdbcAggregateTemplate.hydrate(it, FetchSpec(fetch)) }
        val hasMore = res.totalElements > pageRequest.pageSize

        return SliceImpl(hydratedPage, pageRequest, hasMore)
    }

    fun findPage(
        query: String,
        paramMap: Map<String, Any?>,
        pageRequest: Pageable,
        fetch: Iterable<KProperty1<T, *>> = emptySet(),
    ): Page<T> {
        val page = namedParameterJdbcOperations.queryForPage(query, paramMap, pageRequest, rowMapper)
        return page.mapContent { jdbcAggregateTemplate.hydrate(it, FetchSpec(fetch)) }
    }

    fun findAll(
        fetch: Iterable<KProperty1<T, *>> = emptySet(),
    ): Iterable<T> {
        val rows = jdbcAggregateTemplate.findAll(relationalPersistentEntity.type)
        return jdbcAggregateTemplate.hydrate(rows, FetchSpec(fetch))
    }

    fun findAll(
        query: String,
        paramMap: Map<String, Any?>,
        fetch: Iterable<KProperty1<T, *>> = emptySet(),
    ): Iterable<T> {
        @Suppress("SqlSourceToSinkFlow")
        val rows = namedParameterJdbcOperations.query(query, paramMap, rowMapper)
        return jdbcAggregateTemplate.hydrate(rows, FetchSpec(fetch))
    }

    private fun getColumnNameToSortBy(order: Sort.Order): SqlIdentifier {
        val propertyToSortBy = relationalPersistentEntity.getPersistentProperty(order.property)
        if (propertyToSortBy != null) {
            return propertyToSortBy.columnName
        }

        TODO("Implement embedded (?) property to column resolution")
    }

}

@Suppress("UNCHECKED_CAST")
private fun <T : Any> RelationalMappingContext.getRelationPersistentEntity(
    type: KClass<T>
): RelationalPersistentEntity<T> = getRequiredPersistentEntity(type.java) as RelationalPersistentEntity<T>