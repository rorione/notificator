package io.github.rorione.notificator.model

import io.github.rorione.notificator.utils.RequestSenderUtils

interface RequestInfo {
    fun getTemplateId() : String
    fun getVariables() : Map<String, String>
    fun getRequestType() : RequestSenderUtils.RequestType?
}
