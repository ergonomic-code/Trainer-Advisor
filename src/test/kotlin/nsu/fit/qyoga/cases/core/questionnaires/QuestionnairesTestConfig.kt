package nsu.fit.qyoga.cases.core.questionnaires

import nsu.fit.qyoga.core.questionnaires.QuestionnairesConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration
import org.springframework.context.annotation.Import

@Import(QuestionnairesConfig::class)
@SpringBootApplication(exclude = [WebMvcAutoConfiguration::class])
class QuestionnairesTestConfig
