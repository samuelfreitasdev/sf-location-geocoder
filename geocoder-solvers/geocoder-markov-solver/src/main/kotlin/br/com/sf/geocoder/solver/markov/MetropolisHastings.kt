package br.com.sf.geocoder.solver.markov

import br.com.sf.geocoder.core.domain.model.Coordinate
import br.com.sf.geocoder.core.domain.model.SuggestedCoordinate
import org.apache.commons.math3.distribution.NormalDistribution
import org.apache.commons.math3.distribution.TDistribution
import org.apache.commons.math3.stat.descriptive.SummaryStatistics
import java.util.*
import kotlin.math.sqrt


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
	): SuggestedCoordinate {

		val latitudes = value.map { it.lat }
			.toDoubleArray()
		val longitudes = value.map { it.lng }
			.toDoubleArray()

		val lat = solve(latitudes)
		val lng = solve(longitudes)
		val confidence: Double = minOf(lat.confidence, lng.confidence)

		return SuggestedCoordinate(
			lat = lat.value,
			lng = lng.value,
			confidence = confidence
		)
	}

	private fun solve(
		value: DoubleArray
	): Result {

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

//		println(statistics)
//		println("Confidence Interval: ${confidenceInterval(statistics)}")

		return Result(
			value = statistics.mean,
			confidence = confidenceInterval(statistics)
		)
	}

	private fun confidenceInterval(
		statistics: SummaryStatistics,
	): Double {
		val numSamples = statistics.n

//		# Calculate the within-chain variance
		val W = statistics.mean

//		# Calculate the between-chain variance
		val B = numSamples * statistics.standardDeviation

//		# Estimate the marginal posterior variance
		val V = ((numSamples - 1).toDouble() / numSamples.toDouble()) * W + (1 / numSamples) * B

//		# Calculate R-hat
		val r_hat = sqrt(V / W)

		return r_hat
	}

	data class Result(
		val value: Double,
		val confidence: Double,
	)
}
