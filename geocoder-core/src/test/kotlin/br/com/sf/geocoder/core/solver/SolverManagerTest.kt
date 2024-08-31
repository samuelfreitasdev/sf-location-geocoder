package br.com.sf.geocoder.core.solver

import br.com.sf.geocoder.core.domain.model.Coordinate
import br.com.sf.geocoder.core.domain.model.GeocoderProblem
import br.com.sf.geocoder.core.domain.model.GeocoderSolution
import br.com.sf.geocoder.core.domain.model.SolverStatus
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Duration
import java.util.UUID

class SolverManagerTest {

	private lateinit var solverManager: SolverManager
	private lateinit var uuid: UUID
	private val solverName = "FakeSolver"

	private lateinit var problem: GeocoderProblem
	private lateinit var solution: GeocoderSolution

	@BeforeEach
	fun setUp() {
		solverManager = SolverManager(Duration.ofSeconds(2))
		uuid = UUID.randomUUID()

		problem = GeocoderProblem(
			1,
			"Problem",
			listOf(
				Coordinate(1.0, 1.0),
				Coordinate(2.0, 2.0),
			)
		)
		solution = GeocoderSolution(problem, Coordinate(1.5, 1.5))
	}

	@Test
	fun `test solve when solver key is blacklisted`() = runTest {
		solverManager.cancelSolver(uuid, SolverStatus.ENQUEUED, false)

		val resultFlow = solverManager.solve(uuid, solution, solverName)
		val result = resultFlow.first()

		assertEquals(
			result.solutionRequest.status,
			SolverStatus.TERMINATED,
			"Should return SolverStatus as TERMINATED when solver key is blacklisted"
		)
	}

	@Test
	fun `test solve when solver key already exists`() = runTest {
		solverManager.solve(uuid, solution, solverName)

		val result = solverManager.solve(uuid, solution, solverName).firstOrNull()
		assertNull(result, "Should return empty flow when solver key already exists")
	}

	@Test
	fun `test cancelSolver before solve`() = runTest {
		solverManager.cancelSolver(uuid, SolverStatus.ENQUEUED, false)

		val resultFlow = solverManager.solve(uuid, solution, solverName)
		val result = resultFlow.first()

		assertEquals(
			result.solutionRequest.status,
			SolverStatus.TERMINATED,
			"Should return SolverStatus as TERMINATED when cancelSolver is called before solve"
		)
	}
}
