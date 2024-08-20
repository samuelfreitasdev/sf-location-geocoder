package br.com.sf.geocoder.config

import br.com.sf.geocoder.core.domain.messages.CancelSolverCommand
import br.com.sf.geocoder.core.domain.messages.RequestSolverCommand
import br.com.sf.geocoder.core.domain.messages.SolutionCommand
import br.com.sf.geocoder.core.domain.messages.SolutionRequestCommand
import br.com.sf.geocoder.core.domain.ports.events.BroadcastPort
import br.com.sf.geocoder.core.domain.ports.events.SolverEventsPort
import org.apache.camel.ProducerTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class CamelTemplateConfig(
	@Value("\${camel.route.producer.enqueue-request-solver}") private val enqueueRequestSolver: String,
	@Value("\${camel.route.producer.enqueue-solution-request}") private val enqueueSolutionRequest: String,
	@Value("\${camel.route.producer.broadcast-solution}") private val broadcastSolution: String,
	@Value("\${camel.route.producer.broadcast-cancel-solver}") private val broadcastCancelSolver: String,
	private val template: ProducerTemplate,
) : BroadcastPort, SolverEventsPort {
	override fun enqueueRequestSolver(command: RequestSolverCommand) {
		template.sendBody(enqueueRequestSolver, command)
	}

	override fun enqueueSolutionRequest(command: SolutionRequestCommand) {
		template.sendBody(enqueueSolutionRequest, command)
	}

	override fun broadcastSolution(command: SolutionCommand) {
		template.sendBody(broadcastSolution, command)
	}

	override fun broadcastCancelSolver(command: CancelSolverCommand) {
		template.sendBody(broadcastCancelSolver, command)
	}
}
