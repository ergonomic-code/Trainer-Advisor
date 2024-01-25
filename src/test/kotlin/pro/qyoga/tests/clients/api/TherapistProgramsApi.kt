package pro.qyoga.tests.clients.api

import io.restassured.http.Cookie
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import io.restassured.response.Response
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.http.HttpStatus
import pro.qyoga.core.therapy.programs.dtos.CreateProgramRequest
import pro.qyoga.tests.clients.pages.therapist.therapy.programs.CreateProgramPage
import pro.qyoga.tests.clients.pages.therapist.therapy.programs.ProgramsListPage

class TherapistProgramsApi(override val authCookie: Cookie) : AuthorizedApi {

    fun getProgramsListPage(): Document {
        return Given {
            authorized()
        } When {
            get(ProgramsListPage.path)
        } Then {
            statusCode(HttpStatus.OK.value())
        } Extract {
            Jsoup.parse(body().asString())
        }
    }

    fun getCreateProgramPage(): Document {
        return Given {
            authorized()
        } When {
            get(CreateProgramPage.path)
        } Then {
            statusCode(HttpStatus.OK.value())
        } Extract {
            Jsoup.parse(body().asString())
        }
    }

    fun createProgram(createProgramRequest: CreateProgramRequest, therapeuticTaskName: String): Response {
        return Given {
            authorized()

            formParam(CreateProgramPage.CreateProgramForm.titleInput.name, createProgramRequest.title)
            formParam(CreateProgramPage.CreateProgramForm.therapeuticTaskInput.name, therapeuticTaskName)

            createProgramRequest.exerciseIds.forEach {
                formParams(CreateProgramPage.CreateProgramForm.exerciseIdInputName, it.toString())
            }

            this
        } When {
            post(CreateProgramPage.path)
        }
    }

    fun downloadDocx(programId: Long): ByteArray {
        return Given {
            authorized()

            pathParam("programId", programId)
        } When {
            get(ProgramsListPage.programDocxPath)
        } Then {
            statusCode(HttpStatus.OK.value())
        } Extract {
            body().asByteArray()
        }
    }

}
