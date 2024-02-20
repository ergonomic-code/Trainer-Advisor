package pro.qyoga.tests.clients.api

import io.restassured.http.Cookie
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.http.HttpStatus
import pro.qyoga.app.therapist.appointments.types.components.AppointmentTypesComboBoxController


class TherapistAppointmentTypesApi(override val authCookie: Cookie) : AuthorizedApi {

    fun autocompleteSearch(searchKey: String?): Document {
        return Given {
            authorized()
            if (searchKey != null) {
                queryParam("appointmentTypeTitle", searchKey)
            }
            this
        } When {
            get(AppointmentTypesComboBoxController.PATH)
        } Then {
            statusCode(HttpStatus.OK.value())
        } Extract {
            Jsoup.parse(body().asString())
        }
    }

}