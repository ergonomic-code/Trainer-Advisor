package pro.qyoga.tests.infra.web

import org.junit.jupiter.api.BeforeEach
import pro.qyoga.tests.clients.TherapistClient
import pro.qyoga.tests.infra.rest_assured.configureRestAssured
import pro.qyoga.tests.infra.test_config.spring.context


open class QYogaAppIntegrationBaseTest : QYogaAppBaseTest() {

    protected val theTherapist by lazy { TherapistClient.loginAsTheTherapist() }

    @BeforeEach
    fun setupRestAssured() {
        configureRestAssured(context)
    }

}