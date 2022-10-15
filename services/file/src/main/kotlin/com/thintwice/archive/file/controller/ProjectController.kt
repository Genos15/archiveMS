package com.thintwice.archive.file.controller

import com.thintwice.archive.file.configurations.toUuid
import com.thintwice.archive.file.configurations.token.TokenAnalyzer
import com.thintwice.archive.file.services.FileService
import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("api/")
@CrossOrigin(origins = ["*"], allowedHeaders = ["*"])
class ProjectController(private val handler: FileService, private val d: TokenAnalyzer) {

    @PostMapping(value = ["/upload"])
    suspend fun upload(
        @RequestParam("file") part: MultipartFile,
        @RequestHeader(HttpHeaders.AUTHORIZATION) bearer: String,
    ) = handler.edit(part = part, token = d(bearer))

    @GetMapping(value = ["/find/{id}"])
    suspend fun find(@PathVariable id: String, @RequestHeader(HttpHeaders.AUTHORIZATION) bearer: String) =
        handler.find(id = id.toUuid(), token = d(bearer))

    @GetMapping(value = ["/delete/{id}"])
    suspend fun delete(@PathVariable id: String, @RequestHeader(HttpHeaders.AUTHORIZATION) bearer: String) =
        handler.delete(id = id.toUuid(), token = d(bearer))

}