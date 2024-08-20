package br.com.sf.geocoder.core.domain.messages

import br.com.sf.geocoder.core.domain.model.GeocoderSolution
import br.com.sf.geocoder.core.domain.model.GeocoderSolutionRequest
import br.com.sf.geocoder.core.domain.model.SolverStatus
import java.util.UUID

data class RequestSolverCommand(val solution: GeocoderSolution, val solverKey: UUID, val solverName: String)

data class CancelSolverCommand(val solverKey: UUID, val currentStatus: SolverStatus, val clear: Boolean = false)

data class SolutionRequestCommand(val solutionRequest: GeocoderSolutionRequest, val clear: Boolean)

data class SolutionCommand(val solutionRequest: GeocoderSolutionRequest)
