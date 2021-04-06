package io.github.rorione.notificator.dto

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

class TemplateDTO(

    @field:NotBlank
    @field:Size(max = 100)
    val templateId: String,

    @field:NotBlank
    val template: String,

    val recipients: List<String>
)
