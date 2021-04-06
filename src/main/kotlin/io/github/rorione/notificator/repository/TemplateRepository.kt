package io.github.rorione.notificator.repository

import io.github.rorione.notificator.model.Template
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TemplateRepository : JpaRepository<Template, String>
