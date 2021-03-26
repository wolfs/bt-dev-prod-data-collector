package org.gradle.devprod.enterprise.export

import org.gradle.devprod.enterprise.export.model.BuildEvent
import org.gradle.devprod.enterprise.export.model.TaskStarted
import java.time.Duration
import java.time.Instant

fun determineTotalBuildTime(events: Collection<BuildEvent>): Duration {
    val buildStarted = buildStartedAt(events)
    val buildFinished = events.find { it.type?.eventType == "BuildFinished" }!!.timestamp
    return Duration.ofMillis(buildFinished - buildStarted)
}

fun determineRootProjectName(events: Collection<BuildEvent>): String? {
    val rootProjectNames = events.filter { it.eventType == "ProjectStructure" }
        .mapNotNull { it.data?.stringProperty("rootProjectName") }
    return rootProjectNames.firstOrNull { !it.startsWith("build-logic") }
}

private fun buildStartedAt(events: Collection<BuildEvent>) =
    events.find { it.eventType == "BuildStarted" }!!.timestamp

fun timeToFirstTestTask(events: Collection<BuildEvent>): Duration? {
    val buildStarted = buildStartedAt(events)
    val testTasksStarted = events
        .filter { it.eventType == "TaskStarted" && it.data?.stringProperty("className")?.endsWith("Test") ?: false }
        .map {
            val path = it.data?.stringProperty("path")!!
            val startTime = Instant.ofEpochMilli(it.timestamp)
            val startedAfter = Duration.between(Instant.ofEpochMilli(buildStarted), startTime)
            TaskStarted(path, startedAfter)
        }
    return testTasksStarted.minByOrNull(TaskStarted::startedAfter)?.startedAfter
}

private fun Any.stringProperty(name: String): String? = (this as Map<*, *>)[name] as String?

fun Duration.format() = "${toMinutes()}:${String.format("%02d", toSecondsPart())} min"
