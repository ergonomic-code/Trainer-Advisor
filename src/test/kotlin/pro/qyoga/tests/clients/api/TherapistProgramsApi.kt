package pro.qyoga.tests.clients.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.module.kotlin.convertValue
import io.restassured.http.ContentType
import io.restassured.http.Cookie
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import io.restassured.response.Response
import org.hamcrest.CoreMatchers
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.http.HttpStatus
import pro.qyoga.core.therapy.exercises.dtos.ExerciseSummaryDto
import pro.qyoga.core.therapy.programs.dtos.CreateProgramRequest
import pro.qyoga.tests.clients.pages.therapist.therapy.programs.CreateProgramPage
import pro.qyoga.tests.clients.pages.therapist.therapy.programs.ProgramsListPage
import pro.qyoga.tests.infra.test_config.spring.context

class TherapistProgramsApi(override val authCookie: Cookie) : AuthorizedApi {

    private val mapper = context.getBean(ObjectMapper::class.java)

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
                formParams(CreateProgramPage.CreateProgramForm.EXERCISE_IDS_INPUT_NAME, it.toString())
            }

            this
        } When {
            post(CreateProgramPage.path)
        }
    }

    fun downloadDocx(programId: Long, expectedStatus: HttpStatus = HttpStatus.OK): ByteArray {
        return Given {
            authorized()

            pathParam("programId", programId)
        } When {
            get(ProgramsListPage.programDocxPath)
        } Then {
            statusCode(expectedStatus.value())
        } Extract {
            body().asByteArray()
        }
    }

    fun createProgramForError(createProgramRequest: CreateProgramRequest, therapeuticTaskName: String): Document {
        return createProgram(createProgramRequest, therapeuticTaskName)
            .Then {
                statusCode(HttpStatus.OK.value())
                header("HX-Redirect", CoreMatchers.nullValue())
            } Extract {
            Jsoup.parse(body().asString())
        }

    }

    fun searchExercises(keyword: String): Iterable<ExerciseSummaryDto> {
        return Given {
            authorized()
            accept(ContentType.JSON)

            queryParam(CreateProgramPage.SEARCH_KEY, keyword)
        } When {
            get(CreateProgramPage.SEARCH_EXERCISE_PATH)
        } Then {
            statusCode(HttpStatus.OK.value())
        } Extract {
            val node = body().`as`(ObjectNode::class.java)
            mapper.convertValue(node.get("content"))
        }
    }

    fun deleteProgram(programId: Long): Response {
        return Given {
            authorized()

            pathParam("programId", programId)
        } When {
            delete(ProgramsListPage.programPath)
        }
    }

}
