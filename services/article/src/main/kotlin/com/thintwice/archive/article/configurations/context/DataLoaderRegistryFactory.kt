package com.thintwice.archive.article.configurations.context

import org.dataloader.*
import org.springframework.stereotype.Component
import java.util.*

@Component
class DataLoaderRegistryFactory( ) {

    companion object {

    }

    fun create(instanceId: UUID): DataLoaderRegistry {
        val registry = DataLoaderRegistry()
        return registry
    }

}

