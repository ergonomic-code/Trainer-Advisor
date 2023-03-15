package nsu.fit.qyoga.core.questionnaires.api.dtos

import nsu.fit.qyoga.core.questionnaires.api.enums.QuestionType
import org.springframework.web.multipart.MultipartFile

data class QuestionDto(
    val text: String?,
    val questionType: QuestionType,
    val photo: MultipartFile?
)