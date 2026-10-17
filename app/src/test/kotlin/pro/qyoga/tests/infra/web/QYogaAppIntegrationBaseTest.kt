package pro.qyoga.tests.infra.web

import pro.qyoga.tests.clients.TherapistClient


open class QYogaAppIntegrationBaseTest : QYogaAppBaseTest() {

    protected val theTherapist by lazy { TherapistClient.loginAsTheTherapist() }

}
