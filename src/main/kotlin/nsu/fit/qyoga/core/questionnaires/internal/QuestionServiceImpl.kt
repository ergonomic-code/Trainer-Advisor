package nsu.fit.qyoga.core.questionnaires.internal

import nsu.fit.qyoga.core.questionnaires.api.dtos.QuestionDto
import nsu.fit.qyoga.core.questionnaires.api.model.Question
import nsu.fit.qyoga.core.questionnaires.api.services.QuestionService
import org.springframework.stereotype.Service

@Service
class QuestionServiceImpl(
    private val questionRepo: QuestionRepo
) : QuestionService {

    override fun createQuestion(questionDto: QuestionDto, questionnaireId: Long, imageId: Long) {
        val questionToSave = Question(
            text = questionDto.text?: "",
            questionType = questionDto.questionType,
            questionnaireId = questionnaireId,
            imageId = imageId
        )
        questionRepo.save(questionToSave)
    }
}