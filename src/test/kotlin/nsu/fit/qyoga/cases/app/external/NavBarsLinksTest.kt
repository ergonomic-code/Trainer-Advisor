package nsu.fit.qyoga.cases.app.external

import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.When
import nsu.fit.qyoga.infra.QYogaAppTestBase
import nsu.fit.qyoga.platform.assertLinkValid
import org.jsoup.Jsoup
import org.junit.jupiter.api.Test


class NavBarsLinksTest : QYogaAppTestBase() {

    @Test
    fun `Links in left and top nav bars should lead to existing urls`() {
        val body = Given {
            authorized()
        } When {
            get("/")
        } Extract {
            Jsoup.parse(this.body().asString())
        }

        // left nav bar
        assertLinkValid(body, "Главная")
        assertLinkValid(body, "Клиенты")
        assertLinkValid(body, "Упражнения")
        assertLinkValid(body, "Опросы")

        // top nav bar
        assertLinkValid(body, "Выйти")
    }

}