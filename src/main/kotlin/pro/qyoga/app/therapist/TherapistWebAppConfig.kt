package pro.qyoga.app.therapist

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import pro.qyoga.core.clients.ClientsConfig
import pro.qyoga.core.therapy.TherapyConfig


@Import(ClientsConfig::class, TherapyConfig::class)
@ComponentScan
@Configuration
class TherapistWebAppConfig