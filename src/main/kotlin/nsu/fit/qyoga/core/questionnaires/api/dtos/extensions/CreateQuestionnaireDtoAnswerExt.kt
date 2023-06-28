package nsu.fit.qyoga.core.questionnaires.api.dtos.extensions

import nsu.fit.qyoga.core.questionnaires.api.dtos.CreateAnswerDto
import nsu.fit.qyoga.core.questionnaires.api.dtos.CreateQuestionDto
import nsu.fit.qyoga.core.questionnaires.api.dtos.CreateQuestionnaireDto
import nsu.fit.qyoga.core.questionnaires.api.errors.ElementNotFound

fun CreateQuestionnaireDto.addAnswerImage(
    questionId: Long,
    answerId: Long,
    imageId: Long
): CreateQuestionnaireDto {
    return this.copy(
        question = modifyAnswer(answerId, questionId) {
            it.copy(
                imageId = imageId
            )
        }.toMutableList()
    )
}

fun CreateQuestionnaireDto.deleteAnswersImage(
    questionId: Long,
    answerId: Long
): CreateQuestionnaireDto {
    return this.copy(
        question = modifyAnswer(answerId, questionId) {
            it.copy(
                imageId = null
            )
        }.toMutableList()
    )
}

fun CreateQuestionnaireDto.updateAnswer(
    questionId: Long,
    answerId: Long,
    changedAnswer: CreateAnswerDto
): CreateQuestionnaireDto {
    return this.copy(
        question = modifyAnswer(answerId, questionId) {
            it.copy(
                title = changedAnswer.title,
                bounds = changedAnswer.bounds,
                score = changedAnswer.score
            )
        }.toMutableList()
    )
}

fun CreateQuestionnaireDto.updateQuestionAnswers(
    changedQuestion: CreateQuestionDto,
    questionId: Long
): CreateQuestionnaireDto {
    return this.copy(
        question = modifyQuestion(questionId) {
            it.copy(answers = changedQuestion.answers)
        }.toMutableList()
    )
}

fun CreateQuestionnaireDto.addAnswer(
    changedQuestion: CreateQuestionDto,
    questionId: Long
): CreateQuestionnaireDto {
    val lastId = if (changedQuestion.answers.isEmpty()) 0 else changedQuestion.answers.last().id + 1
    return this.copy(
        question = modifyQuestion(questionId) {
            it.copy(answers = changedQuestion.answers + CreateAnswerDto(id = lastId))
        }.toMutableList()
    )
}

fun CreateQuestionnaireDto.deleteAnswer(
    changedQuestion: CreateQuestionDto,
    questionId: Long,
    answerId: Long
): CreateQuestionnaireDto {
    return this.copy(
        question = modifyQuestion(questionId) {
            it.copy(answers = changedQuestion.answers.filter { it.id != answerId })
        }.toMutableList()
    )
}

fun CreateQuestionnaireDto.modifyAnswer(
    questionId: Long,
    answerId: Long,
    body: (CreateAnswerDto) -> CreateAnswerDto
): List<CreateQuestionDto> {
    var isFound = false
    val questionList = this.question.map { question ->
        if (question.id == questionId) {
            question.copy(
                answers = question.answers.map { answer ->
                    if (answer.id == answerId) {
                        isFound = true
                        body(answer)
                    } else {
                        answer
                    }
                }
            )
        } else {
            question
        }
    }
    if (!isFound) throw ElementNotFound("Выбранный ответ не найден")
    return questionList
}
