package br.com.sf.geocoder.controller

import br.com.sf.geocoder.core.domain.model.GeocoderProblem
import br.com.sf.geocoder.core.domain.model.GeocoderSummary
import br.com.sf.geocoder.core.domain.ports.repo.GeocoderProblemPort
import kotlinx.coroutines.flow.toList
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/problems")
class ProblemController(val repo: GeocoderProblemPort) {
	@GetMapping("", produces = [MediaType.APPLICATION_JSON_VALUE])
	suspend fun index(
		@RequestParam("page", defaultValue = "0") page: Int,
		@RequestParam("size", defaultValue = "25") size: Int,
		@RequestParam("q", defaultValue = "") q: String,
	): Page<GeocoderSummary> {
		val count = repo.count(q)
		val problems = repo.findAll(q, page * size, size).toList()
		return PageImpl(problems, PageRequest.of(page, size), count)
	}

	@GetMapping("/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
	suspend fun show(
		@PathVariable id: Long,
	): ResponseEntity<GeocoderProblem> {
		return repo.getById(id)
			?.let { ResponseEntity.ok(it) } ?: ResponseEntity.notFound().build()
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("", produces = [MediaType.APPLICATION_JSON_VALUE])
	suspend fun create(
		@RequestBody problem: GeocoderProblem,
	): ResponseEntity<GeocoderProblem> {
		return repo.create(problem)
			.let {
				return@let ResponseEntity(
					it,
					HttpStatus.CREATED,
				)
			}
	}

	@PostMapping("/{id}/copy", produces = [MediaType.APPLICATION_JSON_VALUE])
	suspend fun copy(
		@PathVariable id: Long,
		@RequestBody problem: GeocoderProblem,
	): ResponseEntity<Void> {
		return repo.create(problem)
			.let { ResponseEntity.ok().build() }
	}

	@PutMapping("/{id}/update", produces = [MediaType.APPLICATION_JSON_VALUE])
	suspend fun update(
		@PathVariable id: Long,
		@RequestBody problem: GeocoderProblem,
	): ResponseEntity<Void> {
		return repo.update(id, problem)
			.let { ResponseEntity.ok().build() }
	}

	@DeleteMapping("/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
	suspend fun remove(
		@PathVariable id: Long,
	): ResponseEntity<Void> {
		return repo.deleteById(id)
			.let { ResponseEntity.ok().build() }
	}
}
