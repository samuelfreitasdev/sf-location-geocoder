package br.com.sf.geocoder.core.domain.ports.events

import br.com.sf.geocoder.core.domain.messages.CancelSolverCommand
import br.com.sf.geocoder.core.domain.messages.RequestSolverCommand
import br.com.sf.geocoder.core.domain.messages.SolutionRequestCommand

interface SolverEventsPort {

	fun enqueueRequestSolver(command: RequestSolverCommand)

	fun enqueueSolutionRequest(command: SolutionRequestCommand)

	fun broadcastCancelSolver(command: CancelSolverCommand)

}
