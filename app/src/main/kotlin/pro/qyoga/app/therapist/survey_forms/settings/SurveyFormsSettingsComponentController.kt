package pro.qyoga.app.therapist.survey_forms.settings

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.servlet.ModelAndView
import pro.azhidkov.platform.spring.mvc.modelAndView
import pro.qyoga.core.survey_forms.settings.model.SurveyFormsSettings
import pro.qyoga.core.survey_forms.settings.model.SurveyFormsSettingsRepo
import pro.qyoga.core.survey_forms.settings.model.upsertSettings
import pro.qyoga.core.users.auth.dtos.QyogaUserDetails
import pro.qyoga.core.users.therapists.ref


@Controller
class SurveyFormsSettingsComponentController(
    private val surveyFormsSettingsRepo: SurveyFormsSettingsRepo
) {

    @GetMapping(PATH)
    fun handleGetComponent(
        @AuthenticationPrincipal therapist: QyogaUserDetails
    ): ModelAndView {
        val settings = surveyFormsSettingsRepo.findByTherapistRef(therapist.ref)
            ?: SurveyFormsSettings.createDefaultSettings(therapist.ref)
        return surveyFormsSettingsComponentModelAndView(form = settings.toForm(), saved = false)
    }

    @PutMapping(PATH)
    fun handleGetComponent(
        form: SurveyFormsSettingsForm,
        @AuthenticationPrincipal therapist: QyogaUserDetails
    ): ModelAndView {
        surveyFormsSettingsRepo.upsertSettings(therapist.ref, form.yandexAdminEmail)
        return surveyFormsSettingsComponentModelAndView(form = form, saved = true)
    }

    companion object {
        const val PATH = "/therapist/survey-forms/settings"
    }

}

private fun SurveyFormsSettings.toForm(): SurveyFormsSettingsForm = SurveyFormsSettingsForm(yandexAdminEmail ?: "")

private fun surveyFormsSettingsComponentModelAndView(form: SurveyFormsSettingsForm, saved: Boolean) =
    modelAndView(
        "therapist/survey-forms/settings :: surveyFormsSettings", model = mapOf(
            "yandexAdminEmail" to form.yandexAdminEmail,
            "saved" to saved,
        )
    )
