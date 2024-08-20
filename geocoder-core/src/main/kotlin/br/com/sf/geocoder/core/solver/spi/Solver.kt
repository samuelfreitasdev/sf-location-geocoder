package br.com.sf.geocoder.core.solver.spi

import br.com.sf.geocoder.core.domain.model.GeocoderSolution
import br.com.sf.geocoder.core.solver.SolverConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filterNot
import java.util.ServiceLoader

abstract class Solver {
	companion object {
		fun getNamedSolvers(): Map<String, Solver> {
			val solverFactories = mutableMapOf<String, Solver>()
			ServiceLoader.load(Solver::class.java)
				.iterator()
				.forEachRemaining { solverFactories[it.name] = it }
			return solverFactories
		}

		fun getSolverByName(solverName: String): Solver =
			getNamedSolvers()[solverName]
				?: throw IllegalArgumentException("No solver $solverName was found")
	}

	abstract val name: String

	fun solve(
		initialSolution: GeocoderSolution,
		config: SolverConfig,
	): Flow<GeocoderSolution> =
		solveFlow(initialSolution, config)
			.filterNot { it.isEmpty() }
			.distinctUntilChangedBy { it.suggestedCoordinate }

	protected abstract fun solveFlow(
		initialSolution: GeocoderSolution,
		config: SolverConfig,
	): Flow<GeocoderSolution>
}
