package com.thintwice.archive.article.configurations.token

import com.thintwice.archive.article.configurations.context.CustomGraphQLContext
import com.thintwice.archive.article.configurations.exceptions.CustomGraphQLError
import graphql.schema.DataFetchingEnvironment
import org.springframework.stereotype.Component
import java.lang.Double.parseDouble
import kotlin.jvm.Throws

@Component
class GeoLocationAnalyzerImpl : GeoLocationAnalyzer {

    @Throws(CustomGraphQLError::class)
    override fun invoke(environment: DataFetchingEnvironment): GeoLocationData {
        val context: CustomGraphQLContext = environment.getContext()
        val request = context.httpServletRequest
        val xLong = request.getHeader("X-long") ?: ""
        val xLat = request.getHeader("X-lat") ?: ""
        if (xLong.isEmpty() || xLat.isEmpty()) {
            throw CustomGraphQLError("Geolocation data not found")
        } else if (parseDouble(xLong) == 0.0 && parseDouble(xLat) == 0.0) {
            throw CustomGraphQLError("Incorrect geolocation data")
        }
        return GeoLocationData(longitude = parseDouble(xLong).toFloat(), latitude = parseDouble(xLat).toFloat())
    }

}