package com.thintwice.archive.config.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity

import org.springframework.security.web.server.SecurityWebFilterChain


@Configuration
@EnableWebFluxSecurity
class SecurityConfiguration {
    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain? {
        // @formatter:off
        return http
            .csrf().disable()
            .cors().disable()
            .authorizeExchange()
            .anyExchange().authenticated()
            .and()
            .httpBasic()
            .and()
            .build()
        // @formatter:on
    }
}