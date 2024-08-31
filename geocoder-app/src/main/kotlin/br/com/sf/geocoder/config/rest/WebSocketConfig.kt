package br.com.sf.geocoder.config.rest

import br.com.sf.geocoder.adapter.handler.AppSocketHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.HandlerMapping
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter


@Configuration
class WebSocketConfig {

	@Bean
	fun handlerMapping(webSocketHandler: AppSocketHandler): HandlerMapping {
		val map = mapOf("/ws/solution-state/*" to webSocketHandler)
		return SimpleUrlHandlerMapping(map, -1)
	}

	@Bean
	fun handlerAdapter() = WebSocketHandlerAdapter()

//	@Bean
//	fun serverEndpointExporter(): ServerEndpointExporter {
//		return ServerEndpointExporter()
//	}
}
