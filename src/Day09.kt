private object Day09 {
    private data class Point(
        val x: Int,
        val y: Int
    )

    private fun parseInput(filename: String): List<List<Int>> =
        readInput(filename)
            .map { line ->
                line.split("").filter { it.isNotBlank() }.map { it.toInt() }
            }

    fun solvePart1(filename: String): TimeMeasureResult<Int> {
        val input = parseInput(filename)
        return measureTimeInMillis {
            input.flatMapIndexed { y, row ->
                row.mapIndexed { x, point ->
                    val isLocalMin = listOfNotNull(
                        input.getOrNull(y)?.getOrNull(x + 1),
                        input.getOrNull(y)?.getOrNull(x - 1),
                        input.getOrNull(y - 1)?.getOrNull(x),
                        input.getOrNull(y + 1)?.getOrNull(x)
                    ).all { point < it }
                    Pair(point, isLocalMin)
                }
            }
                .filter { it.second }
                .sumOf { it.first + 1 }
        }
    }

    private fun expandBasin(input: List<List<Int>>, basinPoints: Set<Point>, startingPoint: Point): Set<Point> {
        val neighbours = listOf(
            Point(startingPoint.x + 1, startingPoint.y),
            Point(startingPoint.x - 1, startingPoint.y),
            Point(startingPoint.x, startingPoint.y + 1),
            Point(startingPoint.x, startingPoint.y - 1),
        )
        val possibleBasisExpandPoints = neighbours.filter {
            input.getOrNull(it.y)?.getOrNull(it.x) != null && input[it.y][it.x] != 9 && !basinPoints.contains(it)
        }
        return possibleBasisExpandPoints.fold(basinPoints) { acc, point -> expandBasin(input, acc + point, point) }
    }

    fun solvePart2(filename: String): TimeMeasureResult<Int> {
        val input = parseInput(filename)
        return measureTimeInMillis {
            input.foldIndexed(emptyList<Set<Point>>()) { y, acc, row ->
                row.foldIndexed(acc) { x, basins, _ ->
                    if (!basins.any { it.contains(Point(x, y)) } && input[y][x] != 9) {
                        basins.plus<Set<Point>>(expandBasin(input, setOf(Point(x, y)), Point(x, y)))
                    } else {
                        basins
                    }
                }
            }.map { it.size }.sorted().takeLast(3).reduce { a, b -> a * b }
        }
    }
}

fun main() {
    check(Day09.solvePart1("Day09_test").result == 15)
    val part1Solution = Day09.solvePart1("Day09")
    println("Part 1 solution: ${part1Solution.result} [${part1Solution.time}ms]")

    check(Day09.solvePart2("Day09_test").result == 1134)
    val part2Solution = Day09.solvePart2("Day09")
    println("Part 2 solution: ${part2Solution.result} [${part2Solution.time}ms]")
}
