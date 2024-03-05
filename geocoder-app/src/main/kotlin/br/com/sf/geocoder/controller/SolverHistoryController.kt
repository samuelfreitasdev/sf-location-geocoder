package br.com.sf.geocoder.controller

import br.com.sf.geocoder.core.domain.model.Coordinate
import br.com.sf.geocoder.core.domain.model.GeocoderRequest
import br.com.sf.geocoder.core.domain.ports.repo.GeocoderRequestPort
import br.com.sf.geocoder.core.domain.ports.repo.GeocoderSolutionPort
import kotlinx.coroutines.flow.Flow
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/solver-history")
class SolverHistoryController(
	private val geocoderRequestPort: GeocoderRequestPort,
	private val geocoderSolutionPort: GeocoderSolutionPort
) {

	@GetMapping("/{problemId}/requests/{solverName}", produces = [MediaType.APPLICATION_JSON_VALUE])
	fun requests(
		@PathVariable problemId: Long,
		@PathVariable solverName: String
	): ResponseEntity<Flow<GeocoderRequest>> {
		return geocoderRequestPort.requestsByProblemIdAndSolverName(problemId, solverName).let {
			ResponseEntity.ok(it)
		}
	}

	@GetMapping("/{problemId}/solutions", produces = [MediaType.APPLICATION_JSON_VALUE])
	suspend fun solutions(
		@PathVariable problemId: Long
	): ResponseEntity<Coordinate> {
		return geocoderSolutionPort.currentSolution(problemId)
			.let {
				ResponseEntity.ok(it)
			}
	}

}
