package nsu.fit.qyoga.cases.core.completingQuestionnaires

import nsu.fit.qyoga.core.completingQuestionnaires.CompletingQuestionnairesConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration
import org.springframework.context.annotation.Import

@Import(CompletingQuestionnairesConfig::class)
@SpringBootApplication(exclude = [WebMvcAutoConfiguration::class])
class CompletingQuestionnairesTestConfig
