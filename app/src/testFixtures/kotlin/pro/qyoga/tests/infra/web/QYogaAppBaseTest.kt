package pro.qyoga.tests.infra.web

import org.junit.jupiter.api.BeforeEach
import org.springframework.boot.autoconfigure.web.ServerProperties
import pro.qyoga.tests.fixture.backgrounds.Backgrounds
import pro.qyoga.tests.fixture.data.resetFaker
import pro.qyoga.tests.fixture.presets.Presets
import pro.qyoga.tests.infra.db.setupDb
import pro.qyoga.tests.infra.test_config.spring.context
import pro.qyoga.tests.infra.wiremock.WireMock
import javax.sql.DataSource


open class QYogaAppBaseTest {

    private val dataSource: DataSource = context.getBean(DataSource::class.java)

    protected val port: Int = context.getBean(ServerProperties::class.java).port

    protected val backgrounds: Backgrounds = context.getBean(Backgrounds::class.java)

    protected val presets: Presets = context.getBean(Presets::class.java)

    inline fun <reified T> getBean(): T =
        context.getBean(T::class.java)

    @BeforeEach
    fun setupTestData() {
        dataSource.setupDb()
        resetFaker()
        WireMock.reset()
    }

}