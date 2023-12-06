package pro.qyoga.tests.assertions

import io.kotest.assertions.withClue
import io.kotest.matchers.collections.shouldHaveSize
import io.restassured.http.Cookie
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.jsoup.nodes.Element


fun Element.shouldHaveValidLink(linkText: String, authCookie: Cookie) {
    val linkElements = this.select("a:contains($linkText)")
    withClue("Cannot find link '$linkText'") {
        linkElements shouldHaveSize 1
    }
    val link = linkElements.single()
    val path = link.attr("href")
    Given {
        cookie(authCookie)
    } When {
        get(path)
    } Then {
        statusCode(200)
    }
}