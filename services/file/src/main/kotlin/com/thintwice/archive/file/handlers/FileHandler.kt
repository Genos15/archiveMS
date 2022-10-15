package com.thintwice.archive.file.handlers

import com.thintwice.archive.file.configurations.toUuid
import com.thintwice.archive.file.configurations.token.TokenAnalyzer
import com.thintwice.archive.file.services.FileService
import org.springframework.core.io.InputStreamResource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.codec.multipart.Part
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.commons.CommonsMultipartFile
import org.springframework.web.reactive.function.server.*


@Component
class FileHandler(private val service: FileService, private val deduct: TokenAnalyzer) {

    suspend fun edit(request: ServerRequest): ServerResponse {
        val token = deduct(request = request)
        val part: Part? = request.awaitMultipartData().getFirst("file")
        if ((part is MultipartFile).not() && (part is CommonsMultipartFile).not()) {
            throw IllegalArgumentException("impossible to retrieve the image content")
        }
        return ServerResponse.ok().bodyValueAndAwait(service.edit(part = part as CommonsMultipartFile, token = token))
    }

    suspend fun delete(request: ServerRequest): ServerResponse {
        val token = deduct(request = request)
        val id = request.pathVariable("id").toUuid()
        return ServerResponse.ok().bodyValueAndAwait(service.delete(id = id, token = token))
    }

    suspend fun find(request: ServerRequest): ServerResponse {
        val token = deduct(request = request)
        val id = request.pathVariable("id").toUuid()
        val result = service.find(id = id, token = token)
        if (result.isPresent) {
            val outcome = ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(InputStreamResource(result.get()))
            return ServerResponse.ok().bodyValueAndAwait(outcome)
        }
        return ServerResponse.badRequest().buildAndAwait()
    }

}