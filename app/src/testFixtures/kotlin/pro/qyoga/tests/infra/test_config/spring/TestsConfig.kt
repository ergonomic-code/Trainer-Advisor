package pro.qyoga.tests.infra.test_config.spring

import org.springframework.boot.autoconfigure.web.ServerProperties
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.ApplicationContext
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import pro.qyoga.app.QYogaApp
import pro.qyoga.infra.db.SdjConfig
import pro.qyoga.tests.fixture.FailingController
import pro.qyoga.tests.fixture.backgrounds.BackgroundsConfig
import pro.qyoga.tests.fixture.presets.Presets
import pro.qyoga.tests.fixture.test_apis.TestApisConf
import pro.qyoga.tests.fixture.wiremocks.MockServersConf
import pro.qyoga.tests.infra.test_config.spring.auth.TestPasswordEncoderConfig
import pro.qyoga.tests.infra.test_config.spring.db.TestDataSourceConfig
import pro.qyoga.tests.infra.test_config.spring.minio.TestMinioConfig


val context: ConfigurableApplicationContext by lazy {
    SpringApplicationBuilder(TestsConfig::class.java)
        .profiles("test")
        .build()
        .run()
}

val ApplicationContext.baseUrl: String
    get() {
        val port = this.getBean(ServerProperties::class.java).port
        return "http://localhost:$port"
    }

@Suppress("unused") // можно использовать для быстрых тестов кода, работающего только с БД
val sdjContext by lazy {
    AnnotationConfigApplicationContext(SdjTestsConfig::class.java)
}

@Import(
    QYogaApp::class,
    BackgroundsConfig::class,
    TestApisConf::class,
    Presets::class,
    TestPasswordEncoderConfig::class,
    TestDataSourceConfig::class,
    TestMinioConfig::class,
    FailingController::class,
    WireMockConf::class,
    MockServersConf::class,
)
@Configuration
class TestsConfig

@Import(SdjConfig::class, TestDataSourceConfig::class)
class SdjTestsConfig