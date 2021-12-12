import kotlin.collections.Map

private object Day12 {
    private fun parseInput(filename: String): Map<String, List<String>> =
        readInput(filename).flatMap { line ->
            listOf(
                line.split("-")[0] to line.split("-")[1],
                line.split("-")[1] to line.split("-")[0],
            )
        }.groupBy {
            it.first
        }.mapValues { (_, value) -> value.map { it.second } }

    fun findPath(
        path: List<String>,
        map: Map<String, List<String>>
    ): List<List<String>> {
        if (path.last() == "end") {
            return listOf(path)
        }
        return map[path.last()]!!.flatMap { nextNode ->
            val isNextNodeSmallCave = nextNode[0].isLowerCase()
            val isCaveAlreadyVisited = path.contains(nextNode)
            if (isNextNodeSmallCave && isCaveAlreadyVisited) emptyList()
            else findPath(path + nextNode, map)
        }
    }

    fun findPath2(
        path: List<String>,
        map: Map<String, List<String>>,
        smallCaveVisitedTwice: Boolean
    ): List<List<String>> {
        if (path.last() == "end") {
            return listOf(path)
        }
        return map[path.last()]!!
            .filter { it != "start" }
            .flatMap { nextNode ->
                val isNextNodeSmallCave = nextNode[0].isLowerCase()
                val isNextNodeSecondVisit = path.count { it == nextNode } >= 1
                if (isNextNodeSmallCave && isNextNodeSecondVisit && smallCaveVisitedTwice)
                    emptyList()
                else findPath2(
                    path + nextNode,
                    map,
                    smallCaveVisitedTwice || (isNextNodeSmallCave && isNextNodeSecondVisit)
                )
            }
    }

    fun solvePart1(filename: String): TimeMeasureResult<Int> {
        val map = parseInput(filename)
        return measureTimeInMillis {
            findPath(listOf("start"), map).size
        }
    }

    fun solvePart2(filename: String): TimeMeasureResult<Int> {
        val map = parseInput(filename)
        return measureTimeInMillis {
            findPath2(listOf("start"), map, false).size
        }
    }
}

fun main() {
    check(Day12.solvePart1("Day12_test").result == 10)
    val part1Solution = Day12.solvePart1("Day12")
    println("Part 1 solution: ${part1Solution.result} [${part1Solution.time}ms]")

    check(Day12.solvePart2("Day12_test").result == 36)
    val part2Solution = Day12.solvePart2("Day12")
    println("Part 2 solution: ${part2Solution.result} [${part2Solution.time}ms]")
}
