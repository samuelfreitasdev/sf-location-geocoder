package br.com.sf.geocoder.solver

import org.junit.jupiter.api.Test
import java.util.*
import kotlin.math.pow


class SimulatedAnnealing {

	// Initial and final temperature
	var T: Double = 1.0

	// Simulated Annealing parameters
	// Temperature at which iteration terminates
	val Tmin: Double = .0001

	// Decrease in temperature
	val alpha: Double = 0.9

	// Number of iterations of annealing
	// before decreasing temperature
	val numIterations: Int = 100


	// Locational parameters
	// Target array is discretized as M*N grid
	val M: Int = 5
	val N: Int = 5

	// Number of objects desired
	val k: Int = 5


	@Test
	fun run() {
		// Problem: place k objects in an MxN target
		// plane yielding minimal cost according to
		// defined objective function

		// Set of all possible candidate locations

		val sourceArray = Array(M) {
			arrayOfNulls<String>(
				N
			)
		}

		// Global minimum
		var min = Solution(Double.MAX_VALUE, null)

		// Generates random initial candidate solution
		// before annealing process
		var currentSol = genRandSol()

		// Continues annealing until reaching minimum
		// temperature
		while (T > Tmin) {
			for (i in 0 until numIterations) {
				// Reassigns global minimum accordingly

				if (currentSol.CVRMSE < min.CVRMSE) {
					min = currentSol
				}

				val newSol = neighbor(currentSol)
				val ap: Double = Math.E.pow((currentSol.CVRMSE - newSol.CVRMSE) / T)
				if (ap > Math.random()) currentSol = newSol
			}

			T *= alpha // Decreases T, cooling phase
		}

		//Returns minimum value based on optimization
		println(min.CVRMSE.toString() + "\n\n")

		for (row in sourceArray) Arrays.fill(row, "X")

		// Displays
		for (`object` in min.config!!) {
			val coord = indexToPoints(`object`)
			sourceArray[coord[0]][coord[1]] = "-"
		}

		// Displays optimal location
		for (row in sourceArray) println(row.contentToString())
	}

	// Given current configuration, returns "neighboring"
	// configuration (i.e. very similar)
	// integer of k points each in range [0, n)
	/* Different neighbor selection strategies:
        * Move all points 0 or 1 units in a random direction
        * Shift input elements randomly
        * Swap random elements in input sequence
        * Permute input sequence
        * Partition input sequence into a random number
          of segments and permute segments   */
	fun neighbor(currentSol: Solution): Solution {
		// Slight perturbation to the current solution
		// to avoid getting stuck in local minimas

		// Returning for the sake of compilation

		return currentSol
	}

	// Generates random solution via modified Fisher-Yates
	// shuffle for first k elements
	// Pseudorandomly selects k integers from the interval
	// [0, n-1]
	fun genRandSol(): Solution {
		// Instantiating for the sake of compilation

		val a = intArrayOf(1, 2, 3, 4, 5)

		// Returning for the sake of compilation
		return Solution(-1.0, a)
	}


	// Complexity is O(M*N*k), asymptotically tight
	fun cost(inputConfiguration: IntArray?): Double {
		// Given specific configuration, return object
		// solution with assigned cost

		return (-1).toDouble() //Returning for the sake of compilation
	}

	// Mapping from [0, M*N] --> [0,M]x[0,N]
	fun indexToPoints(index: Int): IntArray {
		val points = intArrayOf(index % M, index / M)
		return points
	}

	// Class solution, bundling configuration with error
	class Solution(// function value of instance of solution;
		// using coefficient of variance root mean
		// squared error
		var CVRMSE: Double, // Configuration array
		var config: IntArray?
	) {
		init {
			config = config
		}
	}
}
