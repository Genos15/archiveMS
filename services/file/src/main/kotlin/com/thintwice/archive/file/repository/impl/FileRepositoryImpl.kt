package com.thintwice.archive.file.repository.impl

import com.mongodb.BasicDBObject
import com.thintwice.archive.file.repository.FileRepository
import kotlinx.coroutines.reactive.awaitFirstOrElse
import kotlinx.coroutines.reactor.mono
import org.springframework.core.io.buffer.DefaultDataBufferFactory
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.gridfs.GridFsCriteria
import org.springframework.data.mongodb.gridfs.ReactiveGridFsOperations
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import reactor.core.publisher.Flux
import java.io.InputStream
import java.util.*

@Component
class FileRepositoryImpl(private val database: ReactiveGridFsOperations) : FileRepository {

    override suspend fun edit(part: MultipartFile): Optional<UUID> = mono {
        DefaultDataBufferFactory().wrap(part.inputStream.readBytes())
    }.flatMap {
        val id = UUID.randomUUID()
        val meta = BasicDBObject()
        meta["filename"] = part.originalFilename
        meta["hostId"] = id
        database.store(Flux.just(it), "$id", meta).map { Optional.of(id) }
    }.log().awaitFirstOrElse { Optional.empty() }

    override suspend fun delete(id: UUID): Optional<Boolean> =
        database.find(Query.query(GridFsCriteria.whereFilename().`is`("$id")))
            .map { Optional.of(true) }
            .log()
            .awaitFirstOrElse { Optional.empty() }

    override suspend fun find(id: UUID): Optional<InputStream> =
        database.findOne(Query.query(GridFsCriteria.whereFilename().regex("$id")))
            .flatMap { database.getResource(it) }
            .filter { it.exists() }
            .flatMap { it.inputStream }
            .map { Optional.ofNullable(it) }
            .log()
            .awaitFirstOrElse { Optional.empty() }

}