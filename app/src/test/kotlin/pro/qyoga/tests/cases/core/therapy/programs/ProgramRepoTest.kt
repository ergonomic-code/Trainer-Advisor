package pro.qyoga.tests.cases.core.therapy.programs

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import pro.qyoga.core.therapy.programs.ProgramsRepo
import pro.qyoga.core.therapy.programs.findDocxById
import pro.qyoga.tests.assertions.shouldMatch
import pro.qyoga.tests.infra.test_config.spring.context
import pro.qyoga.tests.infra.web.QYogaAppBaseTest
import pro.qyoga.tests.platform.spring.context.getBean

@DisplayName("Репозиторий программ")
class ProgramRepoTest : QYogaAppBaseTest() {

    private val repo = context.getBean<ProgramsRepo>()

    @DisplayName("Должен находить корректные docx-программы по идентификатору со всеми упражнениями и их шагами")
    @Test
    fun findDocxProgram() {
        // Given
        val program = backgrounds.programs.createRandomProgram(exercisesCount = 3, stepsInEachExercise = 5)

        // When
        val docx = repo.findDocxById(program.id)

        // Then
        docx shouldMatch program
    }

}