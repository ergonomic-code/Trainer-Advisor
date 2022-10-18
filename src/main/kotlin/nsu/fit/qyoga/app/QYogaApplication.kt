package nsu.fit.qyoga.app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [DataSourceAutoConfiguration::class])
class QYogaApplication

fun main(args: Array<String>) {
    runApplication<QYogaApplication>(*args)
}
