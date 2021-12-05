import kotlin.math.abs

typealias Map = MutableList<MutableList<Int>>

private object Day05 {
    data class Point(val x: Int, val y: Int)
    data class Line(val start: Point, val end: Point)

    private fun parseInput(filename: String): List<Line> =
        readInput(filename).map {
            val startRaw = it.split(" -> ")[0]
            val endRaw = it.split(" -> ")[1]
            val start = Point(startRaw.split(",")[0].toInt(), startRaw.split(",")[1].toInt())
            val end = Point(endRaw.split(",")[0].toInt(), endRaw.split(",")[1].toInt())
            Line(start, end)
        }

    private fun newMap(): Map =
        (0..1000).map { (0..1000).map { 0 }.toMutableList() }.toMutableList()

    private fun drawHorizontalLine(map: Map, line: Line) {
        if (line.start.x == line.end.x) {
            val x = line.start.x
            val range = if (line.start.y < line.end.y) (line.start.y..line.end.y) else (line.end.y..line.start.y)
            for (y in range) {
                map[x][y] += 1
            }
        }
    }

    private fun drawVerticalLine(map: Map, line: Line) {
        if (line.start.y == line.end.y) {
            val y = line.start.y
            val range = if (line.start.x < line.end.x) (line.start.x..line.end.x) else (line.end.x..line.start.x)
            for (x in range) {
                map[x][y] += 1
            }
        }
    }

    private fun drawDiagonalLine(map: Map, line: Line){
        if(abs(line.end.x - line.start.x) == abs(line.end.y - line.start.y)) {
            val yRange = if (line.start.y < line.end.y) (line.start.y..line.end.y) else (line.end.y..line.start.y).reversed()
            val xRange = if (line.start.x < line.end.x) (line.start.x..line.end.x) else (line.end.x..line.start.x).reversed()
            val range = xRange.zip(yRange)
            for ((x,y) in range) {
                map[x][y] += 1
            }
        }
    }

    fun solvePart1(filename: String): Int {
        val lines = parseInput(filename)
        val map = newMap()

        for (line in lines) {
            drawVerticalLine(map, line)
            drawHorizontalLine(map, line)
        }

        return map.flatten().filter { it > 1 }.size
    }

    fun solvePart2(filename: String): Int {
        val lines = parseInput(filename)
        val map = newMap()

        for (line in lines) {
            drawVerticalLine(map, line)
            drawHorizontalLine(map, line)
            drawDiagonalLine(map, line)
        }

        return map.flatten().filter { it > 1 }.size
    }
}

fun main() {
    check(Day05.solvePart1("Day05_test") == 5)
    println("Part 1 solution: ${Day05.solvePart1("Day05")}")

    check(Day05.solvePart2("Day05_test") == 12)
    println("Part 2 solution: ${Day05.solvePart2("Day05")}")
}
