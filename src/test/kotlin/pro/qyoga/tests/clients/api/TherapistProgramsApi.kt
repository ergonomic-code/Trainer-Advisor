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
import io.restassured.specification.RequestSpecification
import org.hamcrest.CoreMatchers
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.http.HttpStatus
import pro.qyoga.core.therapy.exercises.dtos.ExerciseSummaryDto
import pro.qyoga.core.therapy.programs.dtos.CreateProgramRequest
import pro.qyoga.core.therapy.programs.dtos.ProgramsSearchFilter
import pro.qyoga.tests.pages.therapist.therapy.programs.CreateProgramForm
import pro.qyoga.tests.pages.therapist.therapy.programs.CreateProgramPage
import pro.qyoga.tests.pages.therapist.therapy.programs.EditProgramPage
import pro.qyoga.tests.pages.therapist.therapy.programs.ProgramsListPage
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

            fillProgramFormParams(createProgramRequest, therapeuticTaskName)
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

            queryParam(CreateProgramPage.searchKey, keyword)
        } When {
            get(CreateProgramPage.searchExercisePath)
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

    fun getProgramEditPage(programId: Long, expectedStatus: HttpStatus = HttpStatus.OK): Document {
        return Given {
            authorized()

            pathParam("programId", programId)
        } When {
            get(ProgramsListPage.programPath)
        } Then {
            statusCode(expectedStatus.value())
        } Extract {
            Jsoup.parse(body().asString())
        }
    }

    fun updateProgram(
        programId: Long,
        updateProgramRequest: CreateProgramRequest,
        therapeuticTaskName: String
    ): Response {
        return Given {
            authorized()

            pathParam("programId", programId)
            fillProgramFormParams(updateProgramRequest, therapeuticTaskName)
        } When {
            put(EditProgramPage.path)
        }
    }

    fun updateProgramForError(
        programId: Long,
        updateProgramRequest: CreateProgramRequest,
        therapeuticTaskName: String,
        expectedStatus: HttpStatus = HttpStatus.OK
    ): Document {
        return updateProgram(programId, updateProgramRequest, therapeuticTaskName)
            .Then {
                statusCode(expectedStatus.value())
            } Extract {
            Jsoup.parse(body().asString())
        }
    }

    private fun RequestSpecification.fillProgramFormParams(
        createProgramRequest: CreateProgramRequest,
        therapeuticTaskName: String
    ): RequestSpecification {
        formParam(CreateProgramForm.titleInput.name, createProgramRequest.title)
        formParam(CreateProgramForm.therapeuticTaskInput.name, therapeuticTaskName)

        createProgramRequest.exerciseIds.forEach {
            formParams(CreateProgramForm.exerciseIdsInputName, it.toString())
        }

        return this
    }

    fun searchPrograms(programsSearchFilter: ProgramsSearchFilter): Document {
        return Given {
            authorized()

            formParam(ProgramsListPage.SearchForm.titleKeywordInput.name, programsSearchFilter.titleKeyword)
            formParam(
                ProgramsListPage.SearchForm.therapeuticTaskKeywordInput.name,
                programsSearchFilter.therapeuticTaskKeyword
            )
        } When {
            get(ProgramsListPage.SearchForm.action.url)
        } Then {
            statusCode(HttpStatus.OK.value())
        } Extract {
            Jsoup.parse(body().asString())
        }
    }

}
