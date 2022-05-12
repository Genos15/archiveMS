package com.thintwice.archive.user.configurations.object_mapping

import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.thintwice.archive.user.configurations.serialization.DateTimeDeserializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import java.time.OffsetDateTime


@Configuration
class Mapper {

    @Primary
    @Bean("JsonMapperKt")
    fun jsonMapperKt(): ObjectMapper {
        val simpleModule = SimpleModule()
        simpleModule.addDeserializer(OffsetDateTime::class.java, DateTimeDeserializer)
        return ObjectMapper().registerModule(KotlinModule.Builder()
            .withReflectionCacheSize(512)
            .configure(KotlinFeature.NullToEmptyCollection, false)
            .configure(KotlinFeature.NullToEmptyMap, false)
            .configure(KotlinFeature.NullIsSameAsDefault, false)
            .configure(KotlinFeature.SingletonSupport, false)
            .configure(KotlinFeature.StrictNullChecks, false)
            .build())
            .registerModule(JavaTimeModule())
            .registerModule(simpleModule)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    }
}