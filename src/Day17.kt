private object Day17 {
    sealed class IsInRange {
        object No : IsInRange()
        object Yes : IsInRange()
        object Missed : IsInRange()
    }

    data class TargetArea(
        val x1: Int,
        val x2: Int,
        val y1: Int,
        val y2: Int
    ) {
        init {
            require(x1 < x2 && y1 > y2)
        }

        fun isInArea(probe: Probe): IsInRange =
            if (probe.x > x2 || probe.y < y2) {
                IsInRange.Missed
            } else if (probe.x < x1 || probe.y > y1) {
                IsInRange.No
            } else {
                IsInRange.Yes
            }
    }

    data class Probe(
        val x: Int = 0,
        val y: Int = 0,
        val velocityX: Int,
        val velocityY: Int,
        val highestVisited: Int = 0
    ) {
        fun step(): Probe =
            copy(
                x = x + velocityX,
                y = y + velocityY,
                velocityX = if (velocityX < 0) velocityX + 1 else if (velocityX > 0) velocityX - 1 else velocityX,
                velocityY = velocityY - 1,
                highestVisited = if(y + velocityY > highestVisited) y + velocityY else highestVisited
            )
    }

    private fun simulate(probe: Probe, target: TargetArea, ): Int? {
        val nextProbe = probe.step()
        return when(target.isInArea(nextProbe)) {
            IsInRange.No -> simulate(nextProbe, target)
            IsInRange.Missed -> null
            IsInRange.Yes -> probe.highestVisited
        }
    }


    fun solvePart1(target: TargetArea): TimeMeasureResult<Int> =
        measureTimeInMillis {
            (-1000..1000).flatMap { velocityY ->
                (-1000..1000).map{ velocityX ->
                    simulate(Probe(velocityX = velocityX, velocityY = velocityY), target)
                }
            }.filterNotNull().maxOf { it }
        }

    fun solvePart2(target: TargetArea): TimeMeasureResult<Int> =
        measureTimeInMillis {
            (-1000..1000).flatMap { velocityY ->
                (-1000..1000).map{ velocityX ->
                    simulate(Probe(velocityX = velocityX, velocityY = velocityY), target)
                }
            }.filterNotNull().size
        }
}

fun main() {
    check(Day17.solvePart1(Day17.TargetArea(x1 = 20, x2=30, y1=-5, y2=-10)).result == 45)
    val part1Solution = Day17.solvePart1(Day17.TargetArea(x1 = 85, x2=145, y1=-108, y2=-163))
    println("Part 1 solution: ${part1Solution.result} [${part1Solution.time}ms]")

    check(Day17.solvePart2(Day17.TargetArea(x1 = 20, x2=30, y1=-5, y2=-10)).result == 112)
    val part2Solution = Day17.solvePart2(Day17.TargetArea(x1 = 85, x2=145, y1=-108, y2=-163))
    println("Part 2 solution: ${part2Solution.result} [${part2Solution.time}ms]")
}
