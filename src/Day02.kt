private object Day02 {
    enum class Direction {
        FORWARD, DOWN, UP
    }

    data class Command(
        val direction: Direction,
        val value: Int
    )

    fun parseDay02Input(filename: String): List<Command> =
        readInput(filename)
            .map { it.split(" ") }
            .map {
                Command(
                    direction = Direction.valueOf(it[0].uppercase()),
                    value = it[1].toInt()
                )
            }

    data class Position(
        val horizontal: Int,
        val vertical: Int
    )

    fun calculateFinalPosition(commands: List<Command>): Position =
        commands.fold(Position(0, 0)) { position, command ->
            when (command.direction) {
                Direction.FORWARD -> position.copy(horizontal = position.horizontal + command.value)
                Direction.DOWN -> position.copy(vertical = position.vertical + command.value)
                Direction.UP -> position.copy(vertical = position.vertical - command.value)
            }
        }

    fun solvePart1(filename: String): Int {
        val input = parseDay02Input(filename)
        val finalPosition = calculateFinalPosition(input)
        return finalPosition.horizontal * finalPosition.vertical
    }

    data class PositionWithAim(
        val horizontal: Int,
        val vertical: Int,
        val aim: Int
    )

    fun calculateFinalPositionWithAim(commands: List<Command>): PositionWithAim =
        commands.fold(PositionWithAim(0, 0, 0)) { position, command ->
            when (command.direction) {
                Direction.FORWARD -> position.copy(
                    horizontal = position.horizontal + command.value,
                    vertical = position.vertical + (position.aim * command.value)

                )
                Direction.DOWN -> position.copy(aim = position.aim + command.value)
                Direction.UP -> position.copy(aim = position.aim - command.value)
            }
        }

    fun solvePart2(filename: String): Int {
        val input = parseDay02Input(filename)
        val finalPosition = calculateFinalPositionWithAim(input)
        return finalPosition.horizontal * finalPosition.vertical
    }
}

fun main() {
    check(Day02.solvePart1("Day02_test") == 150)
    println("Part 1 solution: ${Day02.solvePart1("Day02")}")

    check(Day02.solvePart2("Day02_test") == 900)
    println("Part 2 solution: ${Day02.solvePart2("Day02")}")
}
