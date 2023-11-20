package pro.qyoga.app.publc

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import pro.qyoga.core.users.UsersConfig


@Import(UsersConfig::class)
@ComponentScan
@Configuration
class PublicAppConfig