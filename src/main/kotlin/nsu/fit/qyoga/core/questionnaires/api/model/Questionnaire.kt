package nsu.fit.qyoga.core.questionnaires.api.model

import org.springframework.context.annotation.Scope
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.MappedCollection
import org.springframework.data.relational.core.mapping.Table
import org.springframework.stereotype.Component

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
