package pro.qyoga.tests.pages.publc

import org.jsoup.nodes.Element
import pro.qyoga.tests.assertions.shouldHaveComponent
import pro.qyoga.tests.assertions.shouldHaveTitle
import pro.qyoga.tests.platform.html.FormAction
import pro.qyoga.tests.platform.html.Input.Companion.email
import pro.qyoga.tests.platform.html.Input.Companion.submit
import pro.qyoga.tests.platform.html.Input.Companion.text
import pro.qyoga.tests.platform.html.QYogaForm
import pro.qyoga.tests.platform.html.QYogaPage

object RegisterPage : QYogaPage {

    override val path = "/register"

    override val title = "Заявка на регистрацию"

    object RegisterForm : QYogaForm("registerForm", action = FormAction.hxPost(path)) {

        val firstName by component { text("firstName", true) }

        val lastName by component { text("lastName", true) }

        val email by component { email("email", true) }
        val duplicatedEmail = "${email.selector()}.is-invalid"

        const val DUPLICATED_EMAIL_MESSAGE = "#duplicatedEmailMessage"

        val submit by component { submit("register", "Отправить") }

    }

    override fun match(element: Element) {
        element.shouldHaveTitle(title)
        element shouldHaveComponent RegisterForm
    }

}