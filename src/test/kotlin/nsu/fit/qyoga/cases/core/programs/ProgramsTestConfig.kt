package nsu.fit.qyoga.cases.core.programs

import nsu.fit.qyoga.core.programs.ProgramsConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration
import org.springframework.context.annotation.Import

@Import(ProgramsConfig::class)
@SpringBootApplication(exclude = [WebMvcAutoConfiguration::class])
class ProgramsTestConfig
