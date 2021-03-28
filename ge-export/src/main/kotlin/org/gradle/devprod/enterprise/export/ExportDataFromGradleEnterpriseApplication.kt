package org.gradle.devprod.enterprise.export

import com.fasterxml.jackson.module.kotlin.KotlinModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.toList
import org.gradle.devprod.enterprise.export.configuration.GradleEnterpriseServer
import org.gradle.devprod.enterprise.export.db.TimeToFirstTask
import org.gradle.devprod.enterprise.export.db.TimeToFirstTaskRepo
import org.gradle.devprod.enterprise.export.model.BuildEvent
import org.springframework.beans.factory.getBean
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.r2dbc.core.DatabaseClient
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
	val dbClient = context.getBean<DatabaseClient>()
	runDdl(dbClient)
	val repo = context.getBean<TimeToFirstTaskRepo>()
	client.createEventStream()
		.onEach {
			val data = it.data()
			println("Received at ${ZonedDateTime.now()}: $data")
		}
		.map { it.data() }
		.filterNotNull()
		.map { build ->
			val events: List<BuildEvent> = client.getEvents(build).map { it.data()!! }.toList()
			val buildTime = determineTotalBuildTime(events)
			val rootProjectName = determineRootProjectName(events)
			val timeToFirstTestTask = timeToFirstTestTask(events)
			println("Duration of build ${build.buildId} for $rootProjectName is ${buildTime.format()}, first test task started after ${timeToFirstTestTask?.format()}")
			repo.save(TimeToFirstTask(build.buildId, rootProjectName, timeToFirstTestTask?.toMillis(), null))
		}
		.launchIn(CoroutineScope(Dispatchers.Default))
}

fun runDdl(client: DatabaseClient) =
	client.sql("CREATE TABLE IF NOT EXISTS TIME_TO_FIRST_TASK(build_id VARCHAR(255), project VARCHAR(255), path_to_test_task VARCHAR(255), TIME_TO_FIRST_TASK DECIMAL)").fetch().rowsUpdated().block()
