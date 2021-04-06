package io.github.rorione.notificator.service

import io.github.rorione.notificator.model.TemplateInfo
import io.github.rorione.notificator.factory.RequestFactory
import io.github.rorione.notificator.factory.TemplateFactory
import io.github.rorione.notificator.model.RequestInfo
import io.github.rorione.notificator.model.Template
import io.github.rorione.notificator.repository.TemplateRepository
import io.github.rorione.notificator.utils.RequestSenderUtils
import org.springframework.stereotype.Service

@Service
class TemplateService(
    val templateRepository: TemplateRepository,
    val templateFactory: TemplateFactory,
    val requestFactory: RequestFactory,
    val senderUtils: RequestSenderUtils
) {

    fun addTemplate(templateInfo: TemplateInfo) : Template {
        val template = templateFactory.createTemplate(templateInfo)
        return templateRepository.save(template)
    }

    fun sendTemplate(requestInfo: RequestInfo): Map<String, String> {
        val template = templateRepository.getOne(requestInfo.getTemplateId())
        val request = requestFactory.createRequest(template, requestInfo)

        return senderUtils.sendRequest(request)
    }
}
