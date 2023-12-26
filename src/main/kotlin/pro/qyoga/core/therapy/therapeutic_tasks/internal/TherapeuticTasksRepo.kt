package pro.qyoga.core.therapy.therapeutic_tasks.internal

import org.springframework.data.domain.Pageable
import org.springframework.data.jdbc.core.JdbcAggregateOperations
import org.springframework.data.jdbc.core.convert.JdbcConverter
import org.springframework.data.jdbc.repository.support.SimpleJdbcRepository
import org.springframework.data.mapping.model.BasicPersistentEntity
import org.springframework.data.util.TypeInformation
import org.springframework.stereotype.Repository
import pro.qyoga.core.therapy.therapeutic_tasks.api.TherapeuticTask
import pro.qyoga.platform.spring.sdj.findAllBy
import pro.qyoga.platform.spring.sdj.findOneBy


@Repository
class TherapeuticTasksRepo(
    private val jdbcAggregateTemplate: JdbcAggregateOperations,
    jdbcConverter: JdbcConverter
) : SimpleJdbcRepository<TherapeuticTask, Long>(
    jdbcAggregateTemplate,
    BasicPersistentEntity(TypeInformation.of(TherapeuticTask::class.java)),
    jdbcConverter
) {

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