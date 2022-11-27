package com.thintwice.archive.mbompay.notification

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import java.util.*
import javax.mail.MessagingException


@Service
class EmailService(
    private val mailSender: JavaMailSender,
    @Qualifier("emailTemplateEngine") private val htmlTemplateEngine: TemplateEngine,
) {
    private val EMAIL_SIMPLE_TEMPLATE_NAME = "html/email-simple"
    private val USERNAME = "hello@jesend.app"
    private val DEFAULT_LOCALE = Locale.ENGLISH

    @Throws(MessagingException::class)
    suspend fun sentMailOtp(
        recipientName: String? = null,
        recipientEmail: String,
        locale: Locale?,
        code: String,
    ) {

        // Prepare the evaluation context
        val ctx = Context(locale ?: DEFAULT_LOCALE)
        ctx.setVariable("name", recipientName ?: "")
        ctx.setVariable("code", code)

        // Prepare message using a Spring helper
        val mimeMessage = mailSender.createMimeMessage()
        val message = MimeMessageHelper(mimeMessage, "UTF-8")
        message.setSubject("Welcome back to Jesend")
        message.setFrom(USERNAME)
        message.setTo(recipientEmail)

        // Create the HTML body using Thymeleaf
        val htmlContent = htmlTemplateEngine.process(EMAIL_SIMPLE_TEMPLATE_NAME, ctx)
        message.setText(htmlContent, true /* isHtml */)

        // Send email
        mailSender.send(mimeMessage)
    }
}