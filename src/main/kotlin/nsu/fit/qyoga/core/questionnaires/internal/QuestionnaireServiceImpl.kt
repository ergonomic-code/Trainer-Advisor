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
) : QuestionnaireService{

    override fun findQuestionnaires(title: String?, page: Page) : List<QuestionnaireDto>{
        val pageable: PageRequest = if(page.orderType == OrderType.DESK){
            PageRequest.of(page.pageNum-1, page.pageSize, Sort.by("title").descending())
        }else{
            PageRequest.of(page.pageNum-1, page.pageSize, Sort.by("title"))
        }
        title?.let {
            println("Hi")
            return questionnaireRepo.findAllByTitleContaining(title, pageable)
        }?:let {
            return questionnaireRepo.findAll(pageable).map{QuestionnaireDto(id=it.id, title=it.title)}.toList()
        }

    }
}