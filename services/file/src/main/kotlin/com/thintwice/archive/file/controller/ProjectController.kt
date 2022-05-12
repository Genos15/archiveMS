package com.thintwice.archive.file.controller

import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("files/api/v1")
@CrossOrigin(origins = ["*"], allowedHeaders = ["*"])
class ProjectController {

    @GetMapping(path = ["/name"])
    fun projectName() = "File Microservice"
}