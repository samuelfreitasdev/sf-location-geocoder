package br.com.sf.geocoder.solver.markov

import br.com.sf.geocoder.core.domain.model.GeocoderSolution
import br.com.sf.geocoder.core.solver.SolverConfig
import br.com.sf.geocoder.core.solver.spi.Solver
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class MarkovSolver : Solver() {
	override val name: String = NAME

	override fun solveFlow(
		initialSolution: GeocoderSolution,
		config: SolverConfig,
	): Flow<GeocoderSolution> {
		return callbackFlow {
			val coordinate =
				MetropolisHastings().solve(
					initialSolution.problem.points,
				)

			val result =
				GeocoderSolution(
					initialSolution.problem,
					coordinate,
				)

			send(result)
			close()
		}
	}

	companion object {
		const val NAME = "MarkovSolver"
	}
}
