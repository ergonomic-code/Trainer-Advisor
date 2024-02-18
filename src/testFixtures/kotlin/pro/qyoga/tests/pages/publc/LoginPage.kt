package pro.qyoga.tests.pages.publc

import io.kotest.assertions.withClue
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.should
import org.jsoup.nodes.Element
import pro.qyoga.tests.assertions.shouldBeComponent
import pro.qyoga.tests.assertions.shouldHaveComponent
import pro.qyoga.tests.assertions.shouldHaveElement
import pro.qyoga.tests.assertions.shouldHaveTitle
import pro.qyoga.tests.platform.html.FormAction
import pro.qyoga.tests.platform.html.Input.Companion.email
import pro.qyoga.tests.platform.html.Input.Companion.password
import pro.qyoga.tests.platform.html.Input.Companion.submit
import pro.qyoga.tests.platform.html.Link
import pro.qyoga.tests.platform.html.QYogaForm
import pro.qyoga.tests.platform.html.QYogaPage


object LoginPage : QYogaPage {

    override val path = "/login"

    object LoginForm : QYogaForm("loginForm", FormAction.classicPost(path)) {

        val username by component { email("username", true) }
        val invalidUserName = "${username.selector()}.is-invalid"

        val password by component { password("password", true) }

        val submit by component { submit("loginButton", "Войти") }

    }

    override val title = "Вход в систему"

    private const val LOGIN_ERROR_MESSAGE = "div.invalid-feedback:contains(Неверный логин)"

    private val registerLink = Link("registerLink", RegisterPage, "Оставить заявку на регистрацию")

    override fun match(element: Element) {
        element.shouldHaveTitle(title)

        withClue("Cannot find login form by ${LoginForm.selector()}") {
            element.select(LoginForm.selector()) shouldHaveSize 1
        }
        withClue("Login form has invalid structure") {
            element.select(LoginForm.selector())[0] shouldBeComponent LoginForm
            element.select(LoginForm.selector())[0] should LoginForm.action.matcher()
        }

        element shouldHaveElement LOGIN_ERROR_MESSAGE

        element shouldHaveComponent registerLink
    }

}


