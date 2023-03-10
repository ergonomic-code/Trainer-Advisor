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
        val sort = if (questionnaireSearchDto.orderType == "DESK") {
            Sort.by("title").descending()
        } else {
            Sort.by("title").ascending()
        }
        val pageRequest = PageRequest.of(pageable.pageNumber, pageable.pageSize, sort)
        val title = questionnaireSearchDto.title
        val entities = if (title == null) {
            questionnaireRepo.findAll(pageRequest)
        } else {
            questionnaireRepo.findAllByTitleContaining(title, pageRequest)
        }
        val count = getQuestionnairesCount(questionnaireSearchDto.title)
        return PageImpl(entities.map { QuestionnaireDto(id = it.id, title = it.title) }.toList(), pageRequest, count)
    }

    override fun getQuestionnairesCount(title: String?): Long {
        return  if (title != null) {
            questionnaireRepo.countAllByTitleContaining(title)
        } else {
            questionnaireRepo.count()
        }
    }
}
