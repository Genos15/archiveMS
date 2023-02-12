package com.thintwice.archive.mbompay.controller

import com.stripe.exception.SignatureVerificationException
import com.stripe.model.*
import com.stripe.net.Webhook
import com.thintwice.archive.mbompay.configuration.bundle.RB
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/v0.1")
class StripeApiController(private val sr: RB) {

    @RequestMapping(value = ["/stripe/webhook"], method = [RequestMethod.POST])
    fun webhook(
        @RequestBody payload: String?,
        @RequestHeader("Stripe-Signature") sigHeader: String?,
    ): ResponseEntity<String?>? {
        println("-- new stripe event $payload, $sigHeader")
        val secretWebhookKey = sr.l("stripe.secret.webhook.key")
        val event: Event? = try {
            Webhook.constructEvent(payload, sigHeader, secretWebhookKey)
        } catch (e: SignatureVerificationException) {
//            println("Failed signature verification", e)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body<String?>(null)
        }
        val dataObjectDeserializer = event?.dataObjectDeserializer
        val stripeObject: StripeObject?
        if (dataObjectDeserializer?.getObject()?.isPresent == true) {
            stripeObject = dataObjectDeserializer.getObject()?.get()
            println(stripeObject)
//            logger.debug (stripeObject.toString())
        } else {
            // Deserialization failed, probably due to an API version mismatch.
            // Refer to the Javadoc documentation on `EventDataObjectDeserializer` for
            // instructions on how to handle this case, or return an error here.
        }
        when (event?.type) {
            sr.l("stripe.event.payment_intent.succeeded") -> {
                println("payment succeed…")
            }

            sr.l("stripe.event.payment_intent.processing") -> {
                println("payment succeed…")
            }

            else -> return ResponseEntity.status(HttpStatus.BAD_REQUEST).body<String?>(null)
        }
        return ResponseEntity("Success", HttpStatus.OK)
    }

}

