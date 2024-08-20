package br.com.sf.geocoder.core.domain.ports.repo

import br.com.sf.geocoder.core.domain.model.Coordinate
import br.com.sf.geocoder.core.domain.model.GeocoderSolutionRequest
import br.com.sf.geocoder.core.domain.model.SolverStatus
import java.util.UUID

interface GeocoderSolutionPort {
	suspend fun currentSolution(problemId: Long): Coordinate

	suspend fun currentSolutionRequest(problemId: Long): GeocoderSolutionRequest?

	suspend fun upsertSolution(
		problemId: Long,
		solverStatus: SolverStatus,
		coordinate: Coordinate,
		clear: Boolean,
		uuid: UUID,
	): GeocoderSolutionRequest

// 	fun solutionHistory(problemId: Long): Flow<GeocoderSolverObjective>
}
