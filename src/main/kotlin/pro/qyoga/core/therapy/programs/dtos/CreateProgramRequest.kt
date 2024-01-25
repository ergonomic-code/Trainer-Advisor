package pro.qyoga.core.therapy.programs.dtos

data class CreateProgramRequest(
    val title: String,
    val exerciseIds: List<Long>
)