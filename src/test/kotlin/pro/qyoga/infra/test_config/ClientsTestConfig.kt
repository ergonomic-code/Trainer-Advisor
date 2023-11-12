package pro.qyoga.infra.test_config

import pro.qyoga.app.therapist.TherapistWebAppConfig
import pro.qyoga.core.clients.ClientsConfig
import pro.qyoga.core.programs.exercises.ExercisesConfig
import pro.qyoga.fixture.ClientsBackgrounds
import pro.qyoga.infra.images.ImagesConfig


object ClientsTestConfig {

    private val imagesConfig by lazy { ImagesConfig(SdjTestConfig.sdjConfig) }

    private val clientsConfig by lazy { ClientsConfig(SdjTestConfig.sdjConfig) }

    private val exercisesConfig by lazy { ExercisesConfig(SdjTestConfig.sdjConfig, imagesConfig) }

    private val therapistWebAppConfig by lazy { TherapistWebAppConfig(clientsConfig, exercisesConfig) }

    val clientListPageController by lazy { therapistWebAppConfig.clientsListPageController() }

    val clientsBackgrounds by lazy { ClientsBackgrounds(clientsConfig.clientsService()) }

}