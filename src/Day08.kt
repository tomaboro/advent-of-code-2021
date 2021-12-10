import kotlin.collections.Map

private object Day08 {
    private data class Entry(
        val signals: List<String>,
        val outputs: List<String>
    )

    private fun parseInput(filename: String): List<Entry> =
        readInput(filename)
            .map {
                val signals = it.split(" | ")[0].split(" ")
                val outputs = it.split(" | ")[1].split(" ")
                Entry(signals, outputs)
            }

    fun solvePart1(filename: String): TimeMeasureResult<Int> {
        val entries = parseInput(filename)
        return measureTimeInMillis {
            entries.sumOf { (_, outputs) ->
                outputs.filter { listOf(2, 3, 4, 7).contains(it.length) }.size
            }
        }
    }

    private val signalToNumber = mapOf(
        "abcefg" to "0",
        "cf" to "1",
        "acdeg" to "2",
        "acdfg" to "3",
        "bcdf" to "4",
        "abdfg" to "5",
        "abdefg" to "6",
        "acf" to "7",
        "abcdefg" to "8",
        "abcdfg" to "9"
    )

    private fun buildInitialDictionariesOptions(signals: List<String>): Map<Char, Set<Char>> {
        val ones = signals.filter { it.length == 2 }
        val fours = signals.filter { it.length == 4 }
        val sevens = signals.filter { it.length == 3 }
        val eights = signals.filter { it.length == 7 }
        val possibilities = mutableMapOf<Char, Set<Char>>(
            'a' to emptySet(),
            'b' to emptySet(),
            'c' to emptySet(),
            'd' to emptySet(),
            'e' to emptySet(),
            'f' to emptySet(),
            'g' to emptySet()

        )
        possibilities['c'] = ones[0].toSet()
        possibilities['f'] = ones[0].toSet()
        possibilities['a'] = sevens[0].toSet() - possibilities['c']!! - possibilities['f']!!
        if (possibilities['c']!!.isNotEmpty()) possibilities['c'] = sevens[0].toSet()
        if (possibilities['f']!!.isNotEmpty()) possibilities['c'] = sevens[0].toSet()
        possibilities['b'] = fours[0].toSet() -
            possibilities['c']!! -
            possibilities['f']!!
        possibilities['d'] = fours[0].toSet() -
            possibilities['c']!! -
            possibilities['f']!!
        if (possibilities['c'] == emptySet<Char>()) possibilities['c'] = fours[0].toSet()
        if (possibilities['f'] == emptySet<Char>()) possibilities['c'] = fours[0].toSet()
        possibilities['g'] = eights[0].toSet() -
            possibilities['c']!! - possibilities['f']!! -
            possibilities['b']!! - possibilities['d']!!
        possibilities['e'] = eights[0].toSet() -
            possibilities['c']!! -
            possibilities['f']!! -
            possibilities['b']!! -
            possibilities['d']!!
        if (possibilities['a']!!.isNotEmpty()) possibilities['c'] = eights[0].toSet()
        if (possibilities['b']!!.isNotEmpty()) possibilities['c'] = eights[0].toSet()
        if (possibilities['c']!!.isNotEmpty()) possibilities['c'] = eights[0].toSet()
        if (possibilities['d']!!.isNotEmpty()) possibilities['c'] = eights[0].toSet()
        if (possibilities['a']!!.isNotEmpty()) possibilities['c'] = eights[0].toSet()
        if (possibilities['f']!!.isNotEmpty()) possibilities['c'] = eights[0].toSet()

        return possibilities.toMap()
    }

    fun solvePart2(filename: String): TimeMeasureResult<Int> {
        val entries = parseInput(filename)
        return measureTimeInMillis {
            entries.sumOf { (signals, outputs) ->
                val allSignals = signals + outputs
                val initialDictionaryOptions = buildInitialDictionariesOptions(allSignals)
                initialDictionaryOptions.keys.fold(emptyList<Map<Char, Char>>()) { partialDictionaries, dictionaryKey ->
                    if (partialDictionaries.isNotEmpty()) {
                        partialDictionaries.flatMap { partialDictionary ->
                            initialDictionaryOptions[dictionaryKey]!!
                                .filter { !partialDictionary.values.contains(it) }
                                .map { partialDictionary.plus(dictionaryKey to it) }
                        }
                    } else {
                        initialDictionaryOptions[dictionaryKey]!!.map { mapOf(dictionaryKey to it) }
                    }
                }
                    .map { it.reverse() }
                    .map { reversedDictionary ->
                        allSignals.map { signal ->
                            signal.toSet().map { char ->
                                reversedDictionary[char]
                            }.sortedBy {
                                it
                            }.joinToString("")
                        }
                    }
                    .map { translatedSignals ->
                        translatedSignals.map { signalToNumber[it] }
                    }.filter { displayedNumbers ->
                        displayedNumbers.all { it != null }
                    }[0]
                    .takeLast(4)
                    .joinToString("")
                    .toInt()
            }
        }
    }
}

fun main() {
    check(Day08.solvePart1("Day08_test").result == 26)
    val part1Solution = Day08.solvePart1("Day08")
    println("Part 1 solution: ${part1Solution.result} [${part1Solution.time}ms]")

    check(Day08.solvePart2("Day08_test").result == 61229)
    val part2Solution = Day08.solvePart2("Day08")
    println("Part 2 solution: ${part2Solution.result} [${part2Solution.time}ms]")
}
