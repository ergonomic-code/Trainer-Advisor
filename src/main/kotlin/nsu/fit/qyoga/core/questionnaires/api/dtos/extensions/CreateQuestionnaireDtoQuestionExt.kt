package nsu.fit.qyoga.core.questionnaires.api.dtos.extensions

import nsu.fit.qyoga.core.questionnaires.api.dtos.CreateAnswerDto
import nsu.fit.qyoga.core.questionnaires.api.dtos.CreateQuestionDto
import nsu.fit.qyoga.core.questionnaires.api.dtos.CreateQuestionnaireDto
import nsu.fit.qyoga.core.questionnaires.api.errors.ElementNotFound

fun CreateQuestionnaireDto.addQuestionImage(
    questionId: Long,
    imageId: Long
): CreateQuestionnaireDto {
    return this.copy(
        question = modifyQuestion(questionId) {
            it.copy(imageId = imageId)
        }.toMutableList()
    )
}

fun CreateQuestionnaireDto.deleteQuestionImage(
    questionId: Long
): CreateQuestionnaireDto {
    return this.copy(
        question = modifyQuestion(questionId) {
            it.copy(imageId = null)
        }.toMutableList()
    )
}

fun CreateQuestionnaireDto.deleteQuestion(questionId: Long): CreateQuestionnaireDto =
    this.copy(question = this.question.filter { it.id != questionId }.toMutableList())

fun CreateQuestionnaireDto.addQuestion(): CreateQuestionnaireDto {
    val lastId = if (this.question.isEmpty()) 0 else this.question.last().id + 1
    val newQuestion = CreateQuestionDto(id = lastId, answers = listOf(CreateAnswerDto()))
    return this.copy(question = (this.question + newQuestion).toMutableList())
}

fun CreateQuestionnaireDto.changeQuestionType(
    questionId: Long,
    changedQuestion: CreateQuestionDto
): CreateQuestionnaireDto {
    return this.copy(
        question = modifyQuestion(questionId) {
            it.copy(
                questionType = changedQuestion.questionType,
                answers = listOf(CreateAnswerDto())
            )
        }.toMutableList()
    )
}

fun CreateQuestionnaireDto.updateQuestion(
    questionId: Long,
    changedQuestion: CreateQuestionDto
): CreateQuestionnaireDto {
    return this.copy(
        question = modifyQuestion(questionId) {
            it.copy(
                title = changedQuestion.title,
                questionType = changedQuestion.questionType,
                answers = changedQuestion.answers
            )
        }.toMutableList()
    )
}

fun CreateQuestionnaireDto.modifyQuestion(
    questionId: Long,
    body: (CreateQuestionDto) -> CreateQuestionDto
): List<CreateQuestionDto> {
    var isFound = false
    val questionList = this.question.map { question ->
        if (question.id == questionId) {
            isFound = true
            body(question)
        } else {
            question
        }
    }
    if (!isFound) throw ElementNotFound("Выбранный вопрос не найден")
    return questionList
}
