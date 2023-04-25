package nsu.fit.qyoga.core.questionnaires.api.model

import nsu.fit.qyoga.core.questionnaires.api.dtos.enums.QuestionType
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.MappedCollection
import org.springframework.data.relational.core.mapping.Table

@Table("questions")
data class Question(
    @Id
    val id: Long = 0,
    val title: String?,
    val questionType: QuestionType,
    val questionnaireId: Long,
    val imageId: Long?,
    @MappedCollection(idColumn = "question_id", keyColumn = "id")
    val answers: MutableList<Answer>
)
