package nsu.fit.qyoga.cases.core.images

import nsu.fit.qyoga.core.images.ImagesConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration
import org.springframework.context.annotation.Import

@Import(ImagesConfig::class)
@SpringBootApplication(exclude = [WebMvcAutoConfiguration::class])
class ImagesTestConfig
