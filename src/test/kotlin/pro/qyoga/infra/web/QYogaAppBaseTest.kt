package pro.qyoga.infra.web

import io.restassured.RestAssured
import io.restassured.builder.RequestSpecBuilder
import io.restassured.config.LogConfig
import io.restassured.config.RestAssuredConfig
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import pro.qyoga.app.QYogaApp
import pro.qyoga.fixture.Backgrounds
import pro.qyoga.fixture.BackgroundsConfig
import pro.qyoga.infra.db.DbInitializer
import pro.qyoga.infra.db.TestContainerDbContextInitializer
import pro.qyoga.infra.test_config.spring.auth.TestPasswordEncoderConfig
import pro.qyoga.infra.test_config.spring.db.TestDataSourceConfig
import javax.sql.DataSource


@ContextConfiguration(
    classes = [
        QYogaApp::class,
        BackgroundsConfig::class,
        TestPasswordEncoderConfig::class,
        TestDataSourceConfig::class,
    ],
    initializers = [TestContainerDbContextInitializer::class]
)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("test")
class QYogaAppBaseTest {

    @LocalServerPort
    var port: Int = 0

    @Autowired
    private lateinit var dataSource: DataSource

    @Autowired
    protected lateinit var backgrounds: Backgrounds

    @BeforeEach
    fun setupRestAssured() {
        val logConfig = LogConfig.logConfig()
        val config = RestAssuredConfig.config().logConfig(logConfig)

        RestAssured.requestSpecification = RequestSpecBuilder()
            .setBaseUri("http://localhost:$port")
            .setContentType("application/x-www-form-urlencoded; charset=UTF-8")
            .setRelaxedHTTPSValidation()
            .setConfig(config)
            .build()
    }

    @BeforeEach
    fun setupTestData() {
        DbInitializer(dataSource).executeScripts("/db/shared-fixture.sql")
    }

}