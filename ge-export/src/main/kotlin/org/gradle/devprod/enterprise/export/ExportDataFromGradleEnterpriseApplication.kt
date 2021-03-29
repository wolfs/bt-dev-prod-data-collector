package org.gradle.devprod.enterprise.export

import com.fasterxml.jackson.module.kotlin.KotlinModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import org.gradle.devprod.enterprise.export.configuration.GradleEnterpriseServer
import org.gradle.devprod.enterprise.export.extractor.BuildFinished
import org.gradle.devprod.enterprise.export.extractor.BuildStarted
import org.gradle.devprod.enterprise.export.extractor.Extractor
import org.gradle.devprod.enterprise.export.extractor.FirstTestTaskStart
import org.gradle.devprod.enterprise.export.extractor.RootProjectNames
import org.gradle.devprod.enterprise.export.extractor.Tags
import org.gradle.devprod.enterprise.export.generated.jooq.Tables.TAGS
import org.gradle.devprod.enterprise.export.generated.jooq.Tables.TIME_TO_FIRST_TASK
import org.gradle.devprod.enterprise.export.model.BuildEvent
import org.jooq.DSLContext
import org.springframework.beans.factory.getBean
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

@SpringBootApplication
@EnableConfigurationProperties(GradleEnterpriseServer::class)
	class ExportDataFromGradleEnterpriseApplication {
	@Bean
	fun jsonCustomizer(): Jackson2ObjectMapperBuilderCustomizer = Jackson2ObjectMapperBuilderCustomizer { builder ->
		builder.modulesToInstall(KotlinModule())
	}
}

fun main(args: Array<String>) {
	val context = runApplication<ExportDataFromGradleEnterpriseApplication>(*args)
	val extractor = context.getBean<ExportApiExtractorService>()
	extractor.streamToDatabase().launchIn(CoroutineScope(Dispatchers.IO))
}
