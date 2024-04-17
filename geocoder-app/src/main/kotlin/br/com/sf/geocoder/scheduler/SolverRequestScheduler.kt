package br.com.sf.geocoder.scheduler

import br.com.sf.geocoder.core.domain.ports.repo.GeocoderRequestPort
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.runBlocking
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.Duration
import java.util.concurrent.TimeUnit

@Component
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "5m")
class SolverRequestScheduler(
	@Value("\${solver.termination.time-limit}") private val timeLimit: Duration,
	private val geocoderRequestPort: GeocoderRequestPort
) {

	@Scheduled(fixedDelay = 5, timeUnit = TimeUnit.MINUTES)
	@SchedulerLock(name = "refreshAbandonedSolverRequests", lockAtLeastFor = "5m")
	fun refreshAbandonedSolverRequests() {
		runBlocking {
			val numUpdates = geocoderRequestPort.refreshSolverRequests(timeLimit)
			logger.info { "Refreshed $numUpdates abandoned SolverRequests" }
		}
	}

	companion object {
		private val logger = KotlinLogging.logger {}
	}

}
