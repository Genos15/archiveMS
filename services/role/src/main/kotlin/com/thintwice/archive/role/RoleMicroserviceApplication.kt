package com.thintwice.archive.role

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
@RestController
class RoleMicroserviceApplication

fun main(args: Array<String>) {
    runApplication<RoleMicroserviceApplication>(*args)
}
