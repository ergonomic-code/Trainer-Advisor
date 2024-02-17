package pro.qyoga.tests.clients.pages.publc

import org.jsoup.nodes.Element
import pro.qyoga.tests.assertions.shouldHaveComponent
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

        val firstName = text("firstName", true)

        val lastName = text("lastName", true)

        val email = email("email", true)
        val duplicatedEmail = "${email.selector()}.is-invalid"

        const val DUPLICATED_EMAIL_MESSAGE = "#duplicatedEmailMessage"

        override val components = listOf(
            firstName,
            lastName,
            email,
            submit("register", "Отправить")
        )

    }

    override fun match(element: Element) {
        element.shouldHaveTitle(title)
        element shouldHaveComponent RegisterForm
    }

}