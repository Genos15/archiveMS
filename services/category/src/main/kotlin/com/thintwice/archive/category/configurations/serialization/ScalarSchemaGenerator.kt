package com.thintwice.archive.category.configurations.serialization

import com.expediagroup.graphql.generator.hooks.SchemaGeneratorHooks
import graphql.kickstart.servlet.apollo.ApolloScalars
import graphql.scalars.ExtendedScalars
import graphql.schema.GraphQLType
import org.springframework.context.annotation.Configuration
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.time.OffsetTime
import javax.servlet.http.Part
import kotlin.reflect.KClass
import kotlin.reflect.KType

@Configuration
class ScalarSchemaGenerator : SchemaGeneratorHooks {
    override fun willGenerateGraphQLType(type: KType): GraphQLType? = when (type.classifier as? KClass<*>) {
        Any::class -> ExtendedScalars.Json
        Map::class -> ExtendedScalars.Object
        OffsetDateTime::class -> ExtendedScalars.DateTime
        OffsetTime::class -> ExtendedScalars.Time
        BigDecimal::class -> ExtendedScalars.GraphQLBigDecimal
        Long::class -> ExtendedScalars.GraphQLLong
        Part::class -> ApolloScalars.Upload
        else -> null
    }
}

