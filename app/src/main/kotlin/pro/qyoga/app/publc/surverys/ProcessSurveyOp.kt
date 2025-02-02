package pro.qyoga.app.publc.surverys

import org.springframework.stereotype.Component
import pro.qyoga.core.clients.cards.ClientsRepo
import pro.qyoga.core.clients.cards.findByPhone
import pro.qyoga.core.survey_forms.settings.model.SurveyFormsSettingsRepo
import pro.qyoga.core.users.therapists.TherapistRef


@Component
class ProcessSurveyOp(
    private val clientsRepo: ClientsRepo,
    private val surveyFormsSettingsRepo: SurveyFormsSettingsRepo
) : (TherapistRef, SurveyRq) -> Unit {

    override operator fun invoke(surveyRq: SurveyRq) {
        val client = clientsRepo.findByPhone(therapistRef, surveyRq.phone)
    }

}

