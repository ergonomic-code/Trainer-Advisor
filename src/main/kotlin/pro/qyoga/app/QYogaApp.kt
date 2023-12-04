package pro.qyoga.app

import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import pro.qyoga.app.common.WebAppsCommonConfig
import pro.qyoga.app.publc.PublicAppConfig
import pro.qyoga.app.therapist.TherapistWebAppConfig
import pro.qyoga.core.users.UsersConfig
import pro.qyoga.infra.db.SdjConfig
import pro.qyoga.infra.web.WebConfig

@Import(
    WebAppsCommonConfig::class,
    PublicAppConfig::class,
    TherapistWebAppConfig::class,

    UsersConfig::class,

    WebConfig::class,
    SdjConfig::class,
)
@Configuration
class QYogaApp

fun main(args: Array<String>) {
    runApplication<QYogaApp>(*args)
}
