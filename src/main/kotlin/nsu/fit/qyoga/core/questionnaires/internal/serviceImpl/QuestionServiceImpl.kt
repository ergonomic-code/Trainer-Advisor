package nsu.fit.qyoga.core.questionnaires.internal.serviceImpl

import nsu.fit.qyoga.core.questionnaires.api.dtos.CreateQuestionDto
import nsu.fit.qyoga.core.questionnaires.api.model.Question
import nsu.fit.qyoga.core.questionnaires.api.services.QuestionService
import nsu.fit.qyoga.core.questionnaires.internal.repository.QuestionRepo
import org.springframework.stereotype.Service

@Service
class QuestionServiceImpl(
    private val questionRepo: QuestionRepo
) : QuestionService {

    override fun createQuestion(createQuestionDto: CreateQuestionDto, questionnaireId: Long, imageId: Long?) {
        val questionToSave = Question(
            text = createQuestionDto.text?: "",
            questionType = createQuestionDto.questionType,
            questionnaireId = questionnaireId,
            imageId = imageId
        )
        questionRepo.save(questionToSave)
    }

    override fun loadQuestionsWithAnswers(id: Long): List<CreateQuestionDto> {
        TODO("Not yet implemented")
    }
}