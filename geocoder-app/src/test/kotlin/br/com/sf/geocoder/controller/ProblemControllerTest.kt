package br.com.sf.geocoder.controller

import br.com.sf.geocoder.core.domain.model.Coordinate
import br.com.sf.geocoder.core.domain.model.GeocoderProblem
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.returnResult
import org.testcontainers.containers.PostgreSQLContainer
import java.util.Collections.singletonList

// @Testcontainers
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProblemControllerTest {
	companion object {
		private val postgres: PostgreSQLContainer<*> =
			PostgreSQLContainer<Nothing>("postgres:15-alpine")
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

	@Test
	fun create() {
		val problem = `given a problem`()

		val problemCreated = `try to submit the problem`(problem)
		`try to get the problem`(problemCreated)
		`try to delete the problem`(problemCreated)
	}

	private fun `given a problem`(): GeocoderProblem {
		return GeocoderProblem(
			0L,
			"Teste",
			singletonList(
				Coordinate(1.0, 1.0),
			),
		)
	}

	private fun `try to submit the problem`(problem: GeocoderProblem): GeocoderProblem {
		// Creating problem
		return webTestClient.post()
			.uri("/api/problems")
			.bodyValue(problem)
			.exchange()
			.expectStatus().isCreated
			.returnResult<GeocoderProblem>()
			.responseBody
			.blockFirst()!!
	}

	private fun `try to get the problem`(problem: GeocoderProblem) {
		// Getting problem
		webTestClient.get()
			.uri("/api/problems/${problem.id}")
			.exchange()
			.expectStatus().isOk
			.expectBody(problem.javaClass)
			.isEqualTo(problem)
	}

	private fun `try to delete the problem`(problem: GeocoderProblem) {
		// Deleting problem
		webTestClient.delete()
			.uri("/api/problems/${problem.id}")
			.exchange()
			.expectStatus().isOk
	}
}
