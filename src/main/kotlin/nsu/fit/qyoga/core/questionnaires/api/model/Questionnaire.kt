package nsu.fit.qyoga.core.questionnaires.api.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("questionnaires")
class Questionnaire(
    @Id
    val id: Long,
    val title: String
)