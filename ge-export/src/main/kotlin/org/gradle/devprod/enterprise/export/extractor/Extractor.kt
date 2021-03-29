package org.gradle.devprod.enterprise.export.extractor

import org.gradle.devprod.enterprise.export.model.BuildEvent

abstract class Extractor<T>(val eventType: String) {
    abstract fun extract(events: Iterable<BuildEvent>): T

    fun extractFrom(events: Map<String?, List<BuildEvent>>): T =
        extract(events.getOrDefault(eventType, listOf()))
}