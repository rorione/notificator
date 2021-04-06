package io.github.rorione.notificator.dto.adapter

import io.github.rorione.notificator.dto.RequestDTO
import io.github.rorione.notificator.model.RequestInfo
import io.github.rorione.notificator.utils.RequestSenderUtils

class RequestDTOToInfoAdapter(private val requestDTO: RequestDTO) : RequestInfo {
    override fun getTemplateId(): String {
        return requestDTO.templateId
    }

    override fun getVariables(): Map<String, String> {
        return requestDTO.variables
    }

    override fun getRequestType(): RequestSenderUtils.RequestType? {
        return requestDTO.requestType
    }
}
