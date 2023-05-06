package nsu.fit.qyoga.core.questionnaires.api.dtos

import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

@Component
@Scope("session")
data class testDto(
    val testText: String,
    val value: Long
)
