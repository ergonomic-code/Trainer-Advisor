package nsu.fit.qyoga.core.completingQuestionnaires.internal.serviceImpl

import nsu.fit.qyoga.core.completingQuestionnaires.api.dtos.CompletingListDto
import nsu.fit.qyoga.core.completingQuestionnaires.api.errors.CompletingException
import nsu.fit.qyoga.core.completingQuestionnaires.api.services.CompletingService
import nsu.fit.qyoga.core.completingQuestionnaires.internal.repository.CompletingJdbcTemplateRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CompletingServiceImpl(
    @Autowired val completingJdbcTemplateRepo: CompletingJdbcTemplateRepo
) : CompletingService {

    override fun findCompletingByQId(id: Long): CompletingListDto {
        return completingJdbcTemplateRepo.findQuestionnaireCompletingById(id)
            ?: throw CompletingException("Не удалось загрузить прохождения опросника: Выбранный опросник не найден")
    }
}