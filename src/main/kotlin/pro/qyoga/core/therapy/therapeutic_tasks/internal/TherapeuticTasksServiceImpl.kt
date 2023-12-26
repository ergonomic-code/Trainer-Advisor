package pro.qyoga.core.therapy.therapeutic_tasks.internal

import org.springframework.data.jdbc.core.mapping.AggregateReference
import org.springframework.stereotype.Service
import pro.qyoga.core.therapy.therapeutic_tasks.api.TherapeuticTask
import pro.qyoga.core.therapy.therapeutic_tasks.api.TherapeuticTasksService


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

}