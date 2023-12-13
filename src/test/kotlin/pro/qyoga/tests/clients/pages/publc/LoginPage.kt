package pro.qyoga.tests.clients.pages.publc

import io.kotest.assertions.withClue
import io.kotest.matchers.collections.shouldHaveSize
import org.jsoup.nodes.Element
import pro.qyoga.tests.assertions.shouldBeElement
import pro.qyoga.tests.assertions.shouldHave
import pro.qyoga.tests.assertions.shouldHaveTitle
import pro.qyoga.tests.infra.html.FormAction
import pro.qyoga.tests.infra.html.Input.Companion.email
import pro.qyoga.tests.infra.html.Input.Companion.password
import pro.qyoga.tests.infra.html.Link
import pro.qyoga.tests.infra.html.QYogaForm
import pro.qyoga.tests.infra.html.QYogaPage


object LoginPage : QYogaPage {

    override val path = "/login"

    object LoginForm : QYogaForm("loginForm", FormAction.classicPost(path)) {

        val username = email("username", true)
        val invalidUserName = "${username.selector()}.is-invalid"

        val password = password("password", true)

        override val components = listOf(username, password)

    }

    override val title = "Вход в систему"

    private const val LOGIN_ERROR_MESSAGE = "div.invalid-feedback:contains(Неверный логин)"

    private val registerLink = Link(RegisterPage, "Оставить заявку на регистрацию")

    override fun match(element: Element) {
        element.shouldHaveTitle(title)

        withClue("Cannot find login form by ${LoginForm.selector()}") {
            element.select(LoginForm.selector()) shouldHaveSize 1
        }
        withClue("Login form has invalid structure") {
            element.select(LoginForm.selector())[0] shouldBeElement LoginForm
            element.select(LoginForm.selector())[0] shouldBeElement LoginForm.action
        }

        element shouldHave LOGIN_ERROR_MESSAGE

        element shouldHave registerLink
    }

}


