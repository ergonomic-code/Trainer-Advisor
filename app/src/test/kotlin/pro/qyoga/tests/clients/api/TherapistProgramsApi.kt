package pro.qyoga.tests.clients.api

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.http.HttpCookie
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.client.RestTestClient
import org.springframework.test.web.servlet.client.returnResult
import org.springframework.util.MultiValueMap
import pro.qyoga.core.therapy.exercises.dtos.ExerciseSummaryDto
import pro.qyoga.core.therapy.programs.dtos.CreateProgramRequest
import pro.qyoga.core.therapy.programs.dtos.ProgramsSearchFilter
import pro.qyoga.tests.infra.test_config.spring.context
import pro.qyoga.tests.infra.web.pageRestTestClient
import pro.qyoga.tests.pages.therapist.therapy.programs.CreateProgramForm
import pro.qyoga.tests.pages.therapist.therapy.programs.CreateProgramPage
import pro.qyoga.tests.pages.therapist.therapy.programs.EditProgramPage
import pro.qyoga.tests.pages.therapist.therapy.programs.ProgramsListPage
import pro.qyoga.tests.platform.spring.rest_test_client.formData
import pro.qyoga.tests.platform.spring.rest_test_client.getBodyAsString
import tools.jackson.databind.ObjectMapper
import tools.jackson.databind.node.ObjectNode
import tools.jackson.module.kotlin.convertValue


class TherapistProgramsApi(
    override val authCookie: HttpCookie,
    private val restTestClient: RestTestClient = pageRestTestClient
) : AuthorizedApi {

    private val mapper = context.getBean(ObjectMapper::class.java)

    fun getProgramsListPage(): Document {
        return restTestClient.get()
            .uri(ProgramsListPage.path)
            .accept(MediaType.TEXT_HTML)
            .authorized()
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.OK)
            .getBodyAsString()
            .let { Jsoup.parse(it) }
    }

    fun getCreateProgramPage(): Document {
        return restTestClient.get()
            .uri(CreateProgramPage.path)
            .accept(MediaType.TEXT_HTML)
            .authorized()
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.OK)
            .getBodyAsString()
            .let { Jsoup.parse(it) }
    }

    fun createProgram(createProgramRequest: CreateProgramRequest, therapeuticTaskName: String): RestTestClient.ResponseSpec {
        return restTestClient.post()
            .uri(CreateProgramPage.path)
            .accept(MediaType.TEXT_HTML)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(programForm(createProgramRequest, therapeuticTaskName))
            .authorized()
            .exchange()
    }


    fun downloadDocx(programId: Long, expectedStatus: HttpStatus = HttpStatus.OK): ByteArray {
        return restTestClient.get()
            .uri(ProgramsListPage.programDocxPath, programId)
            .accept(MediaType.TEXT_HTML)
            .authorized()
            .exchange()
            .expectStatus().isEqualTo(expectedStatus.value())
            .returnResult().responseBodyContent
    }

    fun createProgramForError(createProgramRequest: CreateProgramRequest, therapeuticTaskName: String): Document {
        return createProgram(createProgramRequest, therapeuticTaskName)
            .expectStatus().isOk
            .expectHeader().doesNotExist("HX-Redirect")
            .getBodyAsString()
            .let { Jsoup.parse(it) }

    }

    fun searchExercises(keyword: String): Iterable<ExerciseSummaryDto> {
        val node = restTestClient.get()
            .uri("${CreateProgramPage.searchExercisePath}?${CreateProgramPage.searchKey}={keyword}", keyword)
            .accept(MediaType.APPLICATION_JSON)
            .authorized()
            .exchange()
            .expectStatus().isOk
            .returnResult<ObjectNode>().responseBody!!
        return mapper.convertValue(node.get("content"))
    }

    fun deleteProgram(programId: Long): RestTestClient.ResponseSpec {
        return restTestClient.delete()
            .uri(ProgramsListPage.programPath, programId)
            .accept(MediaType.TEXT_HTML)
            .authorized()
            .exchange()
    }

    fun getProgramEditPage(programId: Long, expectedStatus: HttpStatus = HttpStatus.OK): Document {
        return restTestClient.get()
            .uri(ProgramsListPage.programPath, programId)
            .accept(MediaType.TEXT_HTML)
            .authorized()
            .exchange()
            .expectStatus().isEqualTo(expectedStatus.value())
            .getBodyAsString()
            .let { Jsoup.parse(it) }
    }

    fun updateProgram(
        programId: Long,
        updateProgramRequest: CreateProgramRequest,
        therapeuticTaskName: String
    ): RestTestClient.ResponseSpec {
        return restTestClient.put()
            .uri(EditProgramPage.path, programId)
            .accept(MediaType.TEXT_HTML)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(programForm(updateProgramRequest, therapeuticTaskName))
            .authorized()
            .exchange()
    }

    fun updateProgramForError(
        programId: Long,
        updateProgramRequest: CreateProgramRequest,
        therapeuticTaskName: String,
        expectedStatus: HttpStatus = HttpStatus.OK
    ): Document {
        return updateProgram(programId, updateProgramRequest, therapeuticTaskName)
            .expectStatus().isEqualTo(expectedStatus.value())
            .getBodyAsString()
            .let { Jsoup.parse(it) }
    }

    private fun programForm(
        createProgramRequest: CreateProgramRequest,
        therapeuticTaskName: String
    ): MultiValueMap<String, String> {
        return formData(
            CreateProgramForm.titleInput.name to createProgramRequest.title,
            CreateProgramForm.therapeuticTaskInput.name to therapeuticTaskName
        ).apply {
            createProgramRequest.exerciseIds.forEach {
                add(CreateProgramForm.exerciseIdsInputName, it.toString())
            }
        }
    }

    fun searchPrograms(programsSearchFilter: ProgramsSearchFilter): Document {
        return restTestClient.get()
            .uri { builder ->
                builder.path(ProgramsListPage.SearchForm.action.url).queryParams(formData(
                    ProgramsListPage.SearchForm.titleKeywordInput.name to programsSearchFilter.titleKeyword,
                    ProgramsListPage.SearchForm.therapeuticTaskKeywordInput.name to programsSearchFilter.therapeuticTaskKeyword,
                )).build()
            }
            .accept(MediaType.TEXT_HTML)
            .authorized()
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.OK)
            .getBodyAsString()
            .let { Jsoup.parse(it) }
    }

}
