package pro.qyoga.tests.assertions

import org.jsoup.Jsoup
import org.springframework.test.web.servlet.client.RestTestClient
import pro.qyoga.tests.platform.html.HtmlPage
import pro.qyoga.tests.platform.spring.rest_test_client.getBodyAsString

infix fun RestTestClient.ResponseSpec.shouldBePage(page: HtmlPage): RestTestClient.ResponseSpec {
    Jsoup.parse(this.getBodyAsString()) shouldBePage page
    return this
}
