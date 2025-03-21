package pro.qyoga.tests.cases.app.therapist.therapy.exercises

import io.kotest.core.spec.DisplayName
import io.kotest.matchers.collections.shouldHaveSize
import org.springframework.data.domain.Pageable
import pro.qyoga.app.therapist.therapy.exercises.ExercisesListPageController
import pro.qyoga.tests.assertions.shouldMatch
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_REF
import pro.qyoga.tests.fixture.object_mothers.therapists.theTherapistUserDetails
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseKoTest


@DisplayName("Контроллер страницы списка упражнений")
class ExercisesListPageControllerTest : QYogaAppIntegrationBaseKoTest({

    val exercisesPageController = getBean<ExercisesListPageController>()

    "метод получения списка упражнений" - {

        "при наличии в системе упражнений у нескольких терапевтов" - {
            // Сетап
            val exercise = backgrounds.exercises.createExercise(ownerRef = THE_THERAPIST_REF)
            presets.therapistsFixturePreset.createTherapistWithExercise()

            "должен возвращать только упражнения аутентифицированного терапевта" {

                // Действие
                val model = exercisesPageController.getExercises(Pageable.ofSize(2), theTherapistUserDetails)

                // Проверка
                model.exercises shouldHaveSize 1
                model.exercises.single() shouldMatch exercise
            }
        }

    }

})