package pro.qyoga.core.therapy.therapeutic_tasks.api


interface TherapeuticTasksService {

    fun getOrCreate(therapistId: Long, therapeuticTaskName: String): TherapeuticTask

    fun findAllById(ids: List<Long>): Map<Long, TherapeuticTask>

}