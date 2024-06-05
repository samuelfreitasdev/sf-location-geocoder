package br.com.sf.geocoder.core.domain.model

import java.util.*

data class Coordinate(
	val lat: Double,
	val lng: Double
) {
	companion object {
		val EMPTY = Coordinate(0.0, 0.0)
	}
}

data class GeocoderSummary(
	val id: Long,
	val name: String,
	val nLocations: Int,
	val numEnqueuedRequests: Int,
	val numRunningRequests: Int,

	val numTerminatedRequests: Int,
	val numNotSolvedRequests: Int,
	val numSolverRequests: Int
)

data class GeocoderProblem(
	val id: Long,
	val name: String = "",
	val points: List<Coordinate> = emptyList()
)

data class GeocoderRequest(
	val requestKey: UUID,
	val problemId: Long,
	val solver: String,
	val status: SolverStatus
)

data class GeocoderSolutionRequest(
	val solution: GeocoderSolution,
	val status: SolverStatus,
	val solverKey: UUID? = null
) {
	companion object {
		val EMPTY = GeocoderSolutionRequest(
			GeocoderSolution(
				GeocoderProblem(
					-1,
					"",
					emptyList()
				),
				Coordinate.EMPTY
			),
			SolverStatus.NOT_SOLVED
		)
	}
}

data class GeocoderSolution(
	val problem: GeocoderProblem,
	val suggestedCoordinate: Coordinate
) {
	fun isEmpty() = suggestedCoordinate == Coordinate.EMPTY
}

enum class SolverStatus {
	ENQUEUED, NOT_SOLVED, RUNNING, TERMINATED
}
