package pro.qyoga.tests.clients.pages.publc

import org.jsoup.nodes.Element
import pro.qyoga.tests.assertions.shouldHave
import pro.qyoga.tests.assertions.shouldHaveTitle
import pro.qyoga.tests.infra.html.FormAction
import pro.qyoga.tests.infra.html.Input.Companion.email
import pro.qyoga.tests.infra.html.Input.Companion.submit
import pro.qyoga.tests.infra.html.Input.Companion.text
import pro.qyoga.tests.infra.html.QYogaForm
import pro.qyoga.tests.infra.html.QYogaPage

object RegisterPage : QYogaPage {

    override val path = "/register"

    override val title = "Заявка на регистрацию"

    object RegisterForm : QYogaForm("registerForm", action = FormAction.hxPost(path)) {

        val firstName = text("firstName")

        val lastName = text("lastName")

        val email = email("email")
        val duplicatedEmail = "${email.selector()}.is-invalid"

        const val duplicatedEmailMessage = "#duplicatedEmailMessage"

        override val components = listOf(
            firstName,
            lastName,
            email,
            submit("register", "Отправить")
        )

    }

    override fun match(element: Element) {
        element.shouldHaveTitle(title)
        element shouldHave RegisterForm
    }

}