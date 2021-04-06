package io.github.rorione.notificator.model

import io.github.rorione.notificator.utils.RequestSenderUtils
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class Request(
    @NotBlank
    val message: String,
    @NotNull
    val recipients: List<String>,
    val requestType: RequestSenderUtils.RequestType?
)
