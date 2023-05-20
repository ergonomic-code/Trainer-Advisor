package nsu.fit.qyoga.app

import nsu.fit.qyoga.core.clients.ClientConfig
import nsu.fit.qyoga.core.completingQuestionnaires.CompletingQuestionnairesConfig
import nsu.fit.qyoga.core.exercises.ExercisesConfig
import nsu.fit.qyoga.core.images.ImagesConfig
import nsu.fit.qyoga.core.questionnaires.QuestionnairesConfig
import nsu.fit.qyoga.core.therapeutic_purposes.TherapeuticPurposesConfig
import nsu.fit.qyoga.core.users.UsersConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import

@Import(
    UsersConfig::class,
    ExercisesConfig::class,
    QuestionnairesConfig::class,
    ClientConfig::class,
    ImagesConfig::class,
    TherapeuticPurposesConfig::class,
    CompletingQuestionnairesConfig::class
)
@SpringBootApplication
class QYogaApplication

fun main(args: Array<String>) {
    runApplication<QYogaApplication>(*args)
}
