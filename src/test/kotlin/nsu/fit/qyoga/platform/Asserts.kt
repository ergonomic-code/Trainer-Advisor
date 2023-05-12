package nsu.fit.qyoga.platform

import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.jsoup.nodes.Document


fun assertLinkValid(body: Document, linkText: String) {
    val newExerciseLink = body.getElementsByTag("a").toList()
        .first { it.text() == linkText }
    val path = newExerciseLink.attr("href")
    Given {
        this
    } When {
        get(path)
    } Then {
        statusCode(200)
    }
}
