package nsu.fit.qyoga.core.programs.api.dtos

import java.time.Instant

data class ProgramDto(
    val id: Long,
    val title: String,
    val date: Instant,
    val therapeuticPurpose: String
)
