package nsu.fit.qyoga.core.questionnaires.api.dtos.enums

enum class QuestionType(var text: String) {
    SINGLE("Один из списка"),
    SEVERAL("Несколько из списка"),
    RANGE("Интервал"),
    TEXT("Текст")
}
