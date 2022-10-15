package com.thintwice.archive.mbompay.configuration.scalar

import graphql.scalars.ExtendedScalars
import graphql.schema.idl.RuntimeWiring
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.graphql.execution.RuntimeWiringConfigurer


@Configuration
class GraphQlConfig {
    @Bean
    fun runtimeWiringConfigurerUUID(): RuntimeWiringConfigurer {
        return RuntimeWiringConfigurer { wiringBuilder: RuntimeWiring.Builder -> wiringBuilder.scalar(ExtendedScalars.UUID) }
    }

    @Bean
    fun runtimeWiringConfigurerLong(): RuntimeWiringConfigurer {
        return RuntimeWiringConfigurer { wiringBuilder: RuntimeWiring.Builder -> wiringBuilder.scalar(ExtendedScalars.GraphQLLong) }
    }

    @Bean
    fun runtimeWiringConfigurerDateTime(): RuntimeWiringConfigurer {
        return RuntimeWiringConfigurer { wiringBuilder: RuntimeWiring.Builder -> wiringBuilder.scalar(ExtendedScalars.DateTime) }
    }
}


//Any::class -> ExtendedScalars.Json
//Map::class -> ExtendedScalars.Object
//OffsetDateTime::class -> ExtendedScalars.DateTime
//OffsetTime::class -> ExtendedScalars.Time
//BigDecimal::class -> ExtendedScalars.GraphQLBigDecimal
//Long::class -> ExtendedScalars.GraphQLLong
//Part::class -> ApolloScalars.Upload
//UUID::class -> ExtendedScalars.UUID
//else -> null