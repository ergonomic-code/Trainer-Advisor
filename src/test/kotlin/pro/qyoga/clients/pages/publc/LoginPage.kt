package pro.qyoga.clients.pages.publc

import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.jsoup.nodes.Element
import pro.qyoga.assertions.shouldBeElement
import pro.qyoga.infra.html.FormAction
import pro.qyoga.infra.html.Input.Companion.email
import pro.qyoga.infra.html.Input.Companion.password
import pro.qyoga.infra.html.QYogaForm
import pro.qyoga.infra.html.QYogaPage


object LoginPage : QYogaPage {

    override val path = "/login"

    object LoginForm : QYogaForm("loginForm", FormAction.classic(path)) {

        val username = email("username")
        val invalidUserName = "${username.selector()}.is-invalid"

        val password = password("password")

        override val components = listOf(username, password)

    }

    override val title = "Вход в систему"


    private const val LOGIN_ERROR_MESSAGE = "div.invalid-feedback:contains(Неверный логин)"

    override fun match(element: Element) {
        element.select("title").text() shouldBe title

        element.select(LoginForm.selector())[0] shouldBeElement LoginForm

        element.select(LOGIN_ERROR_MESSAGE) shouldHaveSize 1
    }

}


