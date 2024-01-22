package pro.qyoga.core.therapy.therapeutic_tasks.internal

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import pro.qyoga.core.therapy.therapeutic_tasks.api.TherapeuticTask


@Repository
interface TherapeuticTaskMRepo : CrudRepository<TherapeuticTask, Long>