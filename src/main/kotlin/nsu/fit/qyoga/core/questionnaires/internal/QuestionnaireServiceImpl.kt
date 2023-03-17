package nsu.fit.qyoga.core.questionnaires.internal

import nsu.fit.qyoga.core.questionnaires.api.QuestionnaireService
import nsu.fit.qyoga.core.questionnaires.api.dtos.QuestionnaireDto
import nsu.fit.qyoga.core.questionnaires.api.dtos.QuestionnaireSearchDto
import org.springframework.data.domain.*
import org.springframework.stereotype.Service

@Service
class QuestionnaireServiceImpl(
    private val questionnaireRepo: QuestionnaireRepo
) : QuestionnaireService {

    override fun findQuestionnaires(
        questionnaireSearchDto: QuestionnaireSearchDto,
        pageable: Pageable
    ): Page<QuestionnaireDto> {
        val page = questionnaireRepo.findPageByTitle(
            title = questionnaireSearchDto.title ?: "",
            pageable = pageable
        )
        return page.map { QuestionnaireDto(id = it.id, title = it.title) }
    }
}
