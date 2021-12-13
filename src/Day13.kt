private object Day13 {
    private sealed interface PaperPoint {
        object Empty : PaperPoint
        object Dot : PaperPoint
    }

    private sealed interface FoldCommand {
        data class X(val coordinate: Int) : FoldCommand
        data class Y(val coordinate: Int) : FoldCommand
    }

    private fun parsePaper(input: List<String>): List<List<PaperPoint>> {
        val dots = input.takeWhile { it.isNotEmpty() }
            .map {
                Pair(
                    it.split(",")[0].toInt(),
                    it.split(",")[1].toInt()
                )
            }

        val paper = MutableList(dots.maxOf { it.second } + 1) {
            MutableList<PaperPoint>(dots.maxOf { it.first } + 1) { PaperPoint.Empty }
        }
        for ((x, y) in dots) {
            paper[y][x] = PaperPoint.Dot
        }

        return paper.map { it.toList() }.toList()
    }

    private fun parseCommands(input: List<String>): List<FoldCommand> =
        input.takeLastWhile { it.isNotEmpty() }
            .map {
                val axis = it.split("=")[0].last()
                val coordinate = it.split("=")[1].toInt()
                if (axis == 'x') {
                    FoldCommand.X(coordinate)
                } else {
                    FoldCommand.Y(coordinate)
                }
            }

    private fun parseInput(filename: String): Pair<List<List<PaperPoint>>, List<FoldCommand>> {
        val input = readInput(filename)
        val paper = parsePaper(input)
        val commands = parseCommands(input)

        return Pair(paper, commands)
    }

    private fun fold(paper: List<List<PaperPoint>>, command: FoldCommand): List<List<PaperPoint>> =
        when (command) {
            is FoldCommand.X -> List(paper.size) {
                List<PaperPoint>(command.coordinate) { PaperPoint.Empty }
            }
            is FoldCommand.Y -> List(command.coordinate) {
                List<PaperPoint>(paper[0].size) { PaperPoint.Empty }
            }
        }.mapIndexed { y, row ->
            row.mapIndexed { x, _ ->
                when (command) {
                    is FoldCommand.X ->
                        if (paper[y][x] is PaperPoint.Dot || paper[y][2 * command.coordinate - x] is PaperPoint.Dot)
                            PaperPoint.Dot
                        else
                            PaperPoint.Empty
                    is FoldCommand.Y ->
                        if (paper[y][x] is PaperPoint.Dot || paper[2 * command.coordinate - y][x] is PaperPoint.Dot)
                            PaperPoint.Dot
                        else
                            PaperPoint.Empty
                }
            }
        }

    private fun printPaper(paper: List<List<PaperPoint>>) {
        paper.forEach { row ->
            println(
                row.joinToString("") { point ->
                    when (point) {
                        PaperPoint.Empty -> "."
                        PaperPoint.Dot -> "#"
                    }
                }
            )
        }
    }

    fun solvePart1(filename: String): TimeMeasureResult<Int> {
        val (paper, commands) = parseInput(filename)
        return measureTimeInMillis {
            val paperAfterFirstFold = fold(paper, commands.first())
            paperAfterFirstFold.flatten().filterIsInstance<PaperPoint.Dot>().size
        }
    }

    fun solvePart2(filename: String) {
        val (paper, commands) = parseInput(filename)
        val newPaper = commands.fold(paper) { accPaper, command ->
            fold(accPaper, command)
        }
        printPaper(newPaper)
    }
}

fun main() {
    check(Day13.solvePart1("Day13_test").result == 17)
    val part1Solution = Day13.solvePart1("Day13")
    println("Part 1 solution: ${part1Solution.result} [${part1Solution.time}ms]")

    Day13.solvePart2("Day13")
}
