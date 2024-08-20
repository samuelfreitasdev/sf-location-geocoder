package br.com.sf.geocoder.adapters.database

import br.com.sf.geocoder.core.domain.model.GeocoderRequest
import br.com.sf.geocoder.core.domain.model.SolverStatus
import br.com.sf.geocoder.core.domain.ports.repo.GeocoderRequestPort
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactive.awaitSingle
import org.jooq.DSLContext
import org.jooq.generated.tables.references.GEOCODER_SOLVER_REQUEST
import java.time.Duration
import java.time.Instant
import java.util.UUID

class GeocoderRequestJooqAdapter(
	private val dsl: DSLContext,
) : GeocoderRequestPort {
	override suspend fun refreshSolverRequests(timeout: Duration): Int {
		return dsl
			.update(GEOCODER_SOLVER_REQUEST)
			.set(GEOCODER_SOLVER_REQUEST.STATUS, SolverStatus.TERMINATED.name)
			.where(GEOCODER_SOLVER_REQUEST.STATUS.eq(SolverStatus.RUNNING.name))
			.and(GEOCODER_SOLVER_REQUEST.UPDATED_AT.lt(Instant.now() - timeout))
			.awaitSingle()
	}

	override suspend fun createRequest(request: GeocoderRequest): GeocoderRequest? {
		val (numEnqueued) =
			dsl.selectCount().from(GEOCODER_SOLVER_REQUEST)
				.where(GEOCODER_SOLVER_REQUEST.PROBLEM_ID.eq(request.problemId))
				.and(GEOCODER_SOLVER_REQUEST.STATUS.`in`(SolverStatus.ENQUEUED.name, SolverStatus.RUNNING.name))
				.awaitSingle()

// 		if (numEnqueued > 0) return null

		val now = Instant.now()
		val result =
			dsl.insertInto(GEOCODER_SOLVER_REQUEST)
				.set(GEOCODER_SOLVER_REQUEST.REQUEST_KEY, request.requestKey)
				.set(GEOCODER_SOLVER_REQUEST.PROBLEM_ID, request.problemId)
				.set(GEOCODER_SOLVER_REQUEST.SOLVER, request.solver)
				.set(GEOCODER_SOLVER_REQUEST.STATUS, request.status.name)
				.set(GEOCODER_SOLVER_REQUEST.CREATED_AT, now)
				.set(GEOCODER_SOLVER_REQUEST.UPDATED_AT, now)
				.returning()
				.awaitFirstOrNull()

		return request.takeIf { result != null }
	}

	override suspend fun currentSolverRequest(problemId: Long): GeocoderRequest? {
		return dsl.selectFrom(GEOCODER_SOLVER_REQUEST)
			.where(GEOCODER_SOLVER_REQUEST.PROBLEM_ID.eq(problemId))
			.orderBy(GEOCODER_SOLVER_REQUEST.UPDATED_AT.desc())
			.limit(1)
			.awaitFirstOrNull()
			?.let {
				GeocoderRequest(
					requestKey = it.requestKey,
					problemId = it.problemId,
					solver = it.solver,
					status = SolverStatus.valueOf(it.status),
				)
			}
	}

	override suspend fun currentSolverRequest(solverKey: UUID): GeocoderRequest? {
		return dsl.selectFrom(GEOCODER_SOLVER_REQUEST)
			.where(GEOCODER_SOLVER_REQUEST.REQUEST_KEY.eq(solverKey))
			.orderBy(GEOCODER_SOLVER_REQUEST.UPDATED_AT.desc())
			.limit(1)
			.awaitFirstOrNull()
			?.let {
				GeocoderRequest(
					requestKey = it.requestKey,
					problemId = it.problemId,
					solver = it.solver,
					status = SolverStatus.valueOf(it.status),
				)
			}
	}

	override fun requestsByProblemIdAndSolverName(
		problemId: Long,
		solverName: String,
	): Flow<GeocoderRequest> {
		return dsl.selectFrom(GEOCODER_SOLVER_REQUEST)
			.where(GEOCODER_SOLVER_REQUEST.PROBLEM_ID.eq(problemId))
			.and(GEOCODER_SOLVER_REQUEST.SOLVER.eq(solverName))
			.orderBy(GEOCODER_SOLVER_REQUEST.CREATED_AT)
			.asFlow()
			.map {
				GeocoderRequest(
					requestKey = it.requestKey,
					problemId = it.problemId,
					solver = it.solver,
					status = SolverStatus.valueOf(it.status),
				)
			}
	}
}
