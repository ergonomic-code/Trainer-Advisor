package nsu.fit.qyoga.cases.core.clients

import nsu.fit.qyoga.core.clients.ClientConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration
import org.springframework.context.annotation.Import

@Import(ClientConfig::class)
@SpringBootApplication(exclude = [WebMvcAutoConfiguration::class])
class ClientsTestConfig
