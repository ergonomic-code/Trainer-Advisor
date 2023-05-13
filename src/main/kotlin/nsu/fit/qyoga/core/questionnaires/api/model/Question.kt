package nsu.fit.qyoga.core.questionnaires.api.model

import nsu.fit.qyoga.core.images.api.model.Image
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
    @MappedCollection(idColumn = "id")
    val image: Image?,
    @MappedCollection(idColumn = "question_id")
    val answers: Set<Answer>
)
