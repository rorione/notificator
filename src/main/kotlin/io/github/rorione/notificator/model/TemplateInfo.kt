package io.github.rorione.notificator.model

interface TemplateInfo {
    fun getId(): String

    fun getTemplate(): String

    fun getRecipients(): List<String>
}
