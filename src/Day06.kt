import kotlin.collections.Map

typealias DaysToReproduce = Int
typealias LanternfishPopulation = Map<DaysToReproduce, Long>

private object Day06 {
    private fun parseInput(filename: String): LanternfishPopulation {
        val input = readInput(filename).flatMap { it.split(",") }.map { it.toInt() }
        return (0..8).associateWith { days -> input.filter { days == it }.size.toLong() }
    }

    private fun reproduceLanternfish(population: LanternfishPopulation): LanternfishPopulation =
        population.mapValues {
            when (it.key) {
                8 -> population[0]!!
                6 -> population[0]!! + population[7]!!
                else -> population[it.key + 1]!!
            }
        }

    private fun countPopulation(population: LanternfishPopulation): Long =
        population.values.sum()

    private fun countPopulationAfterDays(population: LanternfishPopulation, daysToSimulate: Int): Long {
        return if (daysToSimulate <= 0) {
            countPopulation(population)
        } else {
            countPopulationAfterDays(reproduceLanternfish(population), daysToSimulate - 1)
        }
    }

    fun solvePart1(filename: String): TimeMeasureResult<Long> {
        val initialPopulation = parseInput(filename)
        return measureTimeInMillis { countPopulationAfterDays(initialPopulation, 80) }
    }

    fun solvePart2(filename: String): TimeMeasureResult<Long> {
        val initialPopulation = parseInput(filename)
        return measureTimeInMillis { countPopulationAfterDays(initialPopulation, 256) }
    }
}

fun main() {
    check(Day06.solvePart1("Day06_test").result == 5934L)
    val part1Solution = Day06.solvePart1("Day06")
    println("Part 2 solution: ${part1Solution.result} [${part1Solution.time}ms]")

    check(Day06.solvePart2("Day06_test").result == 26984457539L)
    val part2Solution = Day06.solvePart2("Day06")
    println("Part 2 solution: ${part2Solution.result} [${part2Solution.time}ms]")
}
