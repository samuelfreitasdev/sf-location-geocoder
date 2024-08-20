package br.com.sf.geocoder.adapters.database

import br.com.sf.geocoder.core.domain.model.GeocoderProblem
import br.com.sf.geocoder.core.domain.model.GeocoderSummary
import br.com.sf.geocoder.core.domain.model.SolverStatus
import br.com.sf.geocoder.core.domain.ports.repo.GeocoderProblemPort
import br.com.sf.geocoder.core.serialization.Serde
import br.com.sf.geocoder.core.serialization.fromJson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactive.awaitSingle
import org.jooq.DSLContext
import org.jooq.JSON
import org.jooq.generated.tables.records.GeocoderProblemRecord
import org.jooq.generated.tables.references.GEOCODER_PROBLEM
import org.jooq.generated.tables.references.GEOCODER_SOLVER_REQUEST
import org.jooq.impl.DSL.sum
import org.jooq.impl.DSL.`when`
import org.jooq.kotlin.coroutines.transactionCoroutine
import java.time.Instant
import org.jooq.impl.DSL.count as dslCount

class GeocoderProblemJooqAdapter(
	private val dsl: DSLContext,
	private val mapper: Serde,
) : GeocoderProblemPort {
	override fun findAll(
		query: String,
		offset: Int,
		limit: Int,
	): Flow<GeocoderSummary> {
		val sumStatuses = { status: SolverStatus ->
			sum(`when`(GEOCODER_SOLVER_REQUEST.STATUS.eq(status.name), 1).otherwise(0)).cast(Int::class.java)
		}

		return dsl
			.select(
				GEOCODER_PROBLEM,
				dslCount(GEOCODER_SOLVER_REQUEST),
				sumStatuses(SolverStatus.ENQUEUED),
				sumStatuses(SolverStatus.RUNNING),
				sumStatuses(SolverStatus.TERMINATED),
				sumStatuses(SolverStatus.NOT_SOLVED),
			)
			.from(GEOCODER_PROBLEM)
			.leftJoin(GEOCODER_SOLVER_REQUEST).on(GEOCODER_SOLVER_REQUEST.PROBLEM_ID.eq(GEOCODER_PROBLEM.ID))
			.where(GEOCODER_PROBLEM.NAME.likeIgnoreCase("${query.trim()}%"))
			.groupBy(GEOCODER_PROBLEM)
			.limit(offset, limit).asFlow().map { (p, t, e, r, f, c) ->
				toProblem(p).let {
					GeocoderSummary(
						id = it.id,
						name = it.name,
						nLocations = e,
						numEnqueuedRequests = r,
						numRunningRequests = f,
						numTerminatedRequests = c,
						numNotSolvedRequests = t,
						numSolverRequests = 0,
					)
				}
			}
	}

	override suspend fun count(query: String): Long {
		val (total) =
			dsl.selectCount()
				.from(GEOCODER_PROBLEM)
				.where(GEOCODER_PROBLEM.NAME.likeIgnoreCase("${query.trim()}%"))
				.awaitSingle()
		return total.toLong()
	}

	override suspend fun getById(problemId: Long): GeocoderProblem? {
		return dsl.selectFrom(GEOCODER_PROBLEM)
			.where(GEOCODER_PROBLEM.ID.eq(problemId))
			.awaitFirstOrNull()
			?.let(::toProblem)
	}

	override suspend fun create(problem: GeocoderProblem): GeocoderProblem {
		val now = Instant.now()

		dsl.transactionCoroutine { trx ->
			trx.dsl()
				.insertInto(GEOCODER_PROBLEM)
				.set(GEOCODER_PROBLEM.NAME, problem.name)
				.set(GEOCODER_PROBLEM.POINTS, JSON.json(mapper.toJson(problem.points)))
				.set(GEOCODER_PROBLEM.CREATED_AT, now)
				.set(GEOCODER_PROBLEM.UPDATED_AT, now)
				.returning()
				.awaitSingle()
		}
			.let {
				return toProblem(it)
			}
	}

	override suspend fun deleteById(problemId: Long) {
		dsl.deleteFrom(GEOCODER_PROBLEM)
			.where(GEOCODER_PROBLEM.ID.eq(problemId))
			.awaitFirstOrNull()
	}

	override suspend fun update(
		id: Long,
		problem: GeocoderProblem,
	) {
		val now = Instant.now()

		dsl.update(GEOCODER_PROBLEM)
			.set(GEOCODER_PROBLEM.NAME, problem.name)
			.set(GEOCODER_PROBLEM.POINTS, JSON.json(mapper.toJson(problem.points)))
			.set(GEOCODER_PROBLEM.UPDATED_AT, now)
			.where(GEOCODER_PROBLEM.ID.eq(id))
			.awaitSingle()
	}

	private fun toProblem(p: GeocoderProblemRecord): GeocoderProblem {
		return GeocoderProblem(
			id = p.id!!,
			name = p.name,
			points = mapper.fromJson(p.points.data()),
		)
	}
}
