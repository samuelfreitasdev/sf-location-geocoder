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
import org.testcontainers.containers.PostgreSQLContainer
import java.util.Collections.singletonList
import kotlin.random.Random


@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProblemControllerTest {

	companion object {
		val postgres: PostgreSQLContainer<*> = PostgreSQLContainer<Nothing>("postgres:15-alpine")
			.apply {
				withDatabaseName("sf-location-geocoder")
				withUsername("postgres")
				withPassword("postgres")
			}

		@JvmStatic
		@BeforeAll
		fun beforeAll(): Unit {
			postgres.start()
		}

		@JvmStatic
		@AfterAll
		fun afterAll(): Unit {
			postgres.stop()
		}
	}


	@Autowired
	lateinit var webTestClient: WebTestClient

	@Test
	fun create() {
		val problem = `given a problem`()

		`try to submit the problem`(problem)
		`try to get the problem`(problem)
		`try to delete the problem`(problem)
	}

	private fun `given a problem`(): GeocoderProblem {
		return GeocoderProblem(
			Random.nextLong(),
			"Teste",
			singletonList(
				Coordinate(1.0, 1.0)
			)
		)
	}

	private fun `try to submit the problem`(problem: GeocoderProblem) {
		// Creating problem
		webTestClient.post()
			.uri("/api/problems")
			.bodyValue(problem)
			.exchange()
			.expectStatus().isCreated
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
