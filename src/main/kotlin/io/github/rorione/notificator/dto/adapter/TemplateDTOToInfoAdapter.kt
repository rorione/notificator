package io.github.rorione.notificator.dto.adapter

import io.github.rorione.notificator.dto.TemplateDTO
import io.github.rorione.notificator.model.TemplateInfo

class TemplateDTOToInfoAdapter(private val templateDTO: TemplateDTO) : TemplateInfo {
    override fun getId(): String {
        return templateDTO.templateId
    }

    override fun getTemplate(): String {
        return templateDTO.template
    }

    override fun getRecipients(): List<String> {
        return templateDTO.recipients
    }

}
