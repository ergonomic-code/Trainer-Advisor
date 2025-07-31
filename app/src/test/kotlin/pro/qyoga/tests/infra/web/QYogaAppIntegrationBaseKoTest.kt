package pro.qyoga.tests.infra.web


abstract class QYogaAppIntegrationBaseKoTest(body: QYogaAppIntegrationBaseKoTest.() -> Unit = {}) :
    QYogaAppBaseKoTest() {

    init {
        body()
    }

}