package io.github.rorione.notificator.utils

import io.github.rorione.notificator.factory.RequestBodyFactory
import io.github.rorione.notificator.model.Request
import io.github.rorione.notificator.model.RequestBody
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class RequestSenderUtils(
    val requestBodyFactory: RequestBodyFactory
) {

    enum class RequestType {
        POST_REQUEST
    }

    fun sendRequest(request: Request): Map<String, String> {
        return when (request.requestType) {
            RequestType.POST_REQUEST -> sendPostRequest(request)
            else -> sendPostRequest(request)
        }
    }

    private fun sendPostRequest(request: Request): Map<String, String> {
        val rest = RestTemplate()
        val requestHeaders = HttpHeaders()
        val requestBody = requestBodyFactory.createRequestBody(request)
        val requestEntity = HttpEntity<RequestBody>(requestBody, requestHeaders)
        val responses = mutableMapOf<String, String>()

        requestHeaders.add("Content-Type", "application/json")

        for (recipient in request.recipients) {
            val response = rest.exchange(
                recipient,
                HttpMethod.POST,
                requestEntity,
                String::class.java
            )
            responses[recipient] = response.body ?: "${response.statusCodeValue}"
        }

        return responses
    }
}
