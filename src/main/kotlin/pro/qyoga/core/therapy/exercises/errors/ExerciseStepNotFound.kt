package pro.qyoga.core.therapy.exercises.errors

import pro.qyoga.platform.errors.DomainError

class ExerciseStepNotFound(exerciseId: Long, stepIdx: Int, stepsCount: Int) :
    DomainError("Exercise $exerciseId has $stepsCount steps, but $stepIdx-th step is requested")
