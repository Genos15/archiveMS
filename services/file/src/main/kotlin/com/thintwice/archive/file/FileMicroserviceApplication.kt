package com.thintwice.archive.file

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
@RestController
class FileMicroserviceApplication

fun main(args: Array<String>) {
    runApplication<FileMicroserviceApplication>(*args)
}
