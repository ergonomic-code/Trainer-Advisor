package nsu.fit.qyoga.infra

import nsu.fit.qyoga.app.QYogaApplication
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration


@Retention(AnnotationRetention.RUNTIME)
@ContextConfiguration(
    classes = [QYogaApplication::class],
    initializers = [TestContainerDbContextInitializer::class]
)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
annotation class QYogaAppTest