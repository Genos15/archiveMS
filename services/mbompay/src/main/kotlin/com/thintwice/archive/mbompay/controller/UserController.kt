package com.thintwice.archive.mbompay.controller

import com.thintwice.archive.mbompay.domain.input.UserDetailInput
import com.thintwice.archive.mbompay.domain.model.UserDetail
import com.thintwice.archive.mbompay.repository.UserRepository
import org.springframework.graphql.data.method.annotation.*
import org.springframework.stereotype.Controller
import java.util.*


@Controller
class UserController(private val service: UserRepository) {

//    @MutationMapping(name = "userDetail")
//    suspend fun userDetail(@Argument input: UserDetailInput, @ContextValue token: UUID): Optional<UserDetail> {
//        return service.userDetail(input = input)
//    }
}