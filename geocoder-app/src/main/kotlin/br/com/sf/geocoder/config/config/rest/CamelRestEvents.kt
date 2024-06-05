package br.com.sf.geocoder.config.config.rest

import org.apache.camel.builder.RouteBuilder
import org.apache.camel.component.hazelcast.HazelcastConstants
import org.apache.camel.component.hazelcast.HazelcastOperation
import org.apache.camel.component.reactive.streams.util.UnwrapStreamProcessor
import org.springframework.stereotype.Component


@Component
class CamelRestEvents : RouteBuilder() {

	override fun configure() {

		from("{{camel.route.consumer.enqueue-request-solver}}")
			.routeId("enqueue.request.solver")
			.log("Enqueue request solver: \${body}")
			.setHeader(HazelcastConstants.OPERATION, constant(HazelcastOperation.PUT))
//			.transform().spel("#{body.item}")
			.to("{{camel.route.producer.request-solver}}")

		from("{{camel.route.consumer.enqueue-solution-request}}")
			.routeId("enqueue.solution.request")
			.log("Enqueue request solver1: \${body}")
			.setHeader(HazelcastConstants.OPERATION, constant(HazelcastOperation.PUT))
			.to("{{camel.route.producer.solution-request}}")

		from("{{camel.route.consumer.solution-request}}")
			.routeId("solution.request.queue")
			.log("Solution request received: \${body}")
			.transform().spel("#{body.item}")
			.bean(AsyncPipeRest::class.java, "update")
			.process(UnwrapStreamProcessor())
			.to("{{camel.route.producer.solution-topic}}")
			.log("Enqueue request solve3r: \${body}")

		from("{{camel.route.consumer.broadcast-solution}}")
			.routeId("broadcast.solution")
			.log("Broadcasting solution: \${body}")
			.to("{{camel.route.producer.solution-topic}}")

		from("{{camel.route.consumer.solution-topic}}")
			.routeId("solution.topic")
			.log("Solution topic item received: \${body}")
			.transform().spel("#{body.messageObject}")
			.bean(AsyncPipeRest::class.java, "broadcast")
			.process(UnwrapStreamProcessor())
			.end()

		from("{{camel.route.consumer.broadcast-cancel-solver}}")
			.routeId("broadcast.broadcast.cancel.solver")
			.log("Request canceled: \${body}")
			.to("{{camel.route.producer.cancel-solver-topic}}")
	}

}
