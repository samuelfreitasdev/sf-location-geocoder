package br.com.sf.geocoder.core.domain.ports.repo

import br.com.sf.geocoder.core.domain.model.GeocoderRequest
import kotlinx.coroutines.flow.Flow
import java.time.Duration
import java.util.UUID

interface GeocoderRequestPort {
	suspend fun refreshSolverRequests(timeout: Duration): Int

	suspend fun createRequest(request: GeocoderRequest): GeocoderRequest?

	suspend fun currentSolverRequest(problemId: Long): GeocoderRequest?

	suspend fun currentSolverRequest(solverKey: UUID): GeocoderRequest?

	fun requestsByProblemIdAndSolverName(
		problemId: Long,
		solverName: String,
	): Flow<GeocoderRequest>
}
