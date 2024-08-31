package br.com.sf.geocoder.adapter.handler

import br.com.sf.geocoder.core.domain.model.GeocoderSolutionRequest
import br.com.sf.geocoder.core.serialization.Serde
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactive.asPublisher
import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketSession
import org.springframework.web.util.UriTemplate
import reactor.core.publisher.Mono

private val logger = KotlinLogging.logger {}

@Component
class AppSocketHandler(private val serde: Serde) : WebSocketHandler {
	private val sharedFlow = MutableSharedFlow<GeocoderSolutionRequest>()
	private val uriTemplate = UriTemplate("/ws/solution-state/{problemId}")

	override fun handle(session: WebSocketSession): Mono<Void> {
		val problemId = uriTemplate.match(session.handshakeInfo.uri.path)["problemId"]
		val source = fromChannel(problemId!!)

		return session
			.send(source.map(session::textMessage).asPublisher())
			.doOnError { e ->
				logger.warn(e) { "WebSocket Error!!" }
			}
	}

	private fun fromChannel(problemId: String): Flow<String> {
		return sharedFlow
			.filter { "${it.solution.problem.id}" == problemId }
			.map { data ->
				serde.toJson(data.copy(solution = data.solution))
			}
	}

	suspend fun broadcast(data: GeocoderSolutionRequest) {
		sharedFlow.emit(data)
	}
}
