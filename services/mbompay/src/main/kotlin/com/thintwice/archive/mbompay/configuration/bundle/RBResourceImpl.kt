package com.thintwice.archive.mbompay.configuration.bundle

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment

@Configuration
@PropertySource(value = [
    "classpath:database.query.properties",
    "classpath:stripe.dev.properties",
    "classpath:jesend.dev.properties"
])
class RBResourceImpl(private val environment: Environment) : RB {
    override fun l(resource: String): String {
        return environment.getRequiredProperty(resource)
    }
}