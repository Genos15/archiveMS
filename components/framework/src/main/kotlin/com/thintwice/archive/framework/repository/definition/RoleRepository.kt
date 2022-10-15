package com.thintwice.archive.framework.repository.definition

import com.thintwice.archive.model.domain.Permission
import com.thintwice.archive.model.domain.Role

interface RoleRepository {
    suspend fun createOrEdit(): Role
    suspend fun delete(): Boolean
    suspend fun find(): Role
    suspend fun retrieve(): List<Role>
    suspend fun search(): List<Role>
    suspend fun permissions(): List<Permission>
}