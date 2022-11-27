//package com.thintwice.archive.mbompay.controller
//
//import com.thintwice.archive.mbompay.service.AuthRepositoryImpl
//import graphql.ExecutionInput
//import graphql.execution.instrumentation.SimpleInstrumentation
//import graphql.execution.instrumentation.parameters.InstrumentationExecutionParameters
//import org.junit.jupiter.api.Test
//import org.mockito.Mock
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest
//import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester
//import org.springframework.context.annotation.Import
//import org.springframework.graphql.test.tester.GraphQlTester
//import org.springframework.r2dbc.core.DatabaseClient
//import java.util.*
//
//
//@Import(value = [AuthRepositoryImpl::class, AuthControllerIntegrationTest.MyInstrumentation::class])
//@GraphQlTest(value = [AuthController::class])
////@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
////@DataR2dbcTest
////@TestPropertySource(locations = ["classpath:application-test.yml"])
////@RunWith(value = SpringRunner::class)
//@AutoConfigureHttpGraphQlTester
//internal class AuthControllerIntegrationTest {
//
//    @Mock
//    private lateinit var databaseClient: DatabaseClient
//
//    @Autowired
//    private lateinit var graphQlTester: GraphQlTester
//
////    @BeforeEach
////    fun setUp() {
////    }
////
////    @AfterEach
////    fun tearDown() {
////    }
//
//    @Test
//    fun `given nothing, when login, then return SessionToken instance `() {
//        // language=GraphQL
//        val document = """
//            mutation LOGIN{
//                login {
//                    id
//                    accessToken
//                    refreshToken
//                    expiredIn
//                }
//            }
//        """.trimIndent()
//
//        val response = graphQlTester.document(document).execute().path("login").entity(Optional::class.java)
//
//        // When
////        val response =
////            graphQlTester.document(document)
////                .executeSubscription()
////                .toFlux("user", User::class.java)
////
////        // Then
////        StepVerifier.create(response)
////            .expectNext(null)
////            .verifyComplete()
//
//    }
//
//    //    @Test
//    fun refresh() {
//    }
//
//    internal class MyInstrumentation : SimpleInstrumentation() {
//        override fun instrumentExecutionInput(
//            input: ExecutionInput,
//            params: InstrumentationExecutionParameters?,
//        ): ExecutionInput {
//            input.graphQLContext.put("key", "value")
//            return input
//        }
//    }
//
////    companion object {
////        @JvmStatic
////        @BeforeClass
////        fun initDataBase(): Unit {
////        }
////    }
//}