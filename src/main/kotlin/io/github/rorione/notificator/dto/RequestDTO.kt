package io.github.rorione.notificator.dto

import io.github.rorione.notificator.utils.RequestSenderUtils
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

class RequestDTO(
    @field:NotBlank
    @field:Size(max = 100)
    val templateId: String,

    @field:NotNull
    val variables: Map<String, String>,

    val requestType: RequestSenderUtils.RequestType?
)
