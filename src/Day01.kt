data class AccumulatorA(
    val previous: Int?,
    val increases: Int
)

data class AccumulatorB(
    val previous: List<Int>,
    val increases: Int
)

fun main() {
    val input = readInput("Day01").map { it.toInt() }

    val part1Result = input
        .fold(AccumulatorA(null, 0)) { acc, next ->
            if (acc.previous == null) {
                acc.copy(previous = next)
            } else {
                acc.copy(
                    previous = next,
                    increases = if (acc.previous < next) acc.increases + 1 else acc.increases
                )
            }
        }

    val part2Result = input
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

    println("Part 1 result: ${part1Result.increases}")
    println("Part 2 result: ${part2Result.increases}")
}
