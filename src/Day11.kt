private object Day11 {
    private data class DumboOctopus(
        val energyLevel: Int,
        val iterationFlashed: Int? = null
    )

    private fun parseInput(filename: String): List<List<DumboOctopus>> =
        readInput(filename).map { line ->
            line.split("").filter { it.isNotEmpty() }.map {
                DumboOctopus(energyLevel = it.toInt())
            }
        }

    private fun prepareNextTurnSimulation(octopuses: List<List<DumboOctopus>>): List<List<DumboOctopus>> =
        octopuses.map { octopusesRow ->
            octopusesRow.map {
                it.copy(
                    energyLevel = if (it.energyLevel > 9) 0 else it.energyLevel,
                    iterationFlashed = null
                )
            }
        }

    private fun simulateTimeEnergyIncrease(octopuses: List<List<DumboOctopus>>): List<List<DumboOctopus>> =
        octopuses.map { octopusesRow ->
            octopusesRow.map {
                it.copy(
                    energyLevel = it.energyLevel + 1,
                    iterationFlashed = if (it.energyLevel == 9) 0 else null
                )
            }
        }

    private fun simulateRecharges(octopuses: List<List<DumboOctopus>>, iteration: Int = 1): List<List<DumboOctopus>> {
        val lastIterationFlashes = octopuses.flatten().filter { it.iterationFlashed == iteration - 1 }.size
        if (lastIterationFlashes == 0) {
            return octopuses
        }
        val newOctopuses =
            octopuses.mapIndexed { y, octopusesRow ->
                octopusesRow.mapIndexed { x, octopus ->
                    val flashedNeighbours = listOf(
                        octopuses.getOrNull(y + 1)?.getOrNull(x - 1),
                        octopuses.getOrNull(y + 1)?.getOrNull(x),
                        octopuses.getOrNull(y + 1)?.getOrNull(x + 1),
                        octopuses.getOrNull(y)?.getOrNull(x - 1),
                        octopuses.getOrNull(y)?.getOrNull(x + 1),
                        octopuses.getOrNull(y - 1)?.getOrNull(x - 1),
                        octopuses.getOrNull(y - 1)?.getOrNull(x),
                        octopuses.getOrNull(y - 1)?.getOrNull(x + 1)
                    ).filter { it != null && it.iterationFlashed == iteration - 1 }
                    octopus.copy(
                        energyLevel = octopus.energyLevel + flashedNeighbours.size,
                        iterationFlashed = if (
                            octopus.energyLevel + flashedNeighbours.size > 9 &&
                            octopus.iterationFlashed == null
                        ) iteration
                        else octopus.iterationFlashed
                    )
                }
            }
        return simulateRecharges(newOctopuses, iteration + 1)
    }

    private fun simulateTurn(octopuses: List<List<DumboOctopus>>): List<List<DumboOctopus>> =
        simulateRecharges(simulateTimeEnergyIncrease(octopuses))

    private fun countFlashes(octopuses: List<List<DumboOctopus>>): Int =
        octopuses.flatten().filter { it.energyLevel > 9 }.size

    private fun countFlashes(octopuses: List<List<DumboOctopus>>, turns: Int, flashes: Int = 0): Int {
        if (turns <= 0) {
            return flashes
        }
        val newOctopuses = simulateTurn(octopuses)
        return countFlashes(
            prepareNextTurnSimulation(newOctopuses),
            turns - 1,
            flashes + countFlashes(newOctopuses)
        )
    }

    private fun findSyncTurn(octopuses: List<List<DumboOctopus>>, turn: Int = 1): Int {
        val newOctopuses = simulateTurn(octopuses)
        if (countFlashes(newOctopuses) == newOctopuses.flatten().size) {
            return turn
        }
        return findSyncTurn(
            prepareNextTurnSimulation(newOctopuses),
            turn + 1
        )
    }

    fun solvePart1(filename: String): TimeMeasureResult<Int> {
        val input = parseInput(filename)
        return measureTimeInMillis {
            countFlashes(input, 100)
        }
    }

    fun solvePart2(filename: String): TimeMeasureResult<Int> {
        val input = parseInput(filename)
        return measureTimeInMillis {
            findSyncTurn(input)
        }
    }
}

fun main() {
    check(Day11.solvePart1("Day11_test").result == 1656)
    val part1Solution = Day11.solvePart1("Day11")
    println("Part 1 solution: ${part1Solution.result} [${part1Solution.time}ms]")

    check(Day11.solvePart2("Day11_test").result == 195)
    val part2Solution = Day11.solvePart2("Day11")
    println("Part 2 solution: ${part2Solution.result} [${part2Solution.time}ms]")
}
