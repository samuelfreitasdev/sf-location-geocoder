package br.com.sf.geocoder.config.config.solver

import br.com.sf.geocoder.config.config.SplitStreamProcessorTo
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.component.reactive.streams.util.UnwrapStreamProcessor
import org.springframework.stereotype.Component

@Component
class CamelSolverEvents : RouteBuilder() {

	override fun configure() {
		from("{{camel.route.consumer.request-solver}}")
			.routeId("request.solver.queue")
			.bean(AsyncPipeSolver::class.java, "solve")
			.process(SplitStreamProcessorTo("{{camel.route.producer.solution-request}}", context))

		from("{{camel.route.consumer.cancel-solver-topic}}")
			.routeId("cancel.solver.topic")
			.transform().spel("#{body.messageObject}")
			.bean(AsyncPipeSolver::class.java, "cancelSolver")
			.process(UnwrapStreamProcessor())
			.end()
	}

}
