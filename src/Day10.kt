import kotlin.collections.Map

private object Day10 {
    private fun parseInput(filename: String): List<List<Char>> =
        readInput(filename).map { line -> line.toList() }

    private val part1Points: Map<Char, Int> = mapOf(
        ')' to 3,
        ']' to 57,
        '}' to 1197,
        '>' to 25137
    )

    private val part2Points: Map<Char, Int> = mapOf(
        ')' to 1,
        ']' to 2,
        '}' to 3,
        '>' to 4
    )

    private val closeBracket: Map<Char, Char> = mapOf(
        '(' to ')',
        '[' to ']',
        '{' to '}',
        '<' to '>'
    )

    private val openBracket: Map<Char, Char> = closeBracket.reverse()

    private fun getMissingBrackets(line: List<Char>, opens: List<Char>): List<Char>? {
        if (line.isEmpty()) {
            return opens.reversed().map { closeBracket[it]!! }
        }
        return when (line.first()) {
            '(', '[', '{', '<' ->
                getMissingBrackets(
                    line.drop(1),
                    opens + line.first()
                )
            ')', ']', '}', '>' ->
                if (opens.last() != openBracket[line.first()]) null
                else getMissingBrackets(line.drop(1), opens.dropLast(1))
            else -> null
        }
    }

    private fun getSyntaxErrorScore(line: List<Char>, opens: List<Char>): Int {
        if (line.isEmpty()) {
            return 0
        }
        return when (line.first()) {
            '(', '[', '{', '<' ->
                getSyntaxErrorScore(
                    line.drop(1),
                    opens + line.first()
                )
            ')', ']', '}', '>' ->
                if (opens.last() != openBracket[line.first()]) part1Points[line.first()]!!
                else getSyntaxErrorScore(line.drop(1), opens.dropLast(1))
            else -> 0
        }
    }

    private fun getAutocompleteScore(missingBrackets: List<Char>): Long =
        missingBrackets.fold(0L) { acc, next ->
            acc * 5 + part2Points[next]!!
        }

    fun solvePart1(filename: String): TimeMeasureResult<Int> {
        val input = parseInput(filename)
        return measureTimeInMillis {
            input.sumOf { getSyntaxErrorScore(it, emptyList()) }
        }
    }

    fun solvePart2(filename: String): TimeMeasureResult<Long> {
        val input = parseInput(filename)
        return measureTimeInMillis {
            input.mapNotNull { getMissingBrackets(it, emptyList()) }
                .map { getAutocompleteScore(it) }
                .sorted()
                .middle()
        }
    }
}

fun main() {
    check(Day10.solvePart1("Day10_test").result == 26397)
    val part1Solution = Day10.solvePart1("Day10")
    println("Part 1 solution: ${part1Solution.result} [${part1Solution.time}ms]")

    check(Day10.solvePart2("Day10_test").result == 288957L)
    val part2Solution = Day10.solvePart2("Day10")
    println("Part 2 solution: ${part2Solution.result} [${part2Solution.time}ms]")
}
