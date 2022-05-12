package com.thintwice.archive.auth

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
@RestController
class AuthMicroserviceApplication

fun main(args: Array<String>) {
    runApplication<AuthMicroserviceApplication>(*args)
}
