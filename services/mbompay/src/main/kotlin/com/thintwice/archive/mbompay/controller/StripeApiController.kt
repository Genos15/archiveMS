package com.thintwice.archive.mbompay.controller

import com.stripe.exception.SignatureVerificationException
import com.stripe.model.*
import com.stripe.net.Webhook
import com.thintwice.archive.mbompay.configuration.bundle.RB
import com.thintwice.archive.mbompay.repository.StripeChargeRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/v0.1")
class StripeApiController(private val sr: RB, private val service: StripeChargeRepository) {

    @RequestMapping(value = ["/stripe/webhook"], method = [RequestMethod.POST])
    suspend fun webhook(
        @RequestBody payload: String?,
        @RequestHeader("Stripe-Signature") sigHeader: String?,
    ): ResponseEntity<String>? {
        val eventResult = runCatching {
            val secretWebhookKey = sr.l("stripe.secret.webhook.key")
            val event = Webhook.constructEvent(payload, sigHeader, secretWebhookKey)
            val dataObjectDeserializer = event.dataObjectDeserializer
            val stripeObject = dataObjectDeserializer.`object`.get()

            when (event.type) {
                sr.l("stripe.event.payment_intent.created") -> {
                    service.createPaymentIntentEventHandler(stripeObject as PaymentIntent)
                }

                sr.l("stripe.event.payment_intent.succeeded") -> {
                    println("payment succeed…")
                }

                sr.l("stripe.event.payment_intent.processing") -> {
                    println("payment succeed…")
                }

                else -> throw RuntimeException("Unknown")
            }
            return ResponseEntity("Success", HttpStatus.OK)
        }.recover {
            println("-- Stripe Event Error = ${it.message}")
            val cause: String
            when (it) {
                is SignatureVerificationException -> {
                    cause = "Event signature not found"
                }

                is NoSuchElementException -> {
                    cause = "Event source object undefined"
                }

                else -> {
                    cause = "Unknown Event"
                    println(it.javaClass)
                }
            }

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(cause)
        }

        return eventResult.getOrNull()
    }

}

