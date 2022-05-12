package com.thintwice.archive.config

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.config.server.EnableConfigServer

@SpringBootApplication
@EnableConfigServer
class ConfigureApplication

fun main(args: Array<String>) {
    runApplication<ConfigureApplication>(*args)
}
