package io.github.rorione.notificator.factory

import io.github.rorione.notificator.model.Request
import io.github.rorione.notificator.model.RequestBody
import org.springframework.stereotype.Component

@Component
class RequestBodyFactory {
    fun createRequestBody(request: Request): RequestBody {
        return RequestBody(request.message)
    }
}
