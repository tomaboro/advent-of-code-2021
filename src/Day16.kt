private object Day16 {
    private val hexToBinaryMap = mapOf(
        "0" to "0000",
        "1" to "0001",
        "2" to "0010",
        "3" to "0011",
        "4" to "0100",
        "5" to "0101",
        "6" to "0110",
        "7" to "0111",
        "8" to "1000",
        "9" to "1001",
        "A" to "1010",
        "B" to "1011",
        "C" to "1100",
        "D" to "1101",
        "E" to "1110",
        "F" to "1111",
    )

    private sealed class Packet(open val version: Long, open val typeId: Long) {
        data class LiteralPacket(
            override val version: Long,
            override val typeId: Long,
            val literal: Long
        ) : Packet(version, typeId)

        data class OperationPacket(
            override val version: Long,
            override val typeId: Long,
            val subPackets: List<Packet>
        ) : Packet(version, typeId)
    }

    private fun parseLiteral(binary: String): Pair<Packet.LiteralPacket, Int> {
        check(binary.substring(3, 6).toInt(2) == 4)
        val version = binary.substring(0, 3).toLong(2)
        var literal = ""
        var iter = 0
        while (binary[6 + (iter * 5)] != '0') {
            literal += binary.substring(7 + (iter * 5), 11 + (iter * 5))
            iter += 1
        }
        literal += binary.substring(7 + (iter * 5), 11 + (iter * 5))
        return Pair(
            Packet.LiteralPacket(version, 4L, literal.toLong(2)),
            11 + (iter * 5)
        )
    }

    private fun parseZeroOperator(binary: String): Pair<Packet.OperationPacket, Int> {
        check(binary.substring(3, 6).toInt(2) != 4 && binary[6] == '0')
        val version = binary.substring(0, 3).toLong(2)
        val typeId = binary.substring(3, 6).toLong(2)
        val length = binary.substring(7, 22).toInt(2)
        var bits = 0
        val packets = mutableListOf<Packet>()
        while (bits < length) {
            val (packet, pointer) = parse(binary.substring(22 + bits, 22 + length))
            bits += pointer
            packets += packet
        }
        return Pair(
            Packet.OperationPacket(version, typeId, packets.toList()),
            22 + length
        )
    }

    private fun parseOneOperator(binary: String): Pair<Packet.OperationPacket, Int> {
        check(binary.substring(3, 6).toInt(2) != 4 && binary[6] == '1')
        val version = binary.substring(0, 3).toLong(2)
        val typeId = binary.substring(3, 6).toLong(2)
        val length = binary.substring(7, 18).toInt(2)
        var bits = 18
        val packets = mutableListOf<Packet>()
        (0 until length).forEach { _ ->
            val (packet, pointer) = parse(binary.drop(bits))
            bits += pointer
            packets += packet
        }
        return Pair(
            Packet.OperationPacket(version, typeId, packets),
            bits
        )
    }

    private fun parse(binary: String): Pair<Packet, Int> {
        val typeId = binary.substring(3, 6).toInt(2)
        if (typeId == 4) return parseLiteral(binary)
        else {
            val lengthTypeId = binary[6]
            if (lengthTypeId == '0') {
                return parseZeroOperator(binary)
            } else {
                return parseOneOperator(binary)
            }
        }
    }


    private fun versionSum(packet: Packet): Long =
        when (packet) {
            is Packet.LiteralPacket -> packet.version
            is Packet.OperationPacket -> packet.version + packet.subPackets.sumOf { versionSum(it) }
        }

    private fun calculate(packet: Packet): Long =
        when (packet) {
            is Packet.LiteralPacket -> packet.literal
            is Packet.OperationPacket -> when (packet.typeId) {
                0L -> packet.subPackets.sumOf { calculate(it) }
                1L -> packet.subPackets.fold(1) { acc, next -> acc * calculate(next) }
                2L -> packet.subPackets.minOf { calculate(it) }
                3L -> packet.subPackets.maxOf { calculate(it) }
                5L -> if (calculate(packet.subPackets[0]) > calculate(packet.subPackets[1])) 1L else 0L
                6L -> if (calculate(packet.subPackets[0]) < calculate(packet.subPackets[1])) 1L else 0L
                7L -> if (calculate(packet.subPackets[0]) == calculate(packet.subPackets[1])) 1L else 0L
                else -> 0
            }
        }

    fun solvePart1(input: String): TimeMeasureResult<Long> =
        measureTimeInMillis {
            val binary = input.split("").mapNotNull { hexToBinaryMap[it] }.joinToString("")
            versionSum(parse(binary).first)
        }

    fun solvePart2(input: String): TimeMeasureResult<Long> =
        measureTimeInMillis {
            val binary = input.split("").mapNotNull { hexToBinaryMap[it] }.joinToString("")
            calculate(parse(binary).first)
        }
}

fun main() {
    val testInputs1 = mapOf(
        "8A004A801A8002F478" to 16L,
        "620080001611562C8802118E34" to 12L,
        "C0015000016115A2E0802F182340" to 23L,
        "A0016C880162017C3686B18A3D4780" to 31L,
    )

    testInputs1.keys.map { Pair(Day16.solvePart1(it).result, testInputs1[it]) }.forEach { (received, expected) ->
        check(received == expected)
    }
    val part1Solution = Day16.solvePart1(readInput("Day16")[0])
    println("Part 1 solution: ${part1Solution.result} [${part1Solution.time}ms]")

    val testInputs2 = mapOf(
        "C200B40A82" to 3L,
        "04005AC33890" to 54L,
        "880086C3E88112" to 7L,
        "CE00C43D881120" to 9L,
        "D8005AC2A8F0" to 1L,
        "F600BC2D8F" to 0L,
        "9C005AC2F8F0" to 0L,
        "9C0141080250320F1802104A08" to 1L,
    )

    testInputs2.keys.map { Pair(Day16.solvePart2(it).result, testInputs2[it]) }.forEach { (received, expected) ->
        check(received == expected)
    }
    val part2Solution = Day16.solvePart2(readInput("Day16")[0])
    println("Part 2 solution: ${part2Solution.result} [${part2Solution.time}ms]")
}
