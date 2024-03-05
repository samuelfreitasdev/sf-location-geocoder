package br.com.sf.geocoder.core.domain.repository

import br.com.sf.geocoder.core.domain.model.GeocoderRequest
import br.com.sf.geocoder.core.domain.model.GeocoderSolution
import br.com.sf.geocoder.core.domain.model.GeocoderSolutionRequest
import br.com.sf.geocoder.core.domain.model.SolverStatus
import br.com.sf.geocoder.core.domain.ports.repo.GeocoderProblemPort
import br.com.sf.geocoder.core.domain.ports.repo.GeocoderRequestPort
import br.com.sf.geocoder.core.domain.ports.repo.GeocoderSolutionPort
import java.util.*

class SolverRepository(
	private val vrpProblemPort: GeocoderProblemPort,
	private val vrpSolverSolutionPort: GeocoderSolutionPort,
	private val vrpSolverRequestPort: GeocoderRequestPort
) {

	suspend fun enqueue(problemId: Long, solverName: String): GeocoderRequest? {
		return vrpSolverRequestPort.createRequest(
			GeocoderRequest(UUID.randomUUID(), problemId, solverName, SolverStatus.ENQUEUED)
		)
	}

	suspend fun currentSolverRequest(problemId: Long): GeocoderRequest? {
		return vrpSolverRequestPort.currentSolverRequest(problemId)
	}

	suspend fun currentSolverRequest(requestKey: UUID): GeocoderRequest? {
		return vrpSolverRequestPort.currentSolverRequest(requestKey)
	}

	suspend fun currentSolutionRequest(problemId: Long): GeocoderSolutionRequest? {
		return vrpSolverSolutionPort.currentSolutionRequest(problemId)
	}

	suspend fun addNewSolution(
		sol: GeocoderSolution,
		uuid: UUID,
		solverStatus:
		SolverStatus,
		clear: Boolean
	): GeocoderSolutionRequest {
		return vrpSolverSolutionPort.upsertSolution(
			sol.problem.id,
			solverStatus,
			sol.suggestedCoordinate,
			clear,
			uuid
		)
	}

//	suspend fun currentDetailedSolution(problemId: Long): GeocoderDetailedSolution? {
//		return vrpProblemPort.getMatrixById(problemId)?.let { currentMatrix ->
//			vrpSolverSolutionPort.currentSolutionRequest(problemId)?.let { solutionRequest ->
//				VrpDetailedSolution(solutionRequest.solution, currentMatrix)
//			}
//		}
//	}
}
