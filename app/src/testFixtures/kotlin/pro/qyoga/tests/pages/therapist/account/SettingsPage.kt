package pro.qyoga.tests.pages.therapist.account

import io.kotest.matchers.Matcher
import org.jsoup.nodes.Element
import pro.qyoga.app.therapist.account.SettingsPageController
import pro.qyoga.app.therapist.survey_forms.settings.SurveyFormsSettingsComponentController
import pro.qyoga.tests.assertions.*
import pro.qyoga.tests.platform.html.Component
import pro.qyoga.tests.platform.kotest.all


object SettingsPage : PageMatcher {

    override fun match(element: Element) {
        element shouldHaveTitle "Настройки"
        element shouldHaveComponent SurveysSettingsPlaceholder
    }

    const val PATH = SettingsPageController.PATH

}

object SurveysSettingsPlaceholder : Component {

    override fun selector(): String = "#surveyFormsSettings"

    override fun matcher(): Matcher<Element> = Matcher.all(
        isTag("div"),
        haveAttribute("hx-get"),
        haveAttributeValue("hx-get", SurveyFormsSettingsComponentController.PATH),
        haveAttribute("hx-trigger"),
        haveAttributeValue("hx-trigger", "load")
    )

}