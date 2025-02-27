package pro.qyoga.tests.cases.app.therapist.therapy.programs

import io.kotest.core.annotation.DisplayName
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import pro.qyoga.app.therapist.therapy.programs.list.ProgramsListPageController
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_REF
import pro.qyoga.tests.fixture.object_mothers.therapists.theTherapistUserDetails
import pro.qyoga.tests.fixture.object_mothers.therapy.programs.ProgramsObjectMother
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseKoTest


@DisplayName("Контроллер страницы списка программ")
class ProgramListPageControllerTest : QYogaAppIntegrationBaseKoTest({

    val programsListPageController = getBean<ProgramsListPageController>()

    "при наличии в системе нескольких терапевтов с программами" - {
        // Сетап
        presets.therapistsPreset.createTherapistWithProgram() // второй терапевт с программой

        val program = backgrounds.programs.createRandomProgram(therapistRef = THE_THERAPIST_REF)

        "метод получения страницы списка программ " - {

            // Действие
            val model = programsListPageController.getProgramsListPage(theTherapistUserDetails)

            "должен вернуть страницу с программами только аутентифицированного терапевта" {
                // Проверка
                model.programs shouldHaveSize 1
                model.programs.single().id shouldBe program.id
            }

        }

        "метод получения отфильтрованной страницы списка программ " - {
            // Действие

            val model = programsListPageController.searchPrograms(
                ProgramsObjectMother.aProgramSearchFilter(titleKeyword = program.title),
                theTherapistUserDetails
            )

            "должен вернуть страницу с программами только аутентифицированного терапевта" {
                // Проверка
                model.programs shouldHaveSize 1
                model.programs.single().id shouldBe program.id
            }
        }
    }

})