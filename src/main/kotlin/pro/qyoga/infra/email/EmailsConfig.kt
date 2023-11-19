package pro.qyoga.infra.email

import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration


@ImportAutoConfiguration(MailSenderAutoConfiguration::class)
@ComponentScan
@Configuration
class EmailsConfig