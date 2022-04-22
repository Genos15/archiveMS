package com.thintwice.archive.user

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
@RestController
class UserMicroserviceApplication

fun main(args: Array<String>) {
    runApplication<UserMicroserviceApplication>(*args)
}
