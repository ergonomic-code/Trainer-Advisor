package pro.qyoga.core.therapy.exercises.internal

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import pro.qyoga.core.therapy.exercises.api.model.Exercise


@Repository
interface ExercisesMRepo : CrudRepository<Exercise, Long>