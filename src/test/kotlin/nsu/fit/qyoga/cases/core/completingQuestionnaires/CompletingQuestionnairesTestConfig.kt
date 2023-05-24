package nsu.fit.qyoga.cases.core.completingQuestionnaires

import nsu.fit.qyoga.core.images.ImagesConfig
import nsu.fit.qyoga.core.questionnaires.QuestionnairesConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration
import org.springframework.context.annotation.Import

@SpringBootApplication(exclude = [WebMvcAutoConfiguration::class])
class CompletingQuestionnairesTestConfig
