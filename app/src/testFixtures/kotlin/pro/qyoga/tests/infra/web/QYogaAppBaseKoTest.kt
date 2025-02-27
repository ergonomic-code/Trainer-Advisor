package pro.qyoga.tests.infra.web

import io.kotest.core.spec.style.FreeSpec
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.web.ServerProperties
import pro.qyoga.tests.fixture.backgrounds.Backgrounds
import pro.qyoga.tests.fixture.data.resetFaker
import pro.qyoga.tests.fixture.presets.Presets
import pro.qyoga.tests.infra.db.setupDb
import pro.qyoga.tests.infra.test_config.spring.context
import javax.sql.DataSource


abstract class QYogaAppBaseKoTest(body: FreeSpec.() -> Unit = {}) : FreeSpec() {

    private val log = LoggerFactory.getLogger(javaClass)

    private val dataSource: DataSource = context.getBean(DataSource::class.java)

    protected val port: Int = context.getBean(ServerProperties::class.java).port

    val backgrounds: Backgrounds = context.getBean(Backgrounds::class.java)

    val presets: Presets = context.getBean(Presets::class.java)

    inline fun <reified T> getBean(): T =
        context.getBean(T::class.java)

    inline fun <reified T> getBean(name: String): T =
        context.getBean(name, T::class.java)

    init {
        beforeAny {
            if (it.descriptor.isRootTest()) {
                dataSource.setupDb()
            }
            resetFaker()
        }
        body()
    }

}