package pro.qyoga.tests.infra.test_config.spring

import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import pro.qyoga.app.QYogaApp
import pro.qyoga.tests.infra.db.TestContainerDbContextInitializer
import pro.qyoga.tests.infra.test_config.spring.auth.TestPasswordEncoderConfig
import pro.qyoga.tests.infra.test_config.spring.db.TestDataSourceConfig


val context: ConfigurableApplicationContext = SpringApplicationBuilder(TestsConfig::class.java)
    .profiles("test")
    .initializers(TestContainerDbContextInitializer())
    .build()
    .run()

@Import(
    QYogaApp::class,
    pro.qyoga.tests.fixture.BackgroundsConfig::class,
    TestPasswordEncoderConfig::class,
    TestDataSourceConfig::class,
    FailingController::class
)
@Configuration
class TestsConfig