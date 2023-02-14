package com.thintwice.archive.mbompay.controller

import com.stripe.exception.SignatureVerificationException
import com.stripe.model.*
import com.stripe.net.Webhook
import com.thintwice.archive.mbompay.configuration.bundle.RB
import com.thintwice.archive.mbompay.repository.CardRepository
import com.thintwice.archive.mbompay.repository.CustomerRepository
import com.thintwice.archive.mbompay.repository.StripePaymentRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/v0.1")
class StripeApiController(
    private val sr: RB,
    private val cardService: CardRepository,
    private val customerService: CustomerRepository,
    private val paymentService: StripePaymentRepository,
) {

    @RequestMapping(value = ["/stripe/webhook"], method = [RequestMethod.POST])
    suspend fun webhook(
        @RequestBody payload: String?,
        @RequestHeader("Stripe-Signature") sigHeader: String?,
    ): ResponseEntity<String>? {
        val eventResult = runCatching {
            val secretWebhookKey = sr.l("stripe.secret.webhook.key")

            val event = Webhook.constructEvent(payload, sigHeader, secretWebhookKey)
            val dataObjectDeserializer = event.dataObjectDeserializer
//            val stripeObject = dataObjectDeserializer.`object`.get()
            val stripeObject = dataObjectDeserializer.deserializeUnsafe()

            when (event.type) {
                sr.l("stripe.event.payment_intent.created") -> {
                    paymentService.onCreate(stripeObject as PaymentIntent)
                }

                sr.l("stripe.event.payment_intent.succeeded") -> {
                    paymentService.onSucceed(stripeObject as PaymentIntent)
                }

                sr.l("stripe.event.payment_intent.requires_action") -> {
                    paymentService.onRequiredAction(stripeObject as PaymentIntent)
                }

                sr.l("stripe.event.payment_intent.processing") -> {
                    paymentService.onProcessing(stripeObject as PaymentIntent)
                }

                sr.l("stripe.event.payment_intent.payment_failed") -> {
                    paymentService.onFailed(stripeObject as PaymentIntent)
                }

                sr.l("stripe.event.payment_intent.canceled") -> {
                    paymentService.onCancelled(stripeObject as PaymentIntent)
                }

                sr.l("stripe.event.charge.succeeded") -> {
                    println("stripe payment + customer charge succeeded")
                }

                sr.l("stripe.event.customer.source.deleted") -> {
                    println("stripe delete customer card")
                    cardService.stripeEventDelete(stripeObject as Card)
                }

                sr.l("stripe.event.customer.source.created") -> {
                    println("stripe create customer card")
                    cardService.stripeEventCreate(stripeObject as Card)
                }

                sr.l("stripe.event.customer.updated") -> {
                    println("stripe customer update")
                    val customer = stripeObject as Customer
                    customerService.stripeEventUpdate(customer = customer)
                }

                else -> throw RuntimeException("Unhandled event ${event.type}")
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

