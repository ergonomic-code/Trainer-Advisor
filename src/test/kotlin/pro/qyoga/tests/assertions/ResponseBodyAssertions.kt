package pro.qyoga.tests.assertions

import io.restassured.response.ResponseBodyData
import org.jsoup.Jsoup
import pro.qyoga.tests.platform.html.HtmlPage

infix fun ResponseBodyData.shouldBePage(page: HtmlPage): ResponseBodyData {
    Jsoup.parse(this.asString()) shouldBePage page
    return this
}
