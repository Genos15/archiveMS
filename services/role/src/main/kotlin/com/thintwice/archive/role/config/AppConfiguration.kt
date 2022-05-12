package com.thintwice.archive.role.config

import com.thintwice.archive.role.handler.RoleHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.springframework.web.reactive.function.server.coRouter

@Configuration
@EnableR2dbcRepositories
class AppConfiguration {

    @Bean
    fun userRoute(handler: RoleHandler) = coRouter {
        GET("/roles", handler::retrieve)
//        GET("/users/search", userHandler::search)
//        GET("/users/{id}", userHandler::findUser)
//        POST("/users", userHandler::addUser)
//        PUT("/users/{id}", userHandler::updateUser)
//        DELETE("/users/{id}", userHandler::deleteUser)
    }
}