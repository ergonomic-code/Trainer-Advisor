package nsu.fit.qyoga.core.questionnaires.api.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.MappedCollection
import org.springframework.data.relational.core.mapping.Table

@Table("questionnaires")
data class Questionnaire(
    @Id
    val id: Long = 0,
    val title: String,
    @MappedCollection(idColumn = "questionnaire_id")
    val question: Set<Question>,
    @MappedCollection(idColumn = "questionnaire_id")
    val decoding: Set<Decoding>
)
