package pro.qyoga.infra.db

import org.postgresql.util.PGInterval
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.CustomConversions
import org.springframework.data.jdbc.core.JdbcAggregateTemplate
import org.springframework.data.jdbc.core.convert.*
import org.springframework.data.mapping.PersistentPropertyPath
import org.springframework.data.mapping.model.SimpleTypeHolder
import org.springframework.data.relational.core.dialect.PostgresDialect
import org.springframework.data.relational.core.mapping.RelationalMappingContext
import org.springframework.data.relational.core.mapping.RelationalPersistentProperty
import org.springframework.data.relational.core.sql.IdentifierProcessing
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import pro.qyoga.platform.spring.sdj.DurationToPGIntervalConverter
import pro.qyoga.platform.spring.sdj.PGIntervalToDurationConverter
import javax.sql.DataSource


@ImportAutoConfiguration(DataSourceAutoConfiguration::class, FlywayAutoConfiguration::class)
@Configuration
class SdjConfig(
    private val applicationContextEventPublisher: ApplicationEventPublisher,
    private val dataSource: DataSource,
) {

    @Bean
    fun dialect(): PostgresDialect = PostgresDialect.INSTANCE

    @Bean
    fun jdbcAggregateTemplate() = JdbcAggregateTemplate(
        applicationContextEventPublisher,
        relationalMappingContext(),
        jdbcConverter(),
        relationResolver()
    )

    @Bean
    fun jdbcConverter(): BasicJdbcConverter {
        val relationResolverLazyProxy = object : RelationResolver {

            val actual by lazy { relationResolver() }

            override fun findAllByPath(
                identifier: Identifier,
                path: PersistentPropertyPath<out RelationalPersistentProperty>
            ): MutableIterable<Any> {
                return actual.findAllByPath(identifier, path)
            }

        }
        return BasicJdbcConverter(
            relationalMappingContext(),
            relationResolverLazyProxy,
            JdbcCustomConversions(
                CustomConversions.StoreConversions.of(
                    SimpleTypeHolder(setOf(PGInterval::class.java), true),
                    listOf(
                        DurationToPGIntervalConverter(),
                        PGIntervalToDurationConverter()
                    )
                ),
                emptyList<Converter<*, *>>()
            ),
            DefaultJdbcTypeFactory(namedParameterJdbcTemplate().jdbcOperations),
            IdentifierProcessing.ANSI
        )
    }

    @Bean
    fun relationalMappingContext() = RelationalMappingContext()

    @Bean
    fun relationResolver(): DefaultDataAccessStrategy {
        val namedParameterJdbcTemplate = namedParameterJdbcTemplate()
        return DefaultDataAccessStrategy(
            SqlGeneratorSource(relationalMappingContext(), jdbcConverter(), dialect()),
            relationalMappingContext(),
            jdbcConverter(),
            namedParameterJdbcTemplate,
            SqlParametersFactory(relationalMappingContext(), jdbcConverter()),
            InsertStrategyFactory(
                namedParameterJdbcTemplate,
                BatchJdbcOperations(namedParameterJdbcTemplate.jdbcTemplate),
                dialect()
            )
        )
    }

    @Bean
    fun namedParameterJdbcTemplate() =
        NamedParameterJdbcTemplate(dataSource)

}