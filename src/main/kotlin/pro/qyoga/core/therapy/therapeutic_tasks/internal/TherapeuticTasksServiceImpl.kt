package pro.qyoga.core.therapy.therapeutic_tasks.internal

import org.springframework.data.domain.Pageable
import org.springframework.data.jdbc.core.mapping.AggregateReference
import org.springframework.stereotype.Service
import pro.qyoga.core.therapy.therapeutic_tasks.api.TherapeuticTask
import pro.qyoga.core.therapy.therapeutic_tasks.api.TherapeuticTasksService
import pro.qyoga.platform.spring.sdj.withSortBy


@Service
class TherapeuticTasksServiceImpl(
    private val therapeuticTasksRepo: TherapeuticTasksRepo
) : TherapeuticTasksService {

    override fun getOrCreate(therapistId: Long, therapeuticTaskName: String): TherapeuticTask {
        return therapeuticTasksRepo.getOrCreate(
            TherapeuticTask(
                AggregateReference.to(therapistId),
                therapeuticTaskName
            )
        )
    }

    override fun findAllById(ids: List<Long>): Map<Long, TherapeuticTask> {
        return therapeuticTasksRepo.findAllById(ids)
            .associateBy { it.id }
    }

    override fun findByNameContaining(searchKey: String): Iterable<TherapeuticTask> {
        val page = Pageable.ofSize(5).withSortBy(TherapeuticTask::name)

        return therapeuticTasksRepo.findByNameContaining(searchKey, page)
    }

}