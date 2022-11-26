package com.thintwice.archive.mbompay.configuration.security

import org.springframework.context.annotation.AdviceMode
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers


@EnableWebFluxSecurity
@Configuration
@EnableReactiveMethodSecurity(mode = AdviceMode.PROXY)
class SecurityConfig(
//    private val serverAuthenticationFailureHandler: ServerAuthenticationFailureHandler,
//    private val serverAuthenticationSuccessHandler: ServerAuthenticationSuccessHandler,
    private val reactiveUserDetailsService: ReactiveUserDetailsService,
    private val passwordEncoder: PasswordEncoder,
) {

    @Bean
    fun configureSecurity(
        http: ServerHttpSecurity,
        jwtAuthenticationFilter: AuthenticationWebFilter,
//        jwtService: JWTService,
    ): SecurityWebFilterChain {
        return http
            .csrf { csrf -> csrf.disable() }
            .authorizeExchange {
                it.anyExchange().permitAll()
            }
//            .httpBasic(Customizer.withDefaults())
            .addFilterAt(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
            .build()
//        return http
//            .csrf()
//            .disable()
//
////            .authenticationSuccessHandler(serverAuthenticationSuccessHandler)
////            .authenticationFailureHandler(serverAuthenticationFailureHandler)
////            .and()
//            .authorizeExchange()
////            .pathMatchers("/graphql").authenticated()
////            .pathMatchers(*EXCLUDED_PATHS).permitAll()
////            .pathMatchers("/admin/**").hasRole("ADMIN")
//            .pathMatchers("/**").permitAll().anyExchange().authenticated()
//            .and()
//            .addFilterAt(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
//            .addFilterAt(JWTReactiveAuthorizationFilter(jwtService), SecurityWebFiltersOrder.AUTHORIZATION)
//            .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
//            .build()
    }

    @Bean
    fun authenticationWebFilter(
        reactiveAuthenticationManager: ReactiveAuthenticationManager,
        jwtConverter: ServerAuthenticationConverter,
//        serverAuthenticationSuccessHandler: ServerAuthenticationSuccessHandler,
//        jwtServerAuthenticationFailureHandler: ServerAuthenticationFailureHandler,
    ): AuthenticationWebFilter {
        val authenticationWebFilter = AuthenticationWebFilter(reactiveAuthenticationManager)
        authenticationWebFilter.apply {
            setRequiresAuthenticationMatcher {
                ServerWebExchangeMatchers.pathMatchers(
                    HttpMethod.POST, "/graphql"
                ).matches(it)
            }
            setServerAuthenticationConverter(jwtConverter)
//            setAuthenticationSuccessHandler(serverAuthenticationSuccessHandler)
//            setAuthenticationFailureHandler(jwtServerAuthenticationFailureHandler)
            setSecurityContextRepository(NoOpServerSecurityContextRepository.getInstance())
        }
        return authenticationWebFilter
    }

    @Bean
    fun reactiveAuthenticationManager(): ReactiveAuthenticationManager {
        println("reactiveAuthenticationManager -> 1")
        val manager = UserDetailsRepositoryReactiveAuthenticationManager(reactiveUserDetailsService)
        println("reactiveAuthenticationManager -> 2")
        manager.setPasswordEncoder(passwordEncoder)
        println("reactiveAuthenticationManager -> 3")
        return manager
    }

}