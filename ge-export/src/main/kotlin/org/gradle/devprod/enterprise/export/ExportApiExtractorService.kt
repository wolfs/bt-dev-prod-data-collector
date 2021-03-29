package org.gradle.devprod.enterprise.export

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.toList
import org.gradle.devprod.enterprise.export.extractor.BuildFinished
import org.gradle.devprod.enterprise.export.extractor.BuildStarted
import org.gradle.devprod.enterprise.export.extractor.Extractor
import org.gradle.devprod.enterprise.export.extractor.FirstTestTaskStart
import org.gradle.devprod.enterprise.export.extractor.RootProjectNames
import org.gradle.devprod.enterprise.export.extractor.Tags
import org.gradle.devprod.enterprise.export.generated.jooq.Tables
import org.gradle.devprod.enterprise.export.model.Build
import org.gradle.devprod.enterprise.export.model.BuildEvent
import org.jooq.DSLContext
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

@Service
class ExportApiExtractorService(
    private
    val exportApiClient: ExportApiClient,
    private
    val create: DSLContext
) {

    fun streamToDatabase() =
        exportApiClient.createEventStream()
            .onEach {
                val data = it.data()
                println("Received at ${ZonedDateTime.now()}: $data")
            }
            .map { it.data() }
            .filterNotNull()
            .map(this::persistToDatabase)

    private suspend fun persistToDatabase(build: Build) {
        val existing = create.fetchAny(Tables.BUILD, Tables.BUILD.BUILD_ID.eq(build.buildId))
        if (existing == null) {
            val extractors = listOf(BuildStarted, BuildFinished, FirstTestTaskStart, Tags, RootProjectNames)
            val events: Map<String?, List<BuildEvent>> = exportApiClient.getEvents(build, extractors.map(Extractor<*>::eventType))
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

            val record = create.newRecord(Tables.BUILD)
            record.buildId = build.buildId
            record.buildStart = OffsetDateTime.ofInstant(Instant.ofEpochMilli(build.timestamp), ZoneId.systemDefault())
            record.timeToFirstTestTask = timeToFirstTestTask?.toMillis()
            record.pathToFirstTestTask = firstTestTaskStart?.first
            record.rootProject = rootProjectName
            record.store()

            tags.forEach { tag ->
                val tagRecord = create.newRecord(Tables.TAGS)
                tagRecord.buildId = build.buildId
                tagRecord.tagName = tag
                tagRecord.store()
            }
        }
    }

}