package nsu.fit.qyoga.core.questionnaires.api.model

import nsu.fit.qyoga.core.questionnaires.api.enums.QuestionType
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.nio.file.Path

@Table("questions")
data class Question(
    @Id
    val id: Long = 0,
    val text: String,
    val questionType: QuestionType,
    val questionnaireId: Long,
    val imageId: Long
)