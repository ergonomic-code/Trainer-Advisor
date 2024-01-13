package pro.qyoga.core.therapy.exercises.api

import pro.qyoga.platform.errors.DomainError

class ExerciseNotFound(exerciseId: Long) : DomainError("Cannot find exercise by id $exerciseId")
