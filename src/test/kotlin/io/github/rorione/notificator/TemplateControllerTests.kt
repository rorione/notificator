package io.github.rorione.notificator

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.rorione.notificator.controller.TemplateController
import io.github.rorione.notificator.dto.RequestDTO
import io.github.rorione.notificator.dto.TemplateDTO
import io.github.rorione.notificator.dto.adapter.RequestDTOToInfoAdapter
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import javax.persistence.EntityNotFoundException


@ExtendWith(SpringExtension::class)
@WebMvcTest(TemplateController::class)
@AutoConfigureMockMvc(addFilters = false)

@Tag("Tests")
@DisplayName("Perform operations for tasks")
internal class TemplateControllerTests {

    private fun <T> any(type: Class<T>): T = Mockito.any(type)

    @MockBean
    lateinit var templateService: TemplateService

    @Autowired
    private lateinit var mockMvc: MockMvc

    private val objectMapper = ObjectMapper()

    @Nested
    @DisplayName("Add new template")
    inner class Add {

        val uri = "/api/template/add"

        @Nested
        @DisplayName("When validation fails")
        inner class WhenValidationFails {

            @Nested
            @DisplayName("When fields are empty")
            inner class WhenFieldsAreEmpty {
                private val model = TemplateDTO("", "", listOf())
                private val input = objectMapper.writeValueAsString(model)

                @Test
                @DisplayName("Should return the HTTP status code bad request")
                fun shouldReturnHttpStatusCodeBadRequest() {
                    mockMvc.perform(
                        MockMvcRequestBuilders.post(uri)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(input)
                    ).andExpect { status().isBadRequest }
                }
            }

            @Nested
            @DisplayName("When id is too long")
            inner class WhenIdIsTooLong {
                private val model = TemplateDTO("a".repeat(101), "Very important message", listOf("People"))
                private val input = objectMapper.writeValueAsString(model)

                @Test
                @DisplayName("Should return the HTTP status code bad request")
                fun shouldReturnHttpStatusCodeBadRequest() {
                    mockMvc.perform(
                        MockMvcRequestBuilders.post(uri)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(input)
                    ).andExpect { status().isBadRequest }
                }
            }

        }

        @Nested
        @DisplayName("When all is correct")
        inner class WhenAllCorrect {
            private val model = TemplateDTO("Love letter", "I love \$name$", listOf("Girlfriend"))
            private val input = objectMapper.writeValueAsString(model)

            @Test
            @DisplayName("Should return the HTTP status code ok")
            fun shouldReturnHttpStatusCodeOk() {
                mockMvc.perform(
                    MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(input)
                ).andExpect { status().isOk }
            }
        }
    }

    @Nested
    @DisplayName("Execute request")
    inner class Execute {
        val uri = "/api/template/execute"

        @Nested
        @DisplayName("When validation fails")
        inner class WhenValidationFails {

            @Nested
            @DisplayName("When fields are empty")
            inner class WhenFieldsAreEmpty {
                private val model = RequestDTO("", mapOf(), null)
                private val input = objectMapper.writeValueAsString(model)

                @Test
                @DisplayName("Should return the HTTP status code bad request")
                fun shouldReturnHttpStatusCodeBadRequest() {
                    mockMvc.perform(
                        MockMvcRequestBuilders.post(uri)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(input)
                    ).andExpect { status().isBadRequest }
                }
            }

            @Nested
            @DisplayName("When id is too long")
            inner class WhenIdIsTooLong {
                private val model = RequestDTO("a".repeat(101), mapOf("key" to "value"), null)
                private val input = objectMapper.writeValueAsString(model)

                @Test
                @DisplayName("Should return the HTTP status code bad request")
                fun shouldReturnHttpStatusCodeBadRequest() {
                    mockMvc.perform(
                        MockMvcRequestBuilders.post(uri)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(input)
                    ).andExpect { status().isBadRequest }
                }
            }

        }

        @Nested
        @DisplayName("When all is correct")
        inner class WhenAllCorrect {
            private val model = RequestDTO("Love letter", mapOf("name" to "Evelyn"), null)
            private val input = objectMapper.writeValueAsString(model)

            @Test
            @DisplayName("Should return the HTTP status code ok")
            fun shouldReturnHttpStatusCodeOk() {
                mockMvc.perform(
                    MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(input)
                ).andExpect { status().isOk }
            }

            @Test
            @DisplayName("Should return JSON with success results")
            fun shouldReturnJSONWithSuccessResults() {
                Mockito
                    .`when`(templateService.sendTemplate(any(RequestDTOToInfoAdapter::class.java)))
                    .thenReturn(mapOf("love.ru" to "success"))

                mockMvc.perform(
                    MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(input)
                ).andExpect { result ->
                    assertEquals(
                        objectMapper.writeValueAsString(mapOf("love.ru" to "success")),
                        result.response.contentAsString
                    )
                }
            }
        }

        @Nested
        @DisplayName("When request type is not null")
        inner class WhenRequestTypeIsNotNull {
            private val model = RequestDTO(
                "Love letter",
                mapOf("name" to "Evelyn"),
                RequestSenderUtils.RequestType.POST_REQUEST
            )
            private val input = objectMapper.writeValueAsString(model)

            @Test
            @DisplayName("Should return the HTTP status code ok")
            fun shouldReturnHttpStatusCodeOk() {
                mockMvc.perform(
                    MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(input)
                ).andExpect { status().isOk }
            }

            @Test
            @DisplayName("Should return JSON with success results")
            fun shouldReturnJSONWithSuccessResults() {
                Mockito
                    .`when`(templateService.sendTemplate(any(RequestDTOToInfoAdapter::class.java)))
                    .thenReturn(mapOf("love.ru" to "success"))

                mockMvc.perform(
                    MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(input)
                ).andExpect { result ->
                    assertEquals(
                        objectMapper.writeValueAsString(mapOf("love.ru" to "success")),
                        result.response.contentAsString
                    )
                }
            }
        }

        @Nested
        @DisplayName("When template does not exist")
        inner class WhenTemplateNotExist {
            private val model = RequestDTO("Love letter", mapOf("name" to "Evelyn"), null)
            private val input = objectMapper.writeValueAsString(model)

            @Test
            @DisplayName("Should throw EntityNotFound exception")
            fun shouldReturnJSONWithSuccessResults() {
                Mockito
                    .`when`(templateService.sendTemplate(any(RequestDTOToInfoAdapter::class.java)))
                    .thenThrow(EntityNotFoundException::class.java)

                val exception = assertThrows<Exception> {
                    mockMvc.perform(
                        MockMvcRequestBuilders.post(uri)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(input)
                    )
                }

                assertEquals(exception.cause!!::class.java, EntityNotFoundException::class.java)
            }
        }
    }
}
