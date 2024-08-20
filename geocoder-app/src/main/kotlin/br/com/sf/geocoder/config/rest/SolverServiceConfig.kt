package br.com.sf.geocoder.config.rest

import br.com.sf.geocoder.core.domain.ports.events.BroadcastPort
import br.com.sf.geocoder.core.domain.ports.events.SolverEventsPort
import br.com.sf.geocoder.core.domain.ports.repo.GeocoderProblemPort
import br.com.sf.geocoder.core.domain.ports.repo.GeocoderRequestPort
import br.com.sf.geocoder.core.domain.ports.repo.GeocoderSolutionPort
import br.com.sf.geocoder.core.domain.repository.SolverRepository
import br.com.sf.geocoder.core.solver.SolverService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SolverServiceConfig {
	@Bean
	fun solverRepository(
		solverProblemPort: GeocoderProblemPort,
		solverSolutionPort: GeocoderSolutionPort,
		solverRequestPort: GeocoderRequestPort,
	) = SolverRepository(solverProblemPort, solverSolutionPort, solverRequestPort)

	@Bean
	fun solverService(
		solverRepository: SolverRepository,
		solverEventsPort: SolverEventsPort,
		broadcastPort: BroadcastPort,
	): SolverService {
		return SolverService(broadcastPort, solverEventsPort, solverRepository)
	}
}
