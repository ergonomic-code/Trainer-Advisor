package pro.qyoga.tests.clients.api

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.http.HttpCookie
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.client.MultipartBodyBuilder
import org.springframework.test.web.servlet.client.RestTestClient
import pro.azhidkov.platform.file_storage.api.StoredFile
import pro.azhidkov.platform.java.time.toDecimalMinutes
import pro.qyoga.core.therapy.exercises.dtos.CreateExerciseRequest
import pro.qyoga.core.therapy.exercises.dtos.ExerciseSearchDto
import pro.qyoga.core.therapy.exercises.dtos.ExerciseSummaryDto
import pro.qyoga.tests.infra.web.pageRestTestClient
import pro.qyoga.tests.pages.therapist.therapy.exercises.*
import pro.qyoga.tests.platform.spring.rest_test_client.formData
import pro.qyoga.tests.platform.spring.rest_test_client.getBodyAsString


class TherapistExercisesApi(
    override val authCookie: HttpCookie,
    private val restTestClient: RestTestClient = pageRestTestClient
) : AuthorizedApi {

    fun getCreateExercisesPage(): Document {
        return restTestClient.get()
            .uri(CreateExercisePage.path)
            .accept(MediaType.TEXT_HTML)
            .authorized()
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.OK)
            .getBodyAsString()
            .let { Jsoup.parse(it) }

    }

    fun createExercise(exercise: CreateExerciseRequest, images: Map<Long, StoredFile>) {
        val multipart = MultipartBodyBuilder()
        multipart.part(CreateExerciseForm.title.name, exercise.summary.title)
        multipart.part(CreateExerciseForm.duration.name, exercise.summary.duration.toDecimalMinutes().toString())
        multipart.part(CreateExerciseForm.type.name, exercise.summary.type.toString())
        multipart.part(CreateExerciseForm.description.name, exercise.summary.description)
        exercise.steps.forEachIndexed { idx, step ->
            multipart.part(CreateExerciseForm.stepsDescription(idx), step.description)
            val image = images[idx.toLong()]
            if (image != null) {
                multipart.part(CreateExerciseForm.stepImage(idx), image.content)
                    .filename(image.metaData.name)
                    .contentType(MediaType.parseMediaType(image.metaData.mediaType))
            }
        }
        restTestClient.post()
            .uri(CreateExerciseForm.action.url)
            .accept(MediaType.TEXT_HTML)
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .body(multipart.build())
            .authorized()
            .exchange()
            .expectStatus().isOk
            .expectHeader().valueEquals("HX-Redirect", ExercisesListPage.path)
    }

    fun getExercisesListPage(): Document {
        return restTestClient.get()
            .uri(ExercisesListPage.path)
            .accept(MediaType.TEXT_HTML)
            .authorized()
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.OK)
            .getBodyAsString()
            .let { Jsoup.parse(it) }

    }

    fun searchExercises(searchDto: ExerciseSearchDto): Document {
        return restTestClient.get()
            .uri { builder ->
                builder.path(ExercisesListPage.ExercisesSearchForm.action.url).queryParams(formData(
                    ExercisesListPage.ExercisesSearchForm.title.name to searchDto.title,
                    ExercisesListPage.ExercisesSearchForm.exercisesType.name to searchDto.exerciseType,
                )).build()
            }
            .accept(MediaType.TEXT_HTML)
            .authorized()
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.OK)
            .getBodyAsString()
            .let { Jsoup.parse(it) }
    }

    fun getEditExercisePage(exerciseId: Long, expectedStatus: HttpStatus = HttpStatus.OK): Document {
        return restTestClient.get()
            .uri(EditExercisePage.PATH, exerciseId)
            .accept(MediaType.TEXT_HTML)
            .authorized()
            .exchange()
            .expectStatus().isEqualTo(expectedStatus.value())
            .getBodyAsString()
            .let { Jsoup.parse(it) }
    }

    fun editExercise(exerciseId: Long, exercise: ExerciseSummaryDto) {
        restTestClient.put()
            .uri(EditExerciseForm.action.url, exerciseId)
            .accept(MediaType.TEXT_HTML)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(formData(
                EditExerciseForm.title.name to exercise.title,
                EditExerciseForm.duration.name to exercise.duration.toDecimalMinutes(),
                EditExerciseForm.type.name to exercise.type,
                EditExerciseForm.description.name to exercise.description,
            ))
            .authorized()
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.OK)
            .expectHeader().valueEquals("HX-Redirect", ExercisesListPage.path)
    }

    fun getStepImage(exerciseId: Long, stepIdx: Int, expectedStatus: HttpStatus = HttpStatus.OK): ByteArray {
        return restTestClient.get()
            .uri(EditExerciseForm.IMAGE_PATH, exerciseId, stepIdx)
            .accept(MediaType.TEXT_HTML)
            .authorized()
            .exchange()
            .expectStatus().isEqualTo(expectedStatus.value())
            .returnResult().responseBodyContent
    }

    fun deleteExercise(exerciseId: Long, expectedStatus: HttpStatus = HttpStatus.OK): ByteArray {
        return restTestClient.delete()
            .uri(ExercisesListPage.deleteAction, exerciseId)
            .accept(MediaType.TEXT_HTML)
            .authorized()
            .exchange()
            .expectStatus().isEqualTo(expectedStatus.value())
            .returnResult().responseBodyContent
    }

    fun getExerciseModal(exerciseId: Long, expectedStatus: HttpStatus = HttpStatus.OK): Document {
        return restTestClient.get()
            .uri(ExerciseModal.PATH, exerciseId)
            .accept(MediaType.TEXT_HTML)
            .authorized()
            .exchange()
            .expectStatus().isEqualTo(expectedStatus.value())
            .getBodyAsString()
            .let { Jsoup.parse(it) }
    }

}