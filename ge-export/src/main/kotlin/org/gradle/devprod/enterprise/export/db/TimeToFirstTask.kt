package org.gradle.devprod.enterprise.export.db

import org.springframework.data.annotation.Id
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

@Table
data class TimeToFirstTask(@Id var buildId: String, var project: String?, var timeToFirstTask: Long?, var pathToTestTask: String?) : Persistable<String> {
    override fun isNew(): Boolean = true
    override fun getId(): String? = buildId

}

interface TimeToFirstTaskRepo : CoroutineCrudRepository<TimeToFirstTask, String> {
    
}