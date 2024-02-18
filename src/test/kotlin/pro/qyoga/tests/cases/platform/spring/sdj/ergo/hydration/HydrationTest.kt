package pro.qyoga.tests.cases.platform.spring.sdj.ergo.hydration

import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.data.jdbc.core.JdbcAggregateOperations
import org.springframework.data.jdbc.core.mapping.AggregateReference
import pro.azhidkov.platform.spring.sdj.erpo.hydration.*
import pro.qyoga.core.clients.journals.model.JournalEntry
import pro.qyoga.core.therapy.exercises.model.ExerciseStep
import pro.qyoga.core.therapy.therapeutic_tasks.model.TherapeuticTask
import pro.qyoga.core.users.therapists.Therapist
import pro.qyoga.tests.fixture.object_mothers.FilesObjectMother
import pro.qyoga.tests.fixture.backgrounds.Backgrounds
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_FIRST_NAME
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_ID
import pro.qyoga.tests.infra.db.setupDb
import pro.qyoga.tests.infra.db.testDataSource
import pro.qyoga.tests.infra.test_config.spring.context
import pro.qyoga.tests.platform.spring.context.getBean
import java.time.LocalDate


class HydrationTest {

    private val backgrounds = context.getBean<Backgrounds>()

    @BeforeEach
    fun setup() {
        testDataSource.setupDb()
    }

    @Test
    fun `When for list of entities single property are hydrated, then all entities should have specified property hydrated`() {
        // Given
        val jdbcAggregateOperations = context.getBean<JdbcAggregateOperations>()
        val refs = (1..3).map { jdbcAggregateOperations.save(FilesObjectMother.randomImage().metaData) }
        val aggs = refs.map { ExerciseStep("", AggregateReference.to(it.id)) }

        // When
        val hydrated =
            jdbcAggregateOperations.hydrate(
                aggs,
                FetchSpec(
                    listOf(
                        PropertyFetchSpec(
                            ExerciseStep::imageId
                        )
                    )
                )
            )

        // Then
        (refs zip hydrated).forAll { (origin, fetched) ->
            fetched.imageId.shouldBeInstanceOf<AggregateReferenceTarget<*, *>>()
            fetched.imageId.resolveOrThrow().name shouldBe origin.name
            fetched.imageId.resolveOrThrow().size shouldBe origin.size
        }
    }

    @Test
    fun `When list of properties are hydrated, then all specified properties of entity should be hydrated`() {
        // Given
        val jdbcAggregateOperations = context.getBean<JdbcAggregateOperations>()
        val client = backgrounds.clients.createClients(1).single().ref()
        val task = backgrounds.therapeuticTasks.createTherapeuticTask(THE_THERAPIST_ID).ref()
        val entry = jdbcAggregateOperations.save(JournalEntry(client, LocalDate.now(), task, ""))

        // When
        val hydrated = jdbcAggregateOperations.hydrate(
            listOf(entry),
            FetchSpec(
                JournalEntry::client,
                JournalEntry::therapeuticTask
            )
        ).first()

        // Then
        hydrated.client.shouldBeInstanceOf<AggregateReferenceTarget<*, *>>()
        hydrated.therapeuticTask.shouldBeInstanceOf<AggregateReferenceTarget<*, *>>()
    }

    @Test
    fun `When entity is hydrated with recursive fetch spec, then nested entity should be hydrated`() {
        // Given
        val jdbcAggregateOperations = context.getBean<JdbcAggregateOperations>()
        val client = backgrounds.clients.createClients(1).single()
        val clientRef = client.ref()
        val task = backgrounds.therapeuticTasks.createTherapeuticTask(THE_THERAPIST_ID).ref()
        val entry = jdbcAggregateOperations.save(JournalEntry(clientRef, LocalDate.now(), task, ""))

        val entryTaskFetchSpec = PropertyFetchSpec(
            JournalEntry::therapeuticTask,
            FetchSpec(TherapeuticTask::owner)
        )
        val entryClientFetchSpec =
            PropertyFetchSpec(JournalEntry::client)

        // When
        val hydrated = jdbcAggregateOperations.hydrate(
            listOf(entry),
            FetchSpec(
                listOf(
                    entryTaskFetchSpec,
                    entryClientFetchSpec
                )
            )
        ).first()

        // Then
        val ownerRef = hydrated.therapeuticTask.resolveOrThrow().owner
        ownerRef.shouldBeInstanceOf<AggregateReferenceTarget<Therapist, Long>>()
        val owner = ownerRef.resolveOrThrow()
        owner.firstName shouldBe THE_THERAPIST_FIRST_NAME
    }

}