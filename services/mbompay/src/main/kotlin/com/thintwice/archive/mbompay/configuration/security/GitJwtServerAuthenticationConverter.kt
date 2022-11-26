package com.thintwice.archive.mbompay.configuration.security

import com.thintwice.archive.mbompay.domain.common.GraphqlRequestResolvableType
import org.springframework.core.ResolvableType
import org.springframework.core.ResolvableType.forClass
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono


@Component
class GitJwtServerAuthenticationConverter(private val service: ReactiveUserDetailsService) :
    ServerAuthenticationConverter {
    override fun convert(exchange: ServerWebExchange): Mono<Authentication> {
        return exchange.request.body.map { body ->
            val decoder = Jackson2JsonDecoder()
            val elementType: ResolvableType = forClass(GraphqlRequestResolvableType::class.java)
            return@map decoder.decodeToMono(
                Mono.just(body), elementType, MediaType.APPLICATION_GRAPHQL, emptyMap()
            ).cast(GraphqlRequestResolvableType::class.java)
        }.next()
            .flatMap { it }
            .flatMap { requestBody ->
                println("""
                    excluded: $EXCLUDED_PATHS
                    operationName: ${requestBody.operationName}
                """.trimIndent())
                return@flatMap if (EXCLUDED_PATHS.contains(requestBody.operationName)) {
                    Mono.just(
                        AnonymousAuthenticationToken(
                            "anonymous",
                            "anonymous",
                            AuthorityUtils.createAuthorityList("ANONYMOUS")
                        )
                    )
                } else {
                    Mono.justOrEmpty(exchange.request.headers.getFirst(HttpHeaders.AUTHORIZATION))
                        .filter { header ->
                            header.trim { it <= ' ' }
                                .isNotEmpty() && header.trim { it <= ' ' }
                                .startsWith(AUTH_HEADER_VALUE_PREFIX)
                        }
                        .map { header ->
                            header.substring(AUTH_HEADER_VALUE_PREFIX.length)
                        }
                        .flatMap(service::findByUsername)
                        .map { user ->


                            println("GitJwtServerAuthenticationConverter -> $user")
                            UsernamePasswordAuthenticationToken(
                                user, user.password
                            )
                        }
                }
            }
//            .flatMap {
//                Mono.justOrEmpty(it.request.headers.getFirst(HttpHeaders.AUTHORIZATION))
//            }
//            .filter { header ->
//                header.trim { it <= ' ' }
//                    .isNotEmpty() && header.trim { it <= ' ' }
//                    .startsWith(AUTH_HEADER_VALUE_PREFIX)
//            }
//            .map { header ->
//                header.substring(AUTH_HEADER_VALUE_PREFIX.length)
//            }
//            .flatMap(service::findByUsername)
//            .map { user ->
//
//
//                println("GitJwtServerAuthenticationConverter -> $user")
//                UsernamePasswordAuthenticationToken(
//                    user, user.password
//                )
//            }


//        return Mono.justOrEmpty(exchange)
//            .flatMap { serverWebExchange: ServerWebExchange ->
//
////                serverWebExchange.
//                println(
//                    """
//                    ${serverWebExchange.attributes.values}
//                    ==============================
//                    ${serverWebExchange.request.uri}
//                    ==============================
//
//                """.trimIndent()
//                )
//
//                Mono.justOrEmpty(
//                    serverWebExchange
//                        .request
//                        .headers
//                        .getFirst(HttpHeaders.AUTHORIZATION)
//                )
//            }
//            .filter { header: String ->
//                header.trim { it <= ' ' }.isNotEmpty() && header.trim { it <= ' ' }
//                    .startsWith(AUTH_HEADER_VALUE_PREFIX)
//            }
//            .map { header -> header.substring(AUTH_HEADER_VALUE_PREFIX.length) }
//            .flatMap(service::findByUsername)
//            .map { user ->
//                println("GitJwtServerAuthenticationConverter -> $user")
//                UsernamePasswordAuthenticationToken(
//                    user,
//                    user.password
//                )
//            }
    }

    companion object {
        private const val AUTH_HEADER_VALUE_PREFIX = "Bearer "
        private const val INTROSPECTION_QUERY_NAME = "IntrospectionQuery"
        private val EXCLUDED_PATHS = arrayOf(INTROSPECTION_QUERY_NAME)
    }
}