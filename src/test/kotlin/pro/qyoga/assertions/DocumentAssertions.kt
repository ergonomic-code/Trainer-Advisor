package pro.qyoga.assertions

import io.restassured.http.Cookie
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.jsoup.nodes.Document


fun Document.shouldHaveValidLink(linkText: String, authCookie: Cookie) {
    val newExerciseLink = this.select("a:contains($linkText)").toList().single()
    val path = newExerciseLink.attr("href")
    Given {
        cookie(authCookie)
    } When {
        get(path)
    } Then {
        statusCode(200)
    }
}