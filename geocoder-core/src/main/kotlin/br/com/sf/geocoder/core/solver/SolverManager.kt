package br.com.sf.geocoder.core.solver

import br.com.sf.geocoder.core.domain.messages.SolutionRequestCommand
import br.com.sf.geocoder.core.domain.model.GeocoderSolution
import br.com.sf.geocoder.core.domain.model.GeocoderSolutionRequest
import br.com.sf.geocoder.core.domain.model.SolverStatus
import br.com.sf.geocoder.core.solver.spi.Solver
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import java.time.Duration
import java.util.*
import java.util.concurrent.CancellationException
import java.util.concurrent.ConcurrentHashMap

private val logger = KotlinLogging.logger {}

class SolverManager(timeLimit: Duration) {

	private class UserCancellationException(val clear: Boolean) : CancellationException("User cancellation command.")

	private val supervisorJob = SupervisorJob()
	private val managerScope = CoroutineScope(supervisorJob + Dispatchers.Default)
	private val solverKeys = ConcurrentHashMap<UUID, Job>()
	private val blackListedKeys = ConcurrentHashMap.newKeySet<UUID>()
	private val solverConfig = SolverConfig(timeLimit)

	fun solve(
		solverKey: UUID,
		solution: GeocoderSolution,
		solverName: String
	): Flow<SolutionRequestCommand> {

		if (blackListedKeys.remove(solverKey)) {
			val cmd = wrapCommand(GeocoderSolutionRequest(solution, SolverStatus.TERMINATED, solverKey))
			return flowOf(cmd)
		}

		if (solverKeys.containsKey(solverKey)) return emptyFlow()

		val channel = Channel<SolutionRequestCommand>()
		var bestSolution = solution

		solverKeys[solverKey] = Solver
			.getSolverByName(solverName)
			.solve(solution, solverConfig)
			.onEach {
				bestSolution = it
				logger.info { "onEach: $solverKey | ${bestSolution.suggestedCoordinate} ($solverName)" }
				channel.send(wrapCommand(GeocoderSolutionRequest(it, SolverStatus.RUNNING, solverKey)))
			}
			.onCompletion { ex ->
				logger.info { "onEnd: $solverKey | ${bestSolution.suggestedCoordinate} ($solverName)" }
				val solRequest = GeocoderSolutionRequest(bestSolution, SolverStatus.TERMINATED, solverKey)
				val shouldClear = ex is UserCancellationException && ex.clear
				channel.send(wrapCommand(solRequest, shouldClear))
				channel.close()
			}
			.launchIn(managerScope)

		return channel.receiveAsFlow()
	}

	suspend fun cancelSolver(solverKey: UUID, currentStatus: SolverStatus, clear: Boolean) {
		if (currentStatus == SolverStatus.ENQUEUED) blackListedKeys.add(solverKey)
		solverKeys.remove(solverKey)?.let {
			it.cancel(UserCancellationException(clear))
			it.join()
		}
	}

	fun destroy() {
		supervisorJob.cancel()
	}

	private fun wrapCommand(solutionRequest: GeocoderSolutionRequest, clear: Boolean = false) =
		SolutionRequestCommand(solutionRequest, clear)

}
