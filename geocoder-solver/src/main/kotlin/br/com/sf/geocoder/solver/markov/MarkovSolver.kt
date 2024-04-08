package br.com.sf.geocoder.solver.markov

import br.com.sf.geocoder.core.domain.model.Coordinate
import br.com.sf.geocoder.core.domain.model.GeocoderSolution
import br.com.sf.geocoder.core.solver.SolverConfig
import br.com.sf.geocoder.core.solver.spi.Solver
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.time.Duration

class MarkovSolver : Solver() {

	override val name: String = NAME

	override fun solveFlow(
		initialSolution: GeocoderSolution, config: SolverConfig
	): Flow<GeocoderSolution> {
		return callbackFlow {
			delay(Duration.ofMinutes(1).toMillis())
			val result = GeocoderSolution(
				initialSolution.problem,
				Coordinate(-12.0, -12.0),
			)
			send(result)
			close()
		}
	}

	companion object {
		public const val NAME = "MarkovSolver"
	}

}
