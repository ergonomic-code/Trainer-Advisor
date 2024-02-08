package pro.qyoga.tests.cases.app.therapist.appointments.core

import io.restassured.response.ResponseBodyData
import org.jsoup.Jsoup
import pro.qyoga.tests.assertions.shouldBePage
import pro.qyoga.tests.infra.html.HtmlPage

infix fun ResponseBodyData.shouldBePage(page: HtmlPage): ResponseBodyData {
    Jsoup.parse(this.asString()) shouldBePage page
    return this
}
