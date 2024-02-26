package pro.qyoga.tests.cases.core.therapy.programs.impl

import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import pro.qyoga.core.therapy.programs.impl.ProgramsRepo
import pro.qyoga.core.therapy.programs.impl.findDocxOrNull
import pro.qyoga.tests.fixture.backgrounds.Backgrounds
import pro.qyoga.tests.infra.db.setupDb
import pro.qyoga.tests.infra.db.testDataSource
import pro.qyoga.tests.infra.test_config.spring.context
import pro.qyoga.tests.platform.spring.context.getBean

class ProgramRepoTest {
    private val backgrounds = context.getBean<Backgrounds>()
    private val repo = context.getBean<ProgramsRepo>()

    @BeforeEach
    fun setup() {
        testDataSource.setupDb()
    }

    @Test
    fun should_execute_query() {
        // Given
        val programs = backgrounds.programs.createRandomProgram(exercisesCount = 3, stepsInEachExercise = 5)
        // When
        val docx = repo.findDocxOrNull(programs.id)
        // Then
        docx shouldNotBe null
        docx!!.exercises shouldHaveSize 3
        docx.exercises.forEach {
            it.steps shouldHaveSize 5
        }
    }
}