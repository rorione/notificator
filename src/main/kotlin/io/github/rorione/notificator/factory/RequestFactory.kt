package io.github.rorione.notificator.factory

import io.github.rorione.notificator.dto.RequestDTO
import io.github.rorione.notificator.model.Request
import io.github.rorione.notificator.model.RequestInfo
import io.github.rorione.notificator.model.Template
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.PropertySource
import org.springframework.stereotype.Component
import org.stringtemplate.v4.ST

@Component
@PropertySource("classpath:template.properties")
class RequestFactory {

    @Value("\${template.substitutionBegin}")
    private var beginSymbol: Char = '$'

    @Value("\${template.substitutionEnd}")
    private var endSymbol: Char = '$'

    fun createRequest(template: Template, requestInfo: RequestInfo): Request {
        val stringTemplate = ST(template.template, beginSymbol, endSymbol)
        requestInfo.getVariables().forEach { entry -> stringTemplate.add(entry.key, entry.value) }
        return Request(stringTemplate.render(), template.recipients, requestInfo.getRequestType())
    }
}
