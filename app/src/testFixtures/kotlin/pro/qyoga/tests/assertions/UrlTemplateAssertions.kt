package pro.qyoga.tests.assertions

import io.kotest.assertions.withClue
import io.kotest.matchers.string.shouldMatch


infix fun String.shouldMatchUrlTemplate(template: String) {
    withClue(template) {
        this shouldMatch template.replace("\\{.*?}".toRegex(), ".*")
            .replace("?", "\\?")
    }
}