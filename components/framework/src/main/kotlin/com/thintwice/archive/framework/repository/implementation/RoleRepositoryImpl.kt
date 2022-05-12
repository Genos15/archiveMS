package com.thintwice.archive.framework.repository.implementation

import com.thintwice.archive.framework.repository.definition.RoleRepository
import com.thintwice.archive.model.domain.Permission
import com.thintwice.archive.model.domain.Role
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties("service")
class RoleRepositoryImpl : RoleRepository {
    override suspend fun createOrEdit(): Role {
        TODO("Not yet implemented")
    }

    override suspend fun delete(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun find(): Role {
        TODO("Not yet implemented")
    }

    override suspend fun retrieve(): List<Role> = emptyList()

    override suspend fun search(): List<Role> = emptyList()

    override suspend fun permissions(): List<Permission> = emptyList()
}