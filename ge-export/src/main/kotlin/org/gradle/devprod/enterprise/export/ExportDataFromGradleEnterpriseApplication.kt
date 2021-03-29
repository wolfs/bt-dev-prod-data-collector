package org.gradle.devprod.enterprise.export

import com.fasterxml.jackson.module.kotlin.KotlinModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.toList
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

	val client = context.getBean<ExportApiClient>()
	val create = context.getBean<DSLContext>()
	client.createEventStream()
		.onEach {
			val data = it.data()
			println("Received at ${ZonedDateTime.now()}: $data")
		}
		.map { it.data() }
		.filterNotNull()
		.map { build ->
			val existing = create.fetchAny(TIME_TO_FIRST_TASK, TIME_TO_FIRST_TASK.BUILD_ID.eq(build.buildId))
			if (existing == null) {
				val extractors = listOf(BuildStarted, BuildFinished, FirstTestTaskStart, Tags, RootProjectNames)
				val events: Map<String?, List<BuildEvent>> = client.getEvents(build, extractors.map(Extractor<*>::eventType))
					.map { it.data()!! }
					.toList()
					.groupBy(BuildEvent::eventType)
				val buildStarted = BuildStarted.extractFrom(events)
				val buildFinished = BuildFinished.extractFrom(events)
				val buildTime = Duration.between(buildStarted, buildFinished)
				val rootProjectName = RootProjectNames.extractFrom(events).firstOrNull { !it.startsWith("build-logic") }
				val firstTestTaskStart = FirstTestTaskStart.extractFrom(events)
				val timeToFirstTestTask = firstTestTaskStart?.let { Duration.between(buildStarted, it.second) }
				val tags = Tags.extractFrom(events)
				println("Duration of build ${build.buildId} for $rootProjectName is ${buildTime.format()}, first test task started after ${timeToFirstTestTask?.format()}")
				val record = create.newRecord(TIME_TO_FIRST_TASK)
				record.buildId = build.buildId
				record.buildStart = LocalDateTime.ofInstant(Instant.ofEpochMilli(build.timestamp), ZoneId.systemDefault())
				record.timeToFirstTask = timeToFirstTestTask?.toMillis()
				record.pathToTestTask = firstTestTaskStart?.first
				record.project = rootProjectName
				record.store()

				tags.forEach { tag ->
					val tagRecord = create.newRecord(TAGS)
					tagRecord.buildId = build.buildId
					tagRecord.tagName = tag
					tagRecord.store()
				}
			}
		}
		.launchIn(CoroutineScope(Dispatchers.Default))
}
