package br.com.sf.geocoder.controller

import br.com.sf.geocoder.core.domain.model.Coordinate
import br.com.sf.geocoder.core.domain.model.GeocoderProblem
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.returnResult
import org.testcontainers.containers.PostgreSQLContainer
import java.time.Duration
import java.util.Collections.singletonList

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SolverControllerTest {

	companion object {
		val postgres: PostgreSQLContainer<*> = PostgreSQLContainer<Nothing>("postgres:15-alpine")
			.apply {
				withDatabaseName("sf-location-geocoder")
				withUsername("postgres")
				withPassword("postgres")
			}

		@JvmStatic
		@BeforeAll
		fun beforeAll() {
			postgres.start()
		}

		@JvmStatic
		@AfterAll
		fun afterAll() {
			postgres.stop()
		}
	}

	@Autowired
	lateinit var webTestClient: WebTestClient

	val DEFAULT_PROBLEM = GeocoderProblem(
		0L,
		"Teste",
		singletonList(
			Coordinate(1.0, 1.0)
		)
	)

	lateinit var problemCreated: GeocoderProblem

	@BeforeEach
	fun setUp() {
		problemCreated = `try to submit the problem`(DEFAULT_PROBLEM)
	}

	@AfterEach
	fun teardown() {
		`delete the problem`(problemCreated)
	}

	@Test
	fun solverTest() = runTest {
		`request to start the solver for`(problemCreated)
		`request the problem solution`(problemCreated)
		delay(5000)
	}

	private fun `try to submit the problem`(problem: GeocoderProblem): GeocoderProblem {
		return webTestClient.post()
			.uri("/api/problems")
			.bodyValue(problem)
			.exchange()
			.expectStatus().isCreated
			.returnResult<GeocoderProblem>()
			.responseBody
			.blockFirst()!!
	}

	private fun `request the problem solution`(problem: GeocoderProblem): GeocoderProblem {
		return webTestClient.get()
			.uri("/api/solver/${problem.id}/solution")
			.exchange()
			.expectStatus().isOk
			.returnResult<GeocoderProblem>()
			.responseBody
			.blockFirst()!!
	}

	private fun `delete the problem`(problem: GeocoderProblem) {
		// Deleting problem
		webTestClient.delete()
			.uri("/api/problems/${problem.id}")
			.exchange()
			.expectStatus().isOk
	}

	private fun `request to start the solver for`(problem: GeocoderProblem) {
		webTestClient
			.mutate()
			.responseTimeout(Duration.ofMinutes(5))
			.build()
			.post()
			.uri("/api/solver/${problem.id}/solve/MarkovSolver")
			.exchange()
			.expectStatus().isOk

	}
}
