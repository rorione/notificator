package io.github.rorione.notificator

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.rorione.notificator.controller.TemplateController
import io.github.rorione.notificator.dto.RequestDTO
import io.github.rorione.notificator.dto.TemplateDTO
import io.github.rorione.notificator.dto.adapter.RequestDTOToInfoAdapter
import io.github.rorione.notificator.dto.adapter.TemplateDTOToInfoAdapter
import io.github.rorione.notificator.factory.RequestFactory
import io.github.rorione.notificator.factory.TemplateFactory
import io.github.rorione.notificator.model.Request
import io.github.rorione.notificator.model.Template
import io.github.rorione.notificator.model.TemplateInfo
import io.github.rorione.notificator.repository.TemplateRepository
import io.github.rorione.notificator.service.TemplateService
import io.github.rorione.notificator.utils.RequestSenderUtils
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import javax.persistence.EntityNotFoundException
import org.junit.jupiter.api.BeforeEach
import org.mockito.ArgumentMatchers.anyString
import org.mockito.junit.jupiter.MockitoExtension
import org.stringtemplate.v4.ST
import org.stringtemplate.v4.misc.STNoSuchAttributeException
import java.lang.IllegalArgumentException

@ExtendWith(SpringExtension::class)
@Tag("unitTest")
@DisplayName("Perform operations for tasks")
internal class TemplateServiceTests {

    private fun <T> any(type: Class<T>): T = Mockito.any(type)

    @MockBean
    lateinit var templateRepository: TemplateRepository

    @MockBean
    lateinit var templateFactory: TemplateFactory

    @MockBean
    lateinit var requestFactory: RequestFactory

    @MockBean
    lateinit var senderUtils: RequestSenderUtils

    lateinit var templateService: TemplateService

    @BeforeEach
    fun initUseCase() {
        templateService = TemplateService(templateRepository, templateFactory, requestFactory, senderUtils)
    }

    @Nested
    @DisplayName("Add new template")
    inner class Add {

        private val input = TemplateDTOToInfoAdapter(
            TemplateDTO("Love letter", "I love \$name$", listOf("Girlfriend"))
        )

        @Test
        @DisplayName("Should do not throw exceptions")
        fun shouldDoNotThrowExceptions() {
            val template = Template("Love letter", "I love \$name$", listOf("Girlfriend"))

            Mockito.`when`(templateFactory.createTemplate(any(TemplateDTOToInfoAdapter::class.java)))
                .thenReturn(
                    template
                )

            Mockito.`when`(templateRepository.save(any(Template::class.java)))
                .thenReturn(
                    template
                )

            assertEquals(template, templateService.addTemplate(input))
        }
    }

    @Nested
    @DisplayName("Send template")
    inner class Send {

        private val template = Template("tempID", "\$abacaba$", listOf("friend1, friend2"))
        private val requestInfo = RequestDTOToInfoAdapter(
            RequestDTO("tempID", mapOf("\$abacaba$" to "text"), null)
        )

        @Nested
        @DisplayName("Correct request")
        inner class Correct {
            @Test
            @DisplayName("Should return correct status")
            fun shouldReturnCorrectStatus() {
                Mockito.`when`(templateRepository.getOne(anyString()))
                    .thenReturn(
                        template
                    )

                Mockito.`when`(requestFactory.createRequest(template, requestInfo))
                    .thenReturn(
                        Request("text", listOf("friend1, friend2"), null)
                    )

                Mockito.`when`(senderUtils.sendRequest(any(Request::class.java)))
                    .thenReturn(mapOf("friend1" to "request caught", "friend2" to "request caught"))

                assertEquals(
                    templateService.sendTemplate(requestInfo),
                    mapOf("friend1" to "request caught", "friend2" to "request caught")
                )
            }
        }

        @Nested
        @DisplayName("Incorrect request")
        inner class Incorrect {
            @Test
            @DisplayName("Template doesn't exist")
            fun templateDoesNotExist() {
                Mockito.`when`(templateRepository.getOne(anyString()))
                    .thenThrow(EntityNotFoundException())

                assertThrows<EntityNotFoundException> {
                    templateService.sendTemplate(
                        RequestDTOToInfoAdapter(
                            RequestDTO("null", mapOf(), null)
                        )
                    )
                }
            }


            @Test
            @DisplayName("Template variables are not correct")
            fun templateVariablesProblem() {
                Mockito.`when`(templateRepository.getOne(anyString()))
                    .thenReturn(
                        template
                    )

                Mockito.`when`(requestFactory.createRequest(template, requestInfo))
                    .thenThrow(IllegalArgumentException())

                assertThrows<IllegalArgumentException> {
                    templateService.sendTemplate(requestInfo)
                }

            }
        }


    }
}
