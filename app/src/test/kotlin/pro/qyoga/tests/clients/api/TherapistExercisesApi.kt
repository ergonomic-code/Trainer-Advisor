package pro.qyoga.tests.clients.api

import io.restassured.http.Cookie
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.http.HttpStatus
import pro.azhidkov.platform.file_storage.api.StoredFile
import pro.azhidkov.platform.java.time.toDecimalMinutes
import pro.qyoga.core.therapy.exercises.dtos.CreateExerciseRequest
import pro.qyoga.core.therapy.exercises.dtos.ExerciseSearchDto
import pro.qyoga.core.therapy.exercises.dtos.ExerciseSummaryDto
import pro.qyoga.tests.pages.therapist.therapy.exercises.*


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

    fun createExercise(exercise: CreateExerciseRequest, images: Map<Long, StoredFile>) {
        Given {
            contentType("multipart/form-data; charset=UTF-8")
            authorized()

            formParam(CreateExerciseForm.title.name, exercise.summary.title)
            formParam(CreateExerciseForm.duration.name, exercise.summary.duration.toDecimalMinutes())
            formParam(CreateExerciseForm.type.name, exercise.summary.type)
            formParam(CreateExerciseForm.description.name, exercise.summary.description)

            exercise.steps.forEachIndexed { idx, step ->
                formParam(CreateExerciseForm.stepsDescription(idx), step.description)
                val image = images[idx.toLong()]
                if (image != null) {
                    multiPart(
                        CreateExerciseForm.stepImage(idx),
                        image.metaData.name,
                        image.content,
                        image.metaData.mediaType
                    )
                }
            }

            this
        } When {
            post(CreateExerciseForm.action.url)
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

    fun getEditExercisePage(exerciseId: Long, expectedStatus: HttpStatus = HttpStatus.OK): Document {
        return Given {
            authorized()
            pathParam("exerciseId", exerciseId)
        } When {
            get(EditExercisePage.PATH)
        } Then {
            statusCode(expectedStatus.value())
        } Extract {
            Jsoup.parse(body().asString())
        }
    }

    fun editExercise(exerciseId: Long, exercise: ExerciseSummaryDto) {
        Given {
            authorized()

            pathParam("exerciseId", exerciseId)

            formParam(EditExerciseForm.title.name, exercise.title)
            formParam(EditExerciseForm.duration.name, exercise.duration.toDecimalMinutes())
            formParam(EditExerciseForm.type.name, exercise.type)
            formParam(EditExerciseForm.description.name, exercise.description)
        } When {
            put(EditExerciseForm.action.url)
        } Then {
            statusCode(HttpStatus.OK.value())
            header("HX-Redirect", ExercisesListPage.path)
        }
    }

    fun getStepImage(exerciseId: Long, stepIdx: Int, expectedStatus: HttpStatus = HttpStatus.OK): ByteArray {
        return Given {
            authorized()

            pathParam("exerciseId", exerciseId)
            pathParam("stepIdx", stepIdx)
        } When {
            get(EditExerciseForm.IMAGE_PATH)
        } Then {
            statusCode(expectedStatus.value())
        } Extract {
            asByteArray()
        }
    }

    fun deleteExercise(exerciseId: Long, expectedStatus: HttpStatus = HttpStatus.OK): ByteArray {
        return Given {
            authorized()

            pathParam("exerciseId", exerciseId)
        } When {
            delete(ExercisesListPage.deleteAction)
        } Then {
            statusCode(expectedStatus.value())
        } Extract {
            asByteArray()
        }
    }

}