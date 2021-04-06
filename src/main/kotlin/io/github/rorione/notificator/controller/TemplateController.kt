package io.github.rorione.notificator.controller

import io.github.rorione.notificator.dto.RequestDTO
import io.github.rorione.notificator.dto.adapter.RequestDTOToInfoAdapter
import io.github.rorione.notificator.dto.TemplateDTO
import io.github.rorione.notificator.dto.adapter.TemplateDTOToInfoAdapter
import io.github.rorione.notificator.service.TemplateService
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("api/template")
class TemplateController(
    val templateService: TemplateService
) {

    @PostMapping("/add")
    fun addTemplate(@Valid @RequestBody templateDTO: TemplateDTO) {
        val templateInfo = TemplateDTOToInfoAdapter(templateDTO)
        templateService.addTemplate(templateInfo)
    }

    @PostMapping("/execute")
    fun test(@Valid @RequestBody requestDTO: RequestDTO): Map<String, String> {
        val requestInfo = RequestDTOToInfoAdapter(requestDTO)
        return templateService.sendTemplate(requestInfo)
    }
}
