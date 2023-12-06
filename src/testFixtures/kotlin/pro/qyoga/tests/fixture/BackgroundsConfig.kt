package pro.qyoga.tests.fixture

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Import
import pro.qyoga.core.clients.ClientsConfig
import pro.qyoga.core.therapy.TherapyConfig


@Import(ClientsConfig::class, TherapyConfig::class)
@TestConfiguration
@ComponentScan
class BackgroundsConfig