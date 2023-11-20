package pro.qyoga.infra.web

import io.restassured.RestAssured
import io.restassured.builder.RequestSpecBuilder
import io.restassured.config.LogConfig
import io.restassured.config.RestAssuredConfig
import org.junit.jupiter.api.BeforeEach
import org.springframework.boot.autoconfigure.web.ServerProperties
import pro.qyoga.fixture.Backgrounds
import pro.qyoga.infra.db.DbInitializer
import pro.qyoga.infra.test_config.spring.context
import javax.sql.DataSource


open class QYogaAppBaseTest {


    private val dataSource: DataSource = context.getBean(DataSource::class.java)

    private val port: Int = context.getBean(ServerProperties::class.java).port

    protected val backgrounds: Backgrounds = context.getBean(Backgrounds::class.java)

    inline fun <reified T> getBean(): T =
        context.getBean(T::class.java)

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