package nsu.fit.qyoga.cases.core.exercises.ui

import io.github.ulfs.assertj.jsoup.Assertions
import io.github.ulfs.assertj.jsoup.DocumentAssertionsSpec
import io.restassured.response.ValidatableResponse
import org.jsoup.Jsoup

fun ValidatableResponse.assertThatBody(body: DocumentAssertionsSpec.() -> DocumentAssertionsSpec) {
    val document = Jsoup.parse(extract().body().asString())
    Assertions.assertThatSpec(document, assert = body)
}