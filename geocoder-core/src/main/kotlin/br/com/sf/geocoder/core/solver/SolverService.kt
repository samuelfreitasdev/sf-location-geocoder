package br.com.sf.geocoder.core.solver

import br.com.sf.geocoder.core.domain.messages.CancelSolverCommand
import br.com.sf.geocoder.core.domain.messages.RequestSolverCommand
import br.com.sf.geocoder.core.domain.messages.SolutionRequestCommand
import br.com.sf.geocoder.core.domain.model.GeocoderSolutionRequest
import br.com.sf.geocoder.core.domain.model.SolverStatus
import br.com.sf.geocoder.core.domain.ports.events.BroadcastPort
import br.com.sf.geocoder.core.domain.ports.events.SolverEventsPort
import br.com.sf.geocoder.core.domain.repository.SolverRepository
import br.com.sf.geocoder.core.solver.spi.Solver
import java.util.*

class SolverService(
	private val broadcastPort: BroadcastPort,
	private val solverEventsPort: SolverEventsPort,
	private val solverRepository: SolverRepository
) {

	fun solverNames() = Solver.getNamedSolvers().keys

	suspend fun currentSolutionRequest(problemId: Long): GeocoderSolutionRequest? {
		return solverRepository.currentSolutionRequest(problemId)
	}

	suspend fun showStatus(problemId: Long): SolverStatus =
		solverRepository.currentSolverRequest(problemId)?.status ?: SolverStatus.NOT_SOLVED

	suspend fun update(solRequest: GeocoderSolutionRequest, clear: Boolean): GeocoderSolutionRequest {
		return solverRepository.addNewSolution(solRequest.solution, solRequest.solverKey!!, solRequest.status, clear)
	}

	suspend fun enqueueSolverRequest(problemId: Long, solverName: String): UUID? {
		return solverRepository.enqueue(problemId, solverName)?.let { request ->
			solverRepository.currentSolutionRequest(problemId)?.let { solution ->
//				solverEventsPort.enqueueSolutionRequest(
//					SolutionRequestCommand(solution, true)
//				)
				solverEventsPort.enqueueRequestSolver(
					RequestSolverCommand(solution.solution, request.requestKey, solverName)
				)
			}
//
//			solverRepository.currentDetailedSolution(problemId)?.let { detailedSolution ->
//				solverEventsPort.enqueueRequestSolver(
//					RequestSolverCommand(detailedSolution, request.requestKey, solverName)
//				)
			request.requestKey
//			}
		}
	}

	suspend fun terminate(solverKey: UUID) = terminateEarly(solverKey, false)

	suspend fun clear(solverKey: UUID) = terminateEarly(solverKey, true)

	private suspend fun terminateEarly(solverKey: UUID, clear: Boolean) {
		solverRepository.currentSolverRequest(solverKey)?.also { solverRequest ->
			if (solverRequest.status in listOf(SolverStatus.RUNNING, SolverStatus.ENQUEUED)) {
				solverEventsPort.broadcastCancelSolver(
					CancelSolverCommand(solverKey, solverRequest.status, clear)
				)
			} else if (solverRequest.status == SolverStatus.TERMINATED && clear) {
				solverRepository.currentSolutionRequest(solverRequest.problemId)?.let { solutionRequest ->
					solverEventsPort.enqueueSolutionRequest(
						SolutionRequestCommand(solutionRequest, true)
					)
				}
			}
		}
	}
}
