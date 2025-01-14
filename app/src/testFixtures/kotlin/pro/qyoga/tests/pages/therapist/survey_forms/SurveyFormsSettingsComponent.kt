package pro.qyoga.tests.pages.therapist.survey_forms

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.matchers.Matcher
import org.jsoup.nodes.Element
import pro.qyoga.app.therapist.survey_forms.settings.SurveyFormsSettingsComponentController
import pro.qyoga.tests.assertions.haveComponent
import pro.qyoga.tests.assertions.isTag
import pro.qyoga.tests.infra.test_config.spring.context
import pro.qyoga.tests.platform.html.*
import pro.qyoga.tests.platform.kotest.all


object SurveyFormsSettingsComponent : Component {

    object SurveyFormsSettingsForm :
        QYogaForm("surveyFormsSettingsForm", FormAction.hxPut(SurveyFormsSettingsComponentController.PATH)) {

        private val mapper = context.getBean(ObjectMapper::class.java)

        val yandexAdminEmail by component {
            Input.email(
                "yandexAdminEmail",
                false
            )
        }

        @Suppress("unused")
        val saveButton by component {
            Button("saveYandexAdminEmailButton", "")
        }

        fun yandexAdminEmailValue(element: Element): String? {
            val formEl = element.select("#$id").single()
            val formModelJson = formEl.attr("x-data").replace("'", "\"")
            val formModel = mapper.readValue(formModelJson, JsonNode::class.java)
            return formModel.get(yandexAdminEmail.name).asText()
        }

    }

    override fun selector(): String = "#surveyFormsSettings"

    override fun matcher(): Matcher<Element> = Matcher.all(
        isTag("div"),
        haveComponent(SurveyFormsSettingsForm)
    )

}
