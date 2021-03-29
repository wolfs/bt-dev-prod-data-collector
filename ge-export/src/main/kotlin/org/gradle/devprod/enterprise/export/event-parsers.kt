package org.gradle.devprod.enterprise.export

import java.time.Duration

fun Duration.format() = "${toMinutes()}:${String.format("%02d", toSecondsPart())} min"
