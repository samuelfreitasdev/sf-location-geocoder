package br.com.sf.geocoder.config.config.rest

import br.com.sf.geocoder.adapter.handler.AppSocketHandler
import br.com.sf.geocoder.core.domain.messages.SolutionCommand
import br.com.sf.geocoder.core.domain.messages.SolutionRequestCommand
import br.com.sf.geocoder.core.solver.SolverService
import kotlinx.coroutines.reactive.publish
import org.reactivestreams.Publisher
import org.springframework.stereotype.Component

@Component
class AsyncPipeRest(
	private val solverService: SolverService,
	private val appSocketHandler: AppSocketHandler,
) {
	fun update(cmd: SolutionRequestCommand): Publisher<SolutionCommand> {
		return publish {
			send(SolutionCommand(solverService.update(cmd.solutionRequest, cmd.clear)))
		}
	}

	fun broadcast(cmd: SolutionCommand): Publisher<Unit> {
		return publish {
			appSocketHandler.broadcast(cmd.solutionRequest)
		}
	}
}
