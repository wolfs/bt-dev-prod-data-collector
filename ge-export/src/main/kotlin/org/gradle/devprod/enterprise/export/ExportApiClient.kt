package org.gradle.devprod.enterprise.export

import kotlinx.coroutines.flow.Flow
import org.gradle.devprod.enterprise.export.configuration.GradleEnterpriseServer
import org.gradle.devprod.enterprise.export.model.Build
import org.gradle.devprod.enterprise.export.model.BuildEvent
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.codec.ServerSentEvent
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToFlow
import java.util.Base64

@Component
class ExportApiClient(
    private val server: GradleEnterpriseServer
) {
    private val client: WebClient = WebClient.create("${server.url}/build-export")

    fun createEventStream(): Flow<ServerSentEvent<Build?>> = client.get()
        .uri("/v1/builds/since/${System.currentTimeMillis() - 72 * 60 * 60 * 1000}?stream")
        .bearerAuth()
        .retrieve()
        .bodyToFlow()

    private fun WebClient.RequestHeadersSpec<*>.bearerAuth() = header(
                "Authorization",
                "Bearer ${Base64.getEncoder().encodeToString(server.apiToken.toByteArray())}"
            )

    fun getEvents(build: Build): Flow<ServerSentEvent<BuildEvent>> = client.get()
        .uri("/v1/build/${build.buildId}/events?eventTypes=BuildStarted,BuildFinished,TaskStarted,ProjectStructure")
        .bearerAuth()
        .retrieve()
        .bodyToFlow()

}
