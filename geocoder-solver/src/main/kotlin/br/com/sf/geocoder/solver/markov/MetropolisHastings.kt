package br.com.sf.geocoder.solver.markov

import br.com.sf.geocoder.core.domain.model.Coordinate
import org.apache.commons.math3.distribution.NormalDistribution
import org.apache.commons.math3.distribution.TDistribution
import org.apache.commons.math3.stat.descriptive.SummaryStatistics
import java.util.*


class MetropolisHastings(
	private val numSamples: Int = 1_000,
) {

	// Define the target distribution (e.g., standard normal distribution)
	private val targetDistribution = NormalDistribution(0.0, 1.0)

	// Define the proposal distribution (t-distribution with degrees of freedom)
	private val proposalDistribution = TDistribution(0.1) // degrees of freedom = 0.1

	// Random number generator
	private val random: Random = Random()

	fun solve(
		value: List<Coordinate>
	): Coordinate {

		val latitudes = value.map { it.lat }
			.toDoubleArray()
		val longitudes = value.map { it.lng }
			.toDoubleArray()

		return Coordinate(
			lat = solve(latitudes),
			lng = solve(longitudes)
		)
	}

	private fun solve(
		value: DoubleArray
	): Double {

		val totalIterations = numSamples

		val statistics = SummaryStatistics()

		for (i in 0 until totalIterations) {
			for (currentSample in value) {
				// Generate candidate sample from proposal distribution
				val candidateSample = currentSample + proposalDistribution.sample()

				// Calculate acceptance ratio
				val acceptanceRatio =
					targetDistribution.density(candidateSample) / targetDistribution.density(currentSample)

				// Accept or reject the candidate sample
				if (random.nextDouble() < acceptanceRatio) {
//					currentSample = candidateSample
					statistics.addValue(currentSample)
				}
			}
		}

		println(statistics)

		return statistics.mean
	}

	/*
	private fun solve(
			value: DoubleArray
		): Double {

			val statisticsBefore = SummaryStatistics()
			value.onEach(statisticsBefore::addValue)
			println(statisticsBefore)

			val distribution = NormalDistribution(statisticsBefore.mean, 0.1)

			TDistribution

	//		distribution.sample()


			return 12.0
		}

	*/
}
