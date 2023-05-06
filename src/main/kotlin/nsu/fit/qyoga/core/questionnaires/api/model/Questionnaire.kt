package nsu.fit.qyoga.core.questionnaires.api.model

import org.springframework.context.annotation.Scope
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import org.springframework.stereotype.Component

@Table("questionnaires")
data class Questionnaire(
    @Id
    val id: Long,
    val title: String
)
