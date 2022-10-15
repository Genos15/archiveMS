package com.thintwice.archive.file.services

import com.thintwice.archive.file.repository.FileRepository
import com.thintwice.archive.file.repository.TokenRepository
import kotlinx.coroutines.withTimeout
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.InputStream
import java.util.*
import javax.security.sasl.AuthenticationException

@Service
class FileService(private val tokenService: TokenRepository, private val fileService: FileRepository) {

    suspend fun edit(part: MultipartFile, token: UUID): Optional<UUID> = withTimeout(timeMillis = 30000L) {
        return@withTimeout if (tokenService.isActive(token = token)) {
            fileService.edit(part = part)
        } else {
            throw AuthenticationException("the current token is not valid")
        }
    }

    suspend fun delete(id: UUID, token: UUID): Optional<Boolean> = withTimeout(timeMillis = 5000L) {
        return@withTimeout if (tokenService.isActive(token = token)) {
            fileService.delete(id = id)
        } else {
            throw AuthenticationException("the current token is not valid")
        }
    }

    suspend fun find(id: UUID, token: UUID): Optional<InputStream> = withTimeout(timeMillis = 30000L) {
        return@withTimeout if (tokenService.isActive(token = token)) {
            fileService.find(id = id)
        } else {
            throw AuthenticationException("the current token is not valid")
        }
    }
}
