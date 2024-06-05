package br.com.sf.geocoder.adapters.database

import br.com.sf.geocoder.core.domain.model.*
import br.com.sf.geocoder.core.domain.ports.repo.GeocoderSolutionPort
import br.com.sf.geocoder.core.serialization.Serde
import br.com.sf.geocoder.core.serialization.fromJson
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactive.awaitSingle
import org.jooq.DSLContext
import org.jooq.JSON
import org.jooq.Record3
import org.jooq.generated.tables.records.GeocoderProblemRecord
import org.jooq.generated.tables.records.GeocoderSolutionRecord
import org.jooq.generated.tables.records.GeocoderSolverRequestRecord
import org.jooq.generated.tables.references.GEOCODER_PROBLEM
import org.jooq.generated.tables.references.GEOCODER_SOLUTION
import org.jooq.generated.tables.references.GEOCODER_SOLUTION_REQUEST
import org.jooq.generated.tables.references.GEOCODER_SOLVER_REQUEST
import org.jooq.kotlin.coroutines.transactionCoroutine
import java.time.Instant
import java.util.*

class GeocoderSolutionJooqAdapter(
	private val serde: Serde,
	private val dsl: DSLContext
) : GeocoderSolutionPort {

	override suspend fun currentSolution(problemId: Long): Coordinate = dsl
		.select(GEOCODER_SOLUTION)
		.where(GEOCODER_SOLUTION.GEOCODER_PROBLEM_ID.eq(problemId))
		.limit(1)
		.awaitFirstOrNull()
		?.let { sol ->
			serde.fromJson(sol.get(GEOCODER_SOLUTION.SUGGESTEDCOORDINATE)?.data().orEmpty())
		} ?: Coordinate.EMPTY

	override suspend fun currentSolutionRequest(problemId: Long): GeocoderSolutionRequest? {
		return currentSolutionRequestQuery(dsl, problemId)
			.awaitFirstOrNull()
			?.let(::convertRecordToSolutionRequest)
	}

	private fun currentSolutionRequestQuery(dsl: DSLContext, problemId: Long) = dsl
		.select(GEOCODER_PROBLEM, GEOCODER_SOLUTION, GEOCODER_SOLVER_REQUEST)
		.from(GEOCODER_PROBLEM)
		.leftJoin(GEOCODER_SOLUTION).on(GEOCODER_SOLUTION.GEOCODER_PROBLEM_ID.eq(GEOCODER_PROBLEM.ID))
		.leftJoin(GEOCODER_SOLVER_REQUEST).on(GEOCODER_SOLVER_REQUEST.PROBLEM_ID.eq(GEOCODER_PROBLEM.ID))
		.where(GEOCODER_PROBLEM.ID.eq(problemId))
		.orderBy(GEOCODER_SOLVER_REQUEST.UPDATED_AT.desc())
		.limit(1)

	private fun convertRecordToSolutionRequest(
		record: Record3<GeocoderProblemRecord, GeocoderSolutionRecord, GeocoderSolverRequestRecord>
	): GeocoderSolutionRequest {

		val (problem, solution, solverRequest) = record

		val geocoderProblem = GeocoderProblem(
			problem.id!!,
			problem.name,
			serde.fromJson(problem.points.data())
		)
		val suggestedCoordinate = solution.get(GEOCODER_SOLUTION.SUGGESTEDCOORDINATE)
			?.let { serde.fromJson(it.data()) }
			?: Coordinate.EMPTY

		val status = solverRequest.get(GEOCODER_SOLVER_REQUEST.STATUS)
			?.let(SolverStatus::valueOf)
			?: SolverStatus.NOT_SOLVED

		val requestKey = solverRequest.get(GEOCODER_SOLVER_REQUEST.REQUEST_KEY)

		return GeocoderSolutionRequest(
			GeocoderSolution(geocoderProblem, suggestedCoordinate),
			status,
			requestKey
		)
	}


	override suspend fun upsertSolution(
		problemId: Long,
		solverStatus: SolverStatus,
		coordinate: Coordinate,
		clear: Boolean,
		uuid: UUID
	): GeocoderSolutionRequest {
		val now = Instant.now()
		val jsonCoordinate = if (clear) JSON.json("[]") else JSON.json(serde.toJson(coordinate))

		return dsl.transactionCoroutine { trx ->
			trx.dsl()
				.update(GEOCODER_SOLVER_REQUEST)
				.set(GEOCODER_SOLVER_REQUEST.STATUS, if (clear) SolverStatus.NOT_SOLVED.name else solverStatus.name)
				.set(GEOCODER_SOLVER_REQUEST.UPDATED_AT, now)
				.where(GEOCODER_SOLVER_REQUEST.REQUEST_KEY.eq(uuid))
				.awaitSingle()

			val (numSolutions) = dsl.selectCount().from(GEOCODER_SOLUTION)
				.where(GEOCODER_SOLUTION.GEOCODER_PROBLEM_ID.eq(problemId))
				.awaitSingle()

			if (numSolutions == 0) {
				trx.dsl()
					.insertInto(GEOCODER_SOLUTION)
					.set(GEOCODER_SOLUTION.GEOCODER_PROBLEM_ID, problemId)
					.set(GEOCODER_SOLUTION.SUGGESTEDCOORDINATE, jsonCoordinate)
					.set(GEOCODER_SOLUTION.CREATED_AT, now)
					.set(GEOCODER_SOLUTION.UPDATED_AT, now)
					.awaitSingle()
			} else {
				trx.dsl()
					.update(GEOCODER_SOLUTION)
					.set(GEOCODER_SOLUTION.SUGGESTEDCOORDINATE, jsonCoordinate)
					.set(GEOCODER_SOLUTION.UPDATED_AT, now)
					.where(GEOCODER_SOLUTION.GEOCODER_PROBLEM_ID.eq(problemId))
					.awaitSingle()
			}

			if (!clear) {
				trx.dsl()
					.insertInto(GEOCODER_SOLUTION_REQUEST)
					.set(GEOCODER_SOLUTION_REQUEST.REQUEST_KEY, uuid)
					.set(GEOCODER_SOLUTION_REQUEST.PROBLEM_ID, problemId)
					.set(GEOCODER_SOLUTION_REQUEST.STATUS, solverStatus.name)
					.set(GEOCODER_SOLUTION_REQUEST.RESULT, jsonCoordinate)
					.set(GEOCODER_SOLUTION_REQUEST.CREATED_AT, now)
					.set(GEOCODER_SOLUTION_REQUEST.UPDATED_AT, now)
					.awaitSingle()
			}

			currentSolutionRequestQuery(trx.dsl(), problemId)
				.awaitSingle()
				.let(::convertRecordToSolutionRequest)
		}
	}

}
