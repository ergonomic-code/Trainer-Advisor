package nsu.fit.qyoga.core.questionnaires.internal

import nsu.fit.qyoga.core.questionnaires.api.QuestionnaireService
import nsu.fit.qyoga.core.questionnaires.api.dtos.QuestionnaireDto
import nsu.fit.qyoga.core.questionnaires.utils.OrderType
import nsu.fit.qyoga.core.questionnaires.utils.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class QuestionnaireServiceImpl(
    private val questionnaireRepo: QuestionnaireRepo
) : QuestionnaireService {

    override fun findQuestionnaires(title: String?, page: Page): List<QuestionnaireDto> {
        val pageable: PageRequest = if (page.orderType == OrderType.DESK) {
            PageRequest.of(page.pageNum - 1, page.pageSize, Sort.by("title").descending())
        } else {
            PageRequest.of(page.pageNum - 1, page.pageSize, Sort.by("title"))
        }
        return title?.let {
            questionnaireRepo.findAllByTitleContaining(title, pageable).map { QuestionnaireDto(id = it.id, title = it.title) }.toList()
        } ?: let {
            questionnaireRepo.findAll(pageable).map { QuestionnaireDto(id = it.id, title = it.title) }.toList()
        }

    }

    override fun getQuestionnairesCount(title: String?): Long {
        return title?.let {
            questionnaireRepo.countAllByTitleContaining(title)
        } ?: let {
            questionnaireRepo.count()
        }

    }
}
