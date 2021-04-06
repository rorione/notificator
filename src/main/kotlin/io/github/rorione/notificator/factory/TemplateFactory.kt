package io.github.rorione.notificator.factory

import io.github.rorione.notificator.model.Template
import io.github.rorione.notificator.model.TemplateInfo
import org.springframework.stereotype.Component

@Component
class TemplateFactory {
    fun createTemplate(templateInfo: TemplateInfo) : Template {
        return Template(
            templateInfo.getId(),
            templateInfo.getTemplate(),
            templateInfo.getRecipients()
        )
    }
}
