package nsu.fit.qyoga.core.questionnaires.api.dtos

import nsu.fit.qyoga.core.questionnaires.utils.Page

data class QuestionnaireSearchDto (
    val title: String?,
    val page: Page
)
