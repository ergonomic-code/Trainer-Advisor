package pro.qyoga.core.users

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import pro.qyoga.infra.auth.AuthConfig
import pro.qyoga.infra.db.SdjConfig


@Import(SdjConfig::class, AuthConfig::class)
@ComponentScan
@Configuration
class UsersConfig