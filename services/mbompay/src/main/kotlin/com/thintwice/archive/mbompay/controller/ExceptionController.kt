package com.thintwice.archive.mbompay.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler


@ControllerAdvice
class ExceptionController {

    // For UI Pages
    @ExceptionHandler(UsernameNotFoundException::class)
    fun userNotFoundException(ex: UsernameNotFoundException?): String? {
        println("Tour in userNotFoundException")
        return "Error Maybe"
    }

    // For REST APIs
    @ExceptionHandler(IllegalArgumentException::class)
    fun illegalArgumentException(ex: IllegalArgumentException): ResponseEntity<*>? {
        println("Tour in illegalArgumentException")
        return ResponseEntity(ex.message, HttpStatus.BAD_REQUEST)
    }
}