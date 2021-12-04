private typealias Draws = List<Int>
private typealias Board = List<List<Day04.BoardField>>

private object Day04 {
    data class BoardField(
        val value: Int,
        val isChecked: Boolean
    )

    data class RoundResults(
        val boards: List<Board>,
        val winningBoards: List<Board>
    )

    data class GameInput(
        val draws: Draws,
        val boards: List<Board>
    )

    private fun parseInput(filename: String): GameInput {
        val fileContent = readInput(filename)

        val draws = fileContent.first()
            .split(",")
            .map { it.toInt() }

        val boards = fileContent.drop(1)
            .filter { it.isNotEmpty() }
            .chunked(5)
            .map { rows ->
                rows.map { row ->
                    row.split(" ")
                        .filter { it.isNotEmpty() }
                        .map {
                            BoardField(
                                value = it.toInt(),
                                isChecked = false
                            )
                        }
                }
            }

        return GameInput(draws, boards)
    }

    private fun checkDraws(draw: Int, boards: List<Board>): List<Board> =
        boards.map { board ->
            board.map { row ->
                row.map { field ->
                    if (field.value == draw) field.copy(isChecked = true) else field
                }
            }
        }

    private fun findWinningBoards(boards: List<Board>): List<Board> =
        boards.mapNotNull { board ->
            val winningColumn: List<BoardField>? = (0 until board.first().size).map { columnIndex ->
                board.map { it[columnIndex] }
            }.firstOrNull { column -> column.all { it.isChecked } }

            val winningRow = board.firstOrNull { row -> row.all { it.isChecked } }

            if (winningRow != null || winningColumn != null) {
                board
            } else {
                null
            }
        }

    private fun calculateResult(draw: Int, winningBoard: Board): Int {
        val nonCheckedSum = winningBoard.flatMap { row -> row.filter { !it.isChecked }.map { it.value } }.sum()
        return draw * nonCheckedSum
    }

    private fun playRound(draw: Int, boards: List<Board>): RoundResults {
        val boardsAfterRound = checkDraws(draw, boards)
        val winningBoards = findWinningBoards(boards)

        return RoundResults(boardsAfterRound, winningBoards)
    }

    private fun filterWinningBoards(boards: List<Board>, winningBoards: List<Board>): List<Board> =
        boards.filter { !winningBoards.contains(it) }

    private fun solvePart1Helper(draws: Draws, boards: List<Board>): Int {
        val (boardsAfterRound, winningBoards) = playRound(draws.first(), boards)

        if (winningBoards.isNotEmpty()) {
            return calculateResult(draws.first(), winningBoards.first())
        }

        return solvePart1Helper(draws.drop(1), boardsAfterRound)
    }

    fun solvePart1(filename: String): Int {
        val (draws, boards) = parseInput(filename)
        return solvePart1Helper(draws, boards)
    }

    private fun solvePart2Helper(draws: Draws, boards: List<Board>): Int {
        val (boardsAfterRound, winningBoards) = playRound(draws.first(), boards)

        if (winningBoards.isNotEmpty()) {
            return if (boards.size == winningBoards.size) {
                return calculateResult(draws.first(), winningBoards.first())
            } else {
                solvePart2Helper(
                    draws.drop(1),
                    filterWinningBoards(boardsAfterRound, winningBoards)
                )
            }
        }

        return solvePart2Helper(draws.drop(1), boardsAfterRound)
    }

    fun solvePart2(filename: String): Int {
        val (draws, boards) = parseInput(filename)
        return solvePart2Helper(draws, boards)
    }
}

fun main() {
    check(Day04.solvePart1("Day04_test") == 4512)
    println("Part 1 solution: ${Day04.solvePart1("Day04")}")

    check(Day04.solvePart2("Day04_test") == 1924)
    println("Part 2 solution: ${Day04.solvePart2("Day04")}")
}
