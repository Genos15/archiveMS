package com.thintwice.archive.mbompay

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
@RestController
class MbompayMicroserviceApplication

fun main(args: Array<String>) {
    runApplication<MbompayMicroserviceApplication>(*args)
}
