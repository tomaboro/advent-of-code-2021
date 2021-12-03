private object Day03 {
    fun parseInput(filename: String): List<List<Char>> =
        readInput(filename).map { line ->
            line.toList().filter { !it.isWhitespace() }
        }

    fun String.toDecimal(): Int =
        this.toInt(2)

    fun List<Char>.toDecimal(): Int =
        this.joinToString("").toDecimal()

    fun solvePart1(filename: String): Int {
        val input = parseInput(filename)

        val oneBitCounts = input.fold(List(input.first().size) { 0 }) { acc, list ->
            acc.mapIndexed { index, count -> if (list[index] == '1') count + 1 else count }
        }

        val gammaBinary = oneBitCounts.map { count ->
            if (count > input.size - count) '1' else '0'
        }
        val epsilonBinary = oneBitCounts.map { count ->
            if (count > input.size - count) '0' else '1'
        }

        val gamma = gammaBinary.toDecimal()
        val epsilon = epsilonBinary.toDecimal()

        return gamma * epsilon
    }

    fun solvePart2Helper(
        binaryInputs: List<List<Char>>,
        index: Int,
        assignOnePredicate: (zeroBitCount: Int, oneBitCount: Int) -> Boolean
    ): String {
        if (binaryInputs.size == 1 || index >= binaryInputs.first().size) {
            return binaryInputs.first().joinToString("")
        }

        val oneBitCount = binaryInputs.map { it[index] }.count { it == '1' }
        val zeroBitCount = binaryInputs.size - oneBitCount

        return if (assignOnePredicate(zeroBitCount, oneBitCount)) {
            solvePart2Helper(binaryInputs.filter { it[index] == '1' }, index + 1, assignOnePredicate)
        } else {
            solvePart2Helper(binaryInputs.filter { it[index] == '0' }, index + 1, assignOnePredicate)
        }
    }

    fun solvePart2(filename: String): Int {
        val input = readInput(filename).map { line ->
            line.toList().filter { !it.isWhitespace() }
        }

        val oxygenBinary = solvePart2Helper(input, 0) { zeroBitCount, oneBitCount ->
            oneBitCount >= zeroBitCount
        }
        val co2Binary = solvePart2Helper(input, 0) { zeroBitCount, oneBitCount ->
            oneBitCount < zeroBitCount
        }

        val oxygen = oxygenBinary.toDecimal()
        val co2 = co2Binary.toDecimal()

        return oxygen * co2
    }
}

fun main() {
    check(Day03.solvePart1("Day03_test") == 198)
    println("Part 1 solution: ${Day03.solvePart1("Day03")}")

    check(Day03.solvePart2("Day03_test") == 230)
    println("Part 2 solution: ${Day03.solvePart2("Day03")}")
}
