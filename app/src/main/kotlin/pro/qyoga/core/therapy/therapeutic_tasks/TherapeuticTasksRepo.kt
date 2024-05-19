package pro.qyoga.core.therapy.therapeutic_tasks

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jdbc.core.JdbcAggregateOperations
import org.springframework.data.jdbc.core.convert.JdbcConverter
import org.springframework.data.mapping.model.BasicPersistentEntity
import org.springframework.data.relational.core.mapping.RelationalMappingContext
import org.springframework.data.util.TypeInformation
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import pro.azhidkov.platform.spring.sdj.erpo.ErgoRepository
import pro.azhidkov.platform.spring.sdj.withSortBy
import pro.qyoga.core.therapy.therapeutic_tasks.errors.DuplicatedTherapeuticTaskName
import pro.qyoga.core.therapy.therapeutic_tasks.model.TherapeuticTask


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
        return saveAndMapDuplicatedKeyException(instance) { ex ->
            DuplicatedTherapeuticTaskName(instance, ex)
        }
    }

    @Transactional
    fun getOrCreate(therapeuticTask: TherapeuticTask): TherapeuticTask {
        var persistedTask = findOne {
            TherapeuticTask::owner isEqual therapeuticTask.owner
            TherapeuticTask::name isEqual therapeuticTask.name
        }

        if (persistedTask == null) {
            persistedTask = save(therapeuticTask)
        }

        return persistedTask
    }

}

fun TherapeuticTasksRepo.findTherapistTasksSliceByName(
    therapistId: Long,
    searchKey: String?,
    page: Pageable
): Slice<TherapeuticTask> {
    return findAll(page) {
        TherapeuticTask::owner isEqual therapistId
        TherapeuticTask::name isILikeIfNotNull searchKey
    }
}

fun TherapeuticTasksRepo.findOneByName(name: String): TherapeuticTask? =
    findOne {
        TherapeuticTask::name isEqual name
    }
