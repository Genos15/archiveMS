package com.thintwice.archive.user.configurations.context

import graphql.kickstart.servlet.context.GraphQLServletContext
import org.dataloader.DataLoaderRegistry
import java.util.*
import javax.security.auth.Subject
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.Part

class CustomGraphQLContext(
    val user_id: String?,
    private val context: GraphQLServletContext
) : GraphQLServletContext {

    override fun getSubject(): Optional<Subject> = context.subject

    override fun getDataLoaderRegistry(): DataLoaderRegistry = context.dataLoaderRegistry

    override fun getFileParts(): MutableList<Part> = context.fileParts

    override fun getParts(): MutableMap<String, MutableList<Part>> = context.parts

    override fun getHttpServletRequest(): HttpServletRequest = context.httpServletRequest

    override fun getHttpServletResponse(): HttpServletResponse = context.httpServletResponse
}