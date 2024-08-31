package br.com.sf.geocoder.solver.fake

import br.com.sf.geocoder.core.domain.model.GeocoderSolution
import br.com.sf.geocoder.core.solver.SolverConfig
import br.com.sf.geocoder.core.solver.spi.Solver
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FakeSolver : Solver() {

	override val name: String = "FakeSolver"

	override fun solveFlow(initialSolution: GeocoderSolution, config: SolverConfig): Flow<GeocoderSolution> {
		return callbackFlow {
			val coordinate = initialSolution.problem.points.first()
			val result = GeocoderSolution(initialSolution.problem, coordinate)
			send(result)
			close()
		}
	}

}
