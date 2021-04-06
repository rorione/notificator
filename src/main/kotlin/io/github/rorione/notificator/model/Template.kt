package io.github.rorione.notificator.model

import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
class Template(
    @Id
    @NotBlank
    var templateId: String,

    @NotBlank
    var template: String,

    @ElementCollection
    var recipients: List<String>
)
