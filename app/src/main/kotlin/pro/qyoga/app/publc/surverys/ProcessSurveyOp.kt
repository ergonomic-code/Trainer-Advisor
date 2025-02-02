package pro.qyoga.app.publc.surverys

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import pro.qyoga.core.clients.cards.ClientsRepo
import pro.qyoga.core.clients.cards.findByPhone
import pro.qyoga.core.clients.cards.model.PhoneNumber
import pro.qyoga.core.survey_forms.settings.model.SurveyFormsSettingsRepo
import pro.qyoga.core.survey_forms.settings.model.findByYandexAdminEmail
import java.time.LocalDate


@Component
class ProcessSurveyOp(
    private val clientsRepo: ClientsRepo,
    private val surveyFormsSettingsRepo: SurveyFormsSettingsRepo
) : (SurveyRq) -> Unit {

    @Transactional
    override operator fun invoke(surveyRq: SurveyRq) {
        val therapistRef = surveyFormsSettingsRepo.findByYandexAdminEmail(surveyRq.yandexAdminEmail)
            ?.therapistRef
            ?: throw InvalidSurveyException.surveySettingsNotFoundForAdminEmail()

        var client = clientsRepo.findByPhone(therapistRef, PhoneNumber.of(surveyRq.survey.phone))
            ?: surveyRq.survey.toClient(therapistRef)

        val today = LocalDate.now()
        client = client
            .prependComplaints(formatComplaintsEntry(surveyRq, today))
            .prependAnamnesis(formatAnamnesisEntry(surveyRq, today))

        clientsRepo.save(client)
    }

}

