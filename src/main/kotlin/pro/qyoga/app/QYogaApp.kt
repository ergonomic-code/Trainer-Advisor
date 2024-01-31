package pro.qyoga.app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import
import pro.qyoga.app.common.WebAppsCommonConfig
import pro.qyoga.app.components.QYogaComponetsConfig
import pro.qyoga.app.publc.PublicAppConfig
import pro.qyoga.app.therapist.TherapistWebAppConfig
import pro.qyoga.core.appointments.AppointmentsConfig
import pro.qyoga.core.clients.ClientsConfig
import pro.qyoga.core.therapy.TherapyConfig
import pro.qyoga.core.users.UsersConfig
import pro.qyoga.infra.auth.AuthConfig
import pro.qyoga.infra.db.SdjConfig
import pro.qyoga.infra.email.EmailsConfig
import pro.qyoga.infra.minio.MinioConfig
import pro.qyoga.infra.timezones.TimeZonesConfig
import pro.qyoga.infra.web.ThymeleafConfig
import pro.qyoga.infra.web.WebConfig
import pro.qyoga.platform.file_storage.FilesStorageConfig
import pro.qyoga.platform.spring.sdj.ErgoSdjConfig

@Import(
    // Apps
    WebAppsCommonConfig::class,
    PublicAppConfig::class,
    TherapistWebAppConfig::class,
    QYogaComponetsConfig::class,

    // Core
    AppointmentsConfig::class,
    ClientsConfig::class,
    TherapyConfig::class,
    UsersConfig::class,

    // Infra
    AuthConfig::class,
    WebConfig::class,
    ThymeleafConfig::class,
    SdjConfig::class,
    ErgoSdjConfig::class,
    MinioConfig::class,
    FilesStorageConfig::class,
    EmailsConfig::class,
    TimeZonesConfig::class
)
@SpringBootApplication
class QYogaApp

fun main(args: Array<String>) {
    runApplication<QYogaApp>(*args)
}
