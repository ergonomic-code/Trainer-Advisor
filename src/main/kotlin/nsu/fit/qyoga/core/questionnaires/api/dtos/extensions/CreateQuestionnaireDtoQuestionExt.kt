package nsu.fit.qyoga.core.questionnaires.api.dtos.extensions

import nsu.fit.qyoga.core.questionnaires.api.dtos.CreateAnswerDto
import nsu.fit.qyoga.core.questionnaires.api.dtos.CreateQuestionDto
import nsu.fit.qyoga.core.questionnaires.api.dtos.CreateQuestionnaireDto

fun CreateQuestionnaireDto.addQuestionImage(
    questionId: Long,
    imageId: Long
): CreateQuestionnaireDto? {
    var isFound = false
    val questionList = this.question.map { question ->
        if (question.id == questionId) {
            isFound = true
            question.copy(imageId = imageId)
        } else {
            question
        }
    }
    if (!isFound) return null
    return this.copy(question = questionList.toMutableList())
}

fun CreateQuestionnaireDto.deleteQuestionImage(
    questionId: Long
): CreateQuestionnaireDto? {
    var isFound = false
    val questionList = this.question.map { question ->
        if (question.id == questionId) {
            isFound = true
            question.copy(imageId = null)
        } else {
            question
        }
    }
    if (!isFound) return null
    return this.copy(question = questionList.toMutableList())
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
    val questionList = this.question.map { value ->
        if (value.id == questionId) {
            changedQuestion.copy(answers = listOf(CreateAnswerDto()))
        } else {
            value
        }
    }
    return this.copy(question = questionList.toMutableList())
}

fun CreateQuestionnaireDto.updateQuestion(
    questionId: Long,
    changedQuestion: CreateQuestionDto
): CreateQuestionnaireDto? {
    var isFound = false
    val questionList = this.question.map {
        if (it.id == questionId) {
            isFound = true
            it.copy(
                title = changedQuestion.title,
                questionType = changedQuestion.questionType,
                answers = changedQuestion.answers
            )
        } else {
            it
        }
    }
    if (!isFound) return null
    return this.copy(question = questionList.toMutableList())
}
