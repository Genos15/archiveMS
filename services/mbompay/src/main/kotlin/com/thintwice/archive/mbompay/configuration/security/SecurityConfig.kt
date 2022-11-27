package com.thintwice.archive.mbompay.configuration.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers


@EnableWebFluxSecurity
@Configuration
@EnableReactiveMethodSecurity
//@EnableGlobalMethodSecurity(prePostEnabled=true)
class SecurityConfig {

    @Bean
    fun configureSecurity(
        http: ServerHttpSecurity,
//        jwtAuthenticationFilter: AuthenticationWebFilter,
    ): SecurityWebFilterChain {
        return http
            .csrf { csrf -> csrf.disable() }
            .authorizeExchange {
                it.anyExchange().permitAll()
            }
//            .addFilterAt(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
            .build()
    }

//    @Bean
//    fun authenticationWebFilter(
//        reactiveAuthenticationManager: ReactiveAuthenticationManager,
//        jwtConverter: ServerAuthenticationConverter,
//    ): AuthenticationWebFilter {
//        val authenticationWebFilter = AuthenticationWebFilter(reactiveAuthenticationManager)
//        authenticationWebFilter.apply {
//            setRequiresAuthenticationMatcher {
//                ServerWebExchangeMatchers.pathMatchers(
//                    HttpMethod.POST, "/graphql"
//                ).matches(it)
//            }
//            setServerAuthenticationConverter(jwtConverter)
//            setSecurityContextRepository(NoOpServerSecurityContextRepository.getInstance())
//        }
//        return authenticationWebFilter
//    }
}