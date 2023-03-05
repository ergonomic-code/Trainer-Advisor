package nsu.fit.qyoga.core.questionnaires.api.dtos

import org.springframework.data.annotation.Id

class QuestionnaireDto(
    val id: Long,
    val title: String?
)