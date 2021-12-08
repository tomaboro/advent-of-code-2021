import kotlin.math.abs

private object Day07 {
    private fun parseInput(filename: String): List<Int> =
        readInput(filename)[0].split(",").map { it.toInt() }

    fun solvePart1(filename: String): TimeMeasureResult<Int> {
        val input = parseInput(filename)
        return measureTimeInMillis {
            val sortedPositions = input.sorted()
            val min = sortedPositions.first()
            val max = sortedPositions.last()
            (min..max).minOf { point ->
                sortedPositions.sumOf { abs(point - it) }
            }
        }
    }

    fun solvePart2(filename: String): TimeMeasureResult<Int> {
        val input = parseInput(filename).sorted()
        return measureTimeInMillis {
            val sortedPositions = input.sorted()
            val min = sortedPositions.first()
            val max = sortedPositions.last()
            (min..max).minOf { point ->
                sortedPositions.sumOf { ((1 + abs(point - it)) * abs(point - it)) / 2 }
            }
        }
    }
}

fun main() {
    check(Day07.solvePart1("Day07_test").result == 37)
    val part1Solution = Day07.solvePart1("Day07")
    println("Part 1 solution: ${part1Solution.result} [${part1Solution.time}ms]")

    check(Day07.solvePart2("Day07_test").result == 168)
    val part2Solution = Day07.solvePart2("Day07")
    println("Part 2 solution: ${part2Solution.result} [${part2Solution.time}ms]")
}
