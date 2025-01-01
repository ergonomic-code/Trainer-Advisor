package pro.qyoga.core.therapy.programs.views

import pro.azhidkov.platform.NamedEntity
import pro.qyoga.core.therapy.exercises.dtos.ExerciseSummaryDto
import pro.qyoga.core.therapy.therapeutic_tasks.model.TherapeuticTask


data class ProgramSummaryView(
    val id: Long,
    val title: String,
    val therapeuticTask: NamedEntity<TherapeuticTask, Long>,
    val exercises: List<ExerciseSummaryDto>,
)