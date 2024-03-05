package br.com.sf.geocoder.scheduler

//@Component
//@EnableScheduling
//@EnableSchedulerLock(defaultLockAtMostFor = "24h")
//class SolverRequestScheduler(
//	@Value("\${solver.termination.time-limit}") private val timeLimit: Duration,
//	private val geocoderRequestPort: GeocoderRequestPort
//) {
//
////	@Scheduled(fixedDelay = 5, timeUnit = TimeUnit.MINUTES)
////	@SchedulerLock(name = "refreshAbandonedSolverRequests", lockAtLeastFor = "5m")
////	fun refreshAbandonedSolverRequests() {
////		runBlocking {
////			val numUpdates = geocoderRequestPort.refreshSolverRequests(timeLimit)
////			logger.info { "Refreshed $numUpdates abandoned SolverRequests" }
////		}
////	}
//
//	companion object {
//		private val logger = KotlinLogging.logger {}
//	}
//
//}
