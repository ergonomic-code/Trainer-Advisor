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
        val sort = if (questionnaireSearchDto.orderType == "DESC") {
            Sort.by("title").descending()
        } else {
            Sort.by("title").ascending()
        }
        return questionnaireRepo.findPageByTitle(
            questionnaireSearchDto.title?: "",
            PageRequest.of(pageable.pageNumber, pageable.pageSize, sort)
        )
    }

    override fun getQuestionnairesCount(title: String?): Long {
        return if (title != null) {
            questionnaireRepo.countAllByTitleContaining(title)
        } else {
            questionnaireRepo.count()
        }
    }
}
