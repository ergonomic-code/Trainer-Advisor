package nsu.fit.qyoga.core.questionnaires.api.dtos.extensions

import nsu.fit.qyoga.core.questionnaires.api.dtos.CreateAnswerDto
import nsu.fit.qyoga.core.questionnaires.api.dtos.CreateQuestionDto
import nsu.fit.qyoga.core.questionnaires.api.dtos.CreateQuestionnaireDto

fun CreateQuestionnaireDto.addAnswerImage(
    questionId: Long,
    answerId: Long,
    imageId: Long
): CreateQuestionnaireDto? {
    var isFound = false
    val questionList = this.question.map { question ->
        if (question.id == questionId) {
            question.copy(
                answers = question.answers.map { answer ->
                    if (answer.id == answerId) {
                        isFound = true
                        answer.copy(imageId = imageId)
                    } else {
                        answer
                    }
                }
            )
        } else {
            question
        }
    }
    if (!isFound) return null
    return this.copy(question = questionList.toMutableList())
}

fun CreateQuestionnaireDto.deleteAnswersImage(
    questionId: Long,
    answerId: Long
): CreateQuestionnaireDto? {
    var isFound = false
    val questionList = this.question.map { question ->
        if (question.id == questionId) {
            question.copy(
                answers = question.answers.map { answer ->
                    isFound = true
                    if (answer.id == answerId) {
                        answer.copy(imageId = null)
                    } else {
                        answer
                    }
                }
            )
        } else {
            question
        }
    }
    if (!isFound) return null
    return this.copy(question = questionList.toMutableList())
}

fun CreateQuestionnaireDto.updateAnswer(
    questionId: Long,
    answerId: Long,
    changedAnswer: CreateAnswerDto
): CreateQuestionnaireDto? {
    var isFound = false
    val questionList = this.question.map { value ->
        if (value.id == questionId) {
            val copiedQuestion = value.copy(
                answers = value.answers.map {
                    if (it.id == answerId) {
                        isFound = true
                        it.copy(
                            title = changedAnswer.title,
                            bounds = changedAnswer.bounds,
                            score = changedAnswer.score
                        )
                    } else {
                        it
                    }
                }
            )
            copiedQuestion
        } else {
            value
        }
    }
    if (!isFound) return null
    return this.copy(question = questionList.toMutableList())
}

fun CreateQuestionnaireDto.updateQuestionAnswers(
    changedQuestion: CreateQuestionDto,
    questionId: Long
): CreateQuestionnaireDto? {
    var isFound = false
    val questionList = this.question.map { value ->
        if (value.id == questionId) {
            isFound = true
            value.copy(answers = changedQuestion.answers)
        } else {
            value
        }
    }
    if (!isFound) return null
    return this.copy(question = questionList.toMutableList())
}

fun CreateQuestionnaireDto.addAnswer(
    changedQuestion: CreateQuestionDto,
    questionId: Long
): CreateQuestionnaireDto {
    val lastId = if (changedQuestion.answers.isEmpty()) 0 else changedQuestion.answers.last().id + 1
    val questionList = this.question.map { value ->
        if (value.id == questionId) {
            val copiedQuestion = changedQuestion.copy(
                answers = changedQuestion.answers + CreateAnswerDto(id = lastId)
            )
            copiedQuestion
        } else {
            value
        }
    }
    return this.copy(question = questionList.toMutableList())
}

fun CreateQuestionnaireDto.deleteAnswer(
    changedQuestion: CreateQuestionDto,
    questionId: Long,
    answerId: Long
): CreateQuestionnaireDto {
    val questionList = this.question.map { value ->
        if (value.id == questionId) {
            val copiedQuestion = changedQuestion.copy(
                answers = changedQuestion.answers.filter { it.id != answerId }
            )
            copiedQuestion
        } else {
            value
        }
    }
    return this.copy(question = questionList.toMutableList())
}