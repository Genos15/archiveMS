package com.thintwice.archive.article

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
@RestController
class ArticleMicroserviceApplication

fun main(args: Array<String>) {
    runApplication<ArticleMicroserviceApplication>(*args)
}
