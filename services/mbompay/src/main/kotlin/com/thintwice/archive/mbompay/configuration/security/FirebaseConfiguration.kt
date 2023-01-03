package com.thintwice.archive.mbompay.configuration.security

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource

@Configuration
class FirebaseConfiguration {

    @Bean
    fun firebaseAuth(firebaseApp: FirebaseApp?): FirebaseAuth? {
        return FirebaseAuth.getInstance(firebaseApp)
    }

    @Bean
    fun firebaseApp(credentials: GoogleCredentials): FirebaseApp? {
        val options = FirebaseOptions.builder().setCredentials(credentials).build()
        return FirebaseApp.initializeApp(options)
    }

    @Bean
    fun googleCredentials(): GoogleCredentials? {
        val firebaseOptions = ClassPathResource("firebase/jesend-admin.json")
        return GoogleCredentials.fromStream(firebaseOptions.inputStream)
    }
}