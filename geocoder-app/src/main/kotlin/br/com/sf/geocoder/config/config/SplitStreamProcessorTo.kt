package br.com.sf.geocoder.config.config

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.asPublisher
import org.apache.camel.CamelContext
import org.apache.camel.Exchange
import org.apache.camel.Processor
import org.apache.camel.component.reactive.streams.api.CamelReactiveStreams
import org.reactivestreams.Publisher

internal class SplitStreamProcessorTo(private val uri: String, context: CamelContext) : Processor {
	private val camelReactive = CamelReactiveStreams.get(context)

	override fun process(exchange: Exchange) {
		val body = exchange.`in`.body
		if (body is Publisher<*>) {
			val subscriber = camelReactive.subscriber(uri)
			body.asFlow().map { exchange.copy().apply { message.body = it } }.asPublisher().subscribe(subscriber)
		}
	}
}
