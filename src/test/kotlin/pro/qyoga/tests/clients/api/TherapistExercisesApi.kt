package pro.qyoga.tests.clients.api

import io.restassured.http.Cookie
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.http.HttpStatus
import pro.qyoga.core.therapy.exercises.api.CreateExerciseRequest
import pro.qyoga.core.therapy.exercises.api.ExerciseSearchDto
import pro.qyoga.platform.images.api.Image
import pro.qyoga.tests.clients.pages.therapist.therapy.exercises.CreateExercisePage
import pro.qyoga.tests.clients.pages.therapist.therapy.exercises.ExercisesListPage


class TherapistExercisesApi(override val authCookie: Cookie) : AuthorizedApi {

    fun getCreateExercisesPage(): Document {
        return Given {
            authorized()
        } When {
            get(CreateExercisePage.path)
        } Then {
            statusCode(HttpStatus.OK.value())
        } Extract {
            Jsoup.parse(body().asString())
        }

    }

    fun createExercise(exercise: CreateExerciseRequest, images: Map<Long, Image>) {
        Given {
            contentType("multipart/form-data; charset=UTF-8")
            authorized()

            formParam(CreateExercisePage.CreateExerciseForm.title.name, exercise.title)
            formParam(CreateExercisePage.CreateExerciseForm.duration.name, exercise.duration)
            formParam(CreateExercisePage.CreateExerciseForm.exerciseType.name, exercise.exerciseType)
            formParam(CreateExercisePage.CreateExerciseForm.description.name, exercise.description)

            exercise.steps.forEachIndexed { idx, step ->
                formParam(CreateExercisePage.CreateExerciseForm.stepsDescription(idx), step.description)
                val image = images[idx.toLong()]
                if (image != null) {
                    multiPart(
                        CreateExercisePage.CreateExerciseForm.stepImage(idx),
                        image.name,
                        image.data,
                        image.mediaType
                    )
                }
            }

            this
        } When {
            post(CreateExercisePage.CreateExerciseForm.action.url)
        } Then {
            statusCode(HttpStatus.OK.value())
            header("HX-Redirect", ExercisesListPage.path)
        }

    }

    fun getExercisesListPage(): Document {
        return Given {
            authorized()
        } When {
            get(ExercisesListPage.path)
        } Then {
            statusCode(HttpStatus.OK.value())
        } Extract {
            Jsoup.parse(body().asString())
        }

    }

    fun searchExercises(searchDto: ExerciseSearchDto): Document {
        return Given {
            authorized()
            formParam(ExercisesListPage.ExercisesSearchForm.title.name, searchDto.title)
            formParam(ExercisesListPage.ExercisesSearchForm.exercisesType.name, searchDto.exerciseType)
        } When {
            get(ExercisesListPage.ExercisesSearchForm.action.url)
        } Then {
            statusCode(HttpStatus.OK.value())
        } Extract {
            Jsoup.parse(body().asString())
        }
    }

}