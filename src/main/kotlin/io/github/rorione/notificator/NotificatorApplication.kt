package io.github.rorione.notificator

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class NotificatorApplication

fun main(args: Array<String>) {
    runApplication<NotificatorApplication>(*args)
}
