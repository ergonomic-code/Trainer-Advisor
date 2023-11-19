package pro.qyoga.core.users

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import pro.qyoga.infra.auth.AuthConfig
import pro.qyoga.infra.db.SdjConfig
import pro.qyoga.infra.email.EmailsConfig


@Import(SdjConfig::class, AuthConfig::class, EmailsConfig::class)
@ComponentScan
@Configuration
class UsersConfig