package br.com.sf.geocoder.config.config.solver

import br.com.sf.geocoder.core.domain.messages.CancelSolverCommand
import br.com.sf.geocoder.core.domain.messages.RequestSolverCommand
import br.com.sf.geocoder.core.domain.messages.SolutionRequestCommand
import br.com.sf.geocoder.core.solver.SolverManager
import kotlinx.coroutines.reactive.asPublisher
import kotlinx.coroutines.reactive.publish
import org.reactivestreams.Publisher
import org.springframework.stereotype.Component

@Component
class AsyncPipeSolver(
	private val solverManager: SolverManager
) {

	fun solve(cmd: RequestSolverCommand): Publisher<SolutionRequestCommand> {
		return solverManager.solve(cmd.solverKey, cmd.solution, cmd.solverName).asPublisher()
	}

	fun cancelSolver(cmd: CancelSolverCommand): Publisher<Unit> {
		return publish {
			solverManager.cancelSolver(cmd.solverKey, cmd.currentStatus, cmd.clear)
		}
	}
}
