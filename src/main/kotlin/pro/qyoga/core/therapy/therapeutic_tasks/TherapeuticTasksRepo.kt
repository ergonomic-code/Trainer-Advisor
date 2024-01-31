package pro.qyoga.core.therapy.therapeutic_tasks

import org.springframework.dao.DuplicateKeyException
import org.springframework.data.domain.Pageable
import org.springframework.data.jdbc.core.JdbcAggregateOperations
import org.springframework.data.jdbc.core.convert.JdbcConverter
import org.springframework.data.mapping.model.BasicPersistentEntity
import org.springframework.data.relational.core.conversion.DbActionExecutionException
import org.springframework.data.relational.core.mapping.RelationalMappingContext
import org.springframework.data.util.TypeInformation
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import pro.qyoga.core.therapy.therapeutic_tasks.errors.DuplicatedTherapeuticTaskName
import pro.qyoga.core.therapy.therapeutic_tasks.model.TherapeuticTask
import pro.qyoga.platform.spring.sdj.erpo.ErgoRepository
import pro.qyoga.platform.spring.sdj.withSortBy


@Repository
class TherapeuticTasksRepo(
    jdbcAggregateTemplate: JdbcAggregateOperations,
    namedParameterJdbcOperations: NamedParameterJdbcOperations,
    jdbcConverter: JdbcConverter,
    relationalMappingContext: RelationalMappingContext,
) : ErgoRepository<TherapeuticTask, Long>(
    jdbcAggregateTemplate,
    namedParameterJdbcOperations,
    BasicPersistentEntity(TypeInformation.of(TherapeuticTask::class.java)),
    jdbcConverter,
    relationalMappingContext
) {

    object Page {
        val topFiveByName = Pageable.ofSize(5).withSortBy(TherapeuticTask::name)
        val topTenByName = Pageable.ofSize(10).withSortBy(TherapeuticTask::name)
    }

    @Transactional
    override fun <S : TherapeuticTask?> save(instance: S & Any): S & Any {
        val result = runCatching { super.save(instance) }
        val ex = result.exceptionOrNull()
        if ((ex as? DbActionExecutionException)?.cause is DuplicateKeyException) {
            throw DuplicatedTherapeuticTaskName(instance, ex.cause as DuplicateKeyException)
        }

        return result.getOrThrow()
    }

    @Transactional
    fun getOrCreate(therapeuticTask: TherapeuticTask): TherapeuticTask {
        var persistedTask = findOne {
            TherapeuticTask::name isEqual therapeuticTask.name
        }

        if (persistedTask == null) {
            persistedTask = save(therapeuticTask)
        }

        return persistedTask
    }

}

fun TherapeuticTasksRepo.findByNameContaining(
    therapistId: Long,
    searchKey: String?,
    page: Pageable
): Iterable<TherapeuticTask> {
    return findAll(page) {
        TherapeuticTask::owner isEqual therapistId
        TherapeuticTask::name isILikeIfNotNull searchKey
    }
}

fun TherapeuticTasksRepo.findByName(name: String): TherapeuticTask? =
    findOne {
        TherapeuticTask::name isEqual name
    }
