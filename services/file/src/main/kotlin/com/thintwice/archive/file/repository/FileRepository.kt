package com.thintwice.archive.file.repository

import org.springframework.web.multipart.MultipartFile
import java.io.InputStream
import java.util.*

interface FileRepository {
    suspend fun edit(part: MultipartFile): Optional<UUID>
    suspend fun delete(id: UUID): Optional<Boolean>
    suspend fun find(id: UUID): Optional<InputStream>
}