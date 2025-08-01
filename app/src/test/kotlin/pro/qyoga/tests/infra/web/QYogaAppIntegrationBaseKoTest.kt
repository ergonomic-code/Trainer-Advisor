package pro.qyoga.tests.infra.web

import pro.qyoga.tests.infra.rest_assured.configureRestAssured
import pro.qyoga.tests.infra.test_config.spring.context


abstract class QYogaAppIntegrationBaseKoTest(body: QYogaAppIntegrationBaseKoTest.() -> Unit = {}) :
    QYogaAppBaseKoTest() {

    init {
        beforeSpec {
            configureRestAssured(context)
        }
        body()
    }

}