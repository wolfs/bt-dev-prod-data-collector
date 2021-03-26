package org.gradle.devprod.enterprise.export.model

data class Build(val buildId: String, val pluginVersion: String, val gradleVersion: String, val timestamp: Long)
