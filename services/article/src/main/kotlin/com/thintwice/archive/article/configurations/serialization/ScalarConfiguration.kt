package com.thintwice.archive.article.configurations.serialization

import graphql.kickstart.servlet.apollo.ApolloScalars
import graphql.scalars.ExtendedScalars
import graphql.schema.GraphQLScalarType
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ScalarConfiguration {

    @Bean
    fun jsonType(): GraphQLScalarType = ExtendedScalars.Json

    @Bean
    fun longType(): GraphQLScalarType = ExtendedScalars.GraphQLLong

    @Bean
    fun timestampType(): GraphQLScalarType = ExtendedScalars.DateTime

    @Bean
    fun timeType(): GraphQLScalarType = ExtendedScalars.Time

    @Bean
    fun objectType(): GraphQLScalarType = ExtendedScalars.Object

    @Bean
    fun bigDecimal(): GraphQLScalarType = ExtendedScalars.GraphQLBigDecimal

    @Bean
    fun uploadScalar(): GraphQLScalarType = ApolloScalars.Upload
}