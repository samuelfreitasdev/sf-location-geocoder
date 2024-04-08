package br.com.sf.geocoder.core.domain.ports.repo

import br.com.sf.geocoder.core.domain.model.GeocoderProblem
import br.com.sf.geocoder.core.domain.model.GeocoderSummary
import kotlinx.coroutines.flow.Flow

interface GeocoderProblemPort {

	fun findAll(query: String = "", offset: Int = 0, limit: Int = 25): Flow<GeocoderSummary>

	suspend fun count(query: String = ""): Long

	suspend fun getById(problemId: Long): GeocoderProblem?

	suspend fun create(problem: GeocoderProblem): GeocoderProblem

	suspend fun deleteById(problemId: Long)

	suspend fun update(id: Long, problem: GeocoderProblem)

}
