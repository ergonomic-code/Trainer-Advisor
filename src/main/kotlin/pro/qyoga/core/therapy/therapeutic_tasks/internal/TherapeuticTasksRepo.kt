package pro.qyoga.core.therapy.therapeutic_tasks.internal

import org.springframework.dao.DuplicateKeyException
import org.springframework.data.domain.Pageable
import org.springframework.data.jdbc.core.JdbcAggregateOperations
import org.springframework.data.jdbc.core.convert.JdbcConverter
import org.springframework.data.mapping.model.BasicPersistentEntity
import org.springframework.data.relational.core.conversion.DbActionExecutionException
import org.springframework.data.util.TypeInformation
import org.springframework.stereotype.Repository
import pro.qyoga.core.therapy.therapeutic_tasks.api.DuplicatedTherapeuticTaskName
import pro.qyoga.core.therapy.therapeutic_tasks.api.TherapeuticTask
import pro.qyoga.platform.spring.sdj.erpo.ErgoRepository
import pro.qyoga.platform.spring.sdj.findAllBy
import pro.qyoga.platform.spring.sdj.findOneBy


@Repository
class TherapeuticTasksRepo(
    override val jdbcAggregateTemplate: JdbcAggregateOperations,
    jdbcConverter: JdbcConverter
) : ErgoRepository<TherapeuticTask, Long>(
    jdbcAggregateTemplate,
    BasicPersistentEntity(TypeInformation.of(TherapeuticTask::class.java)),
    jdbcConverter
) {

    override fun <S : TherapeuticTask?> save(instance: S & Any): S & Any {
        val result = runCatching { super.save(instance) }
        val ex = result.exceptionOrNull()
        if ((ex as? DbActionExecutionException)?.cause is DuplicateKeyException) {
            throw DuplicatedTherapeuticTaskName(instance, ex.cause as DuplicateKeyException)
        }

        return result.getOrThrow()
    }

    fun getOrCreate(therapeuticTask: TherapeuticTask): TherapeuticTask {
        var persistedTask = jdbcAggregateTemplate.findOneBy<TherapeuticTask> {
            TherapeuticTask::name isEqual therapeuticTask.name
        }

        if (persistedTask == null) {
            persistedTask = jdbcAggregateTemplate.save(therapeuticTask)
        }

        return persistedTask
    }

    fun findByNameContaining(searchKey: String?, page: Pageable): Iterable<TherapeuticTask> {
        return jdbcAggregateTemplate.findAllBy<TherapeuticTask>(page) {
            TherapeuticTask::name isILikeIfNotNull searchKey
        }
    }

}