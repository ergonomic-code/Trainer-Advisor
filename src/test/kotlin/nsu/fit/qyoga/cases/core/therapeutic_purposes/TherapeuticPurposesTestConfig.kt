package nsu.fit.qyoga.cases.core.therapeutic_purposes

import nsu.fit.qyoga.core.therapeutic_purposes.TherapeuticPurposesConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration
import org.springframework.context.annotation.Import

@Import(TherapeuticPurposesConfig::class)
@SpringBootApplication(exclude = [WebMvcAutoConfiguration::class])
class TherapeuticPurposesTestConfig
