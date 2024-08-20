package br.com.sf.geocoder.config.solver

import br.com.sf.geocoder.core.solver.SolverManager
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Duration

@Configuration
class SolverManagerConfig {
	@Bean(destroyMethod = "destroy")
	fun vrpSolverManager(
		@Value("\${solver.termination.time-limit:60}") timeLimit: Duration,
	): SolverManager {
		return SolverManager(timeLimit)
	}
}
