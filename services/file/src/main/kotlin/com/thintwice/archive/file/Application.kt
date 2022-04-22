package com.thintwice.archive.file

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FileMicroserviceApplication

fun main(args: Array<String>) {
    runApplication<FileMicroserviceApplication>(*args)
}
