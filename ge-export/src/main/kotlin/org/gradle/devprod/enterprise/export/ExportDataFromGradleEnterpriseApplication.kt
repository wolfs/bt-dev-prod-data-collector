package org.gradle.devprod.enterprise.export

import com.fasterxml.jackson.module.kotlin.KotlinModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.toList
import org.gradle.devprod.enterprise.export.configuration.GradleEnterpriseServer
import org.gradle.devprod.enterprise.export.model.BuildEvent
import org.springframework.beans.factory.getBean
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import java.time.Duration
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
			println("Duration of build ${build.buildId} for ${determineRootProjectName(events)} is ${buildTime.format()}, first test task started after ${timeToFirstTestTask(events)}")
		}
		.launchIn(CoroutineScope(Dispatchers.Default))
}
