package org.gradle.devprod.enterprise.export.model

import java.time.Duration

data class TaskStarted(val path: String, val startedAfter: Duration)