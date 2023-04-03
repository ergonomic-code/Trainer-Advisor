package nsu.fit.qyoga.core.programs.api.dtos

import java.time.Instant

data class ProgramSearchDto(
    val title: String? = null,
    val date: Instant? = null,
    val therapeuticPurpose: String? = null
)
