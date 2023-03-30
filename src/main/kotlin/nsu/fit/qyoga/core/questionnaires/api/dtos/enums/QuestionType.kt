package nsu.fit.qyoga.core.questionnaires.api.dtos.enums

enum class QuestionType(var text: String) {
    SINGLE("one"),
    SEVERAL("several"),
    RANGE("range"),
    TEXT("text")
}