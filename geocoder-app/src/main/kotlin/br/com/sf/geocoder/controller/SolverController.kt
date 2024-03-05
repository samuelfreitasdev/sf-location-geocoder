package br.com.sf.geocoder.controller

import br.com.sf.geocoder.core.domain.model.GeocoderSolutionRequest
import br.com.sf.geocoder.core.domain.model.SolverStatus
import br.com.sf.geocoder.core.solver.SolverService
import io.github.oshai.kotlinlogging.KLogging
import io.swagger.v3.oas.annotations.Parameter
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.WebSession

@RestController
@RequestMapping("/api/solver")
class SolverController(
	private val solverService: SolverService,
) : KLogging() {

	@GetMapping("/solver-names", produces = [MediaType.APPLICATION_JSON_VALUE])
	fun solverNames() = solverService.solverNames().sorted()

	@PostMapping("/{id}/solve/{solverName}", produces = [MediaType.APPLICATION_JSON_VALUE])
	suspend fun solve(@PathVariable id: Long, @PathVariable solverName: String): ResponseEntity<SolverStatus> {
		val uuid = solverService.enqueueSolverRequest(id, solverName)
		logger.info("Solver request enqueued with id: $uuid")
		return ResponseEntity.ok(solverService.showStatus(id))
	}

	@PostMapping("/{id}/terminate", produces = [MediaType.APPLICATION_JSON_VALUE])
	suspend fun terminate(@PathVariable id: Long): ResponseEntity<SolverStatus> {
		solverService.currentSolutionRequest(id)?.also { it.solverKey?.also { key -> solverService.terminate(key) } }
		return ResponseEntity.ok(solverService.showStatus(id))
	}

	@PostMapping("/{id}/clean", produces = [MediaType.APPLICATION_JSON_VALUE])
	suspend fun clear(@PathVariable id: Long): ResponseEntity<SolverStatus> {
		solverService.currentSolutionRequest(id)?.also { it.solverKey?.also { key -> solverService.clear(key) } }
		return ResponseEntity.ok(solverService.showStatus(id))
	}

	@GetMapping("/{id}/solution", produces = [MediaType.APPLICATION_JSON_VALUE])
	suspend fun solutionState(
		@PathVariable id: Long,
		@Parameter(hidden = true) session: WebSession
	): ResponseEntity<GeocoderSolutionRequest> {
		return solverService.currentSolutionRequest(id)?.let { ResponseEntity.ok(it) } ?: ResponseEntity.notFound()
			.build()
	}

}
