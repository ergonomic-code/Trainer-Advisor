package nsu.fit.qyoga.app

import nsu.fit.qyoga.core.users.UsersConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import

@Import(UsersConfig::class)
@SpringBootApplication
class QYogaApplication

fun main(args: Array<String>) {
    runApplication<QYogaApplication>(*args)
}
