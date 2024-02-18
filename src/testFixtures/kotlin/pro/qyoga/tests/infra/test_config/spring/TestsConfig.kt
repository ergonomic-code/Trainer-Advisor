package pro.qyoga.tests.infra.test_config.spring

import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import pro.qyoga.app.QYogaApp
import pro.qyoga.infra.db.SdjConfig
import pro.qyoga.tests.fixture.backgrounds.BackgroundsConfig
import pro.qyoga.tests.infra.test_config.spring.auth.TestPasswordEncoderConfig
import pro.qyoga.tests.infra.test_config.spring.db.TestDataSourceConfig
import pro.qyoga.tests.infra.test_config.spring.minio.TestMinioConfig
import pro.qyoga.tests.fixture.FailingController


val context: ConfigurableApplicationContext by lazy {
    SpringApplicationBuilder(TestsConfig::class.java)
        .profiles("test")
        .build()
        .run()
}

@Suppress("unused") // можно использовать для быстрых тестов кода, работающего только с БД
val sdjContext by lazy {
    AnnotationConfigApplicationContext(SdjTestsConfig::class.java)
}

@Import(
    QYogaApp::class,
    BackgroundsConfig::class,
    TestPasswordEncoderConfig::class,
    TestDataSourceConfig::class,
    TestMinioConfig::class,
    FailingController::class
)
@Configuration
class TestsConfig

@Import(SdjConfig::class, TestDataSourceConfig::class)
class SdjTestsConfig