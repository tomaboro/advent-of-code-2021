private object Day01 {
    fun parseDay01Input(filename: String): List<Int> =
        readInput(filename).map { it.toInt() }

    data class AccumulatorA(
        val previous: Int?,
        val increases: Int
    )

    fun solvePart1(filename: String): Int {
        val input = parseDay01Input(filename)
        val resultAccumulator = input.fold(AccumulatorA(null, 0)) { acc, next ->
            if (acc.previous == null) {
                acc.copy(previous = next)
            } else {
                acc.copy(
                    previous = next,
                    increases = if (acc.previous < next) acc.increases + 1 else acc.increases
                )
            }
        }

        return resultAccumulator.increases
    }

    data class AccumulatorB(
        val previous: List<Int>,
        val increases: Int
    )

    fun solvePart2(filename: String): Int {
        val input = parseDay01Input(filename)
        val resultAccumulator = input
            .fold(AccumulatorB(emptyList(), 0)) { acc, next ->
                if (acc.previous.size < 3) {
                    val nextList = acc.previous.plus(next)
                    acc.copy(previous = nextList)
                } else {
                    val nextList = acc.previous.drop(1).plus(next)
                    acc.copy(
                        previous = nextList,
                        increases =
                        if (acc.previous.sum() < nextList.sum()) acc.increases + 1 else acc.increases
                    )
                }
            }
        return resultAccumulator.increases
    }
}

fun main() {
    check(Day01.solvePart1("Day01_test") == 7)
    println("Part 1 solution: ${Day01.solvePart1("Day01")}")
    check(Day01.solvePart2("Day01_test") == 5)
    println("Part 2 solution: ${Day01.solvePart1("Day01")}")
}
