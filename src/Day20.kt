private object Day20 {
    private fun parseInput(filename: String): Pair<List<Char>, List<List<Char>>> {
        val lines = readInput(filename)
        val enhancement = lines[0].toList()
        val image = (0..199).map { (0..lines[2].length + 399).map { '.' } } +
            lines.drop(2).map { (0..199).map { '.' } + it.toList() + (0..199).map { '.' } } +
            (0..199).map { (0..lines[2].length + 399).map { '.' } }
        return Pair(enhancement, image)

    }

    private fun enhanceImage(image: List<List<Char>>, enhancement: List<Char>, infiniteDefault: Char): List<List<Char>> =
        image.mapIndexed { y, line ->
            line.mapIndexed { x, pixel ->
                val num = listOf(
                    image.getOrNull(y -  1)?.getOrNull(x - 1) ?: infiniteDefault,
                    image.getOrNull(y -  1)?.getOrNull(x) ?: infiniteDefault,
                    image.getOrNull(y -  1)?.getOrNull(x + 1) ?: infiniteDefault,
                    image.getOrNull(y)?.getOrNull(x - 1) ?: infiniteDefault,
                    pixel,
                    image.getOrNull(y)?.getOrNull(x + 1) ?: infiniteDefault,
                    image.getOrNull(y +  1)?.getOrNull(x - 1) ?: infiniteDefault,
                    image.getOrNull(y +  1)?.getOrNull(x) ?: infiniteDefault,
                    image.getOrNull(y +  1)?.getOrNull(x + 1) ?: infiniteDefault
                ).map {
                    if(it == '.') '0' else '1'
                }.joinToString("").toInt(2)

                enhancement[num]
            }
        }

    private fun enhanceTimes(image: List<List<Char>>, enhancement: List<Char>, times: Int): List<List<Char>> {
        if(times <= 0) return image
        return enhanceTimes(enhanceImage(image, enhancement, image[0][0]), enhancement, times -1)
    }

    fun solvePart1(filename: String): TimeMeasureResult<Int> {
        val (enhancement, image) = parseInput(filename)
        return measureTimeInMillis {
            enhanceTimes(image, enhancement, 2).flatten().count { it == '#' }
        }
    }

    fun solvePart2(filename: String): TimeMeasureResult<Int> {
        val (enhancement, image) = parseInput(filename)
        return measureTimeInMillis {
            enhanceTimes(image, enhancement, 50).flatten().count { it == '#' }
        }
    }
}
fun main() {
    check(Day20.solvePart1("Day20_test").result == 35)
    val part1Solution = Day20.solvePart1("Day20")
    println("Part 1 solution: ${part1Solution.result} [${part1Solution.time}ms]")

    check(Day20.solvePart2("Day20_test").result == 3351)
    val part2Solution = Day20.solvePart2("Day20")
    println("Part 2 solution: ${part2Solution.result} [${part2Solution.time}ms]")
}
