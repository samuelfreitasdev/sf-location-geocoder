package br.com.sf.geocoder.config.rest

import br.com.sf.geocoder.adapters.database.GeocoderProblemJooqAdapter
import br.com.sf.geocoder.adapters.database.GeocoderRequestJooqAdapter
import br.com.sf.geocoder.adapters.database.GeocoderSolutionJooqAdapter
import br.com.sf.geocoder.core.domain.ports.repo.GeocoderProblemPort
import br.com.sf.geocoder.core.domain.ports.repo.GeocoderRequestPort
import br.com.sf.geocoder.core.domain.ports.repo.GeocoderSolutionPort
import br.com.sf.geocoder.core.serialization.Serde
import org.jooq.DSLContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RepositoryConfig {
	@Bean
	fun geocoderProblemRepository(
		serde: Serde,
		dslContext: DSLContext,
	): GeocoderProblemPort = GeocoderProblemJooqAdapter(dslContext, serde)

	@Bean
	fun geocoderSolutionRepository(
		serde: Serde,
		dslContext: DSLContext,
	): GeocoderSolutionPort = GeocoderSolutionJooqAdapter(serde, dslContext)

	@Bean
	fun geocoderSolverRequestRepository(dslContext: DSLContext): GeocoderRequestPort = GeocoderRequestJooqAdapter(dslContext)
}
