import java.io.File
import java.math.BigInteger
import java.security.MessageDigest



/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src", "$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)

data class TimeMeasureResult<T>(
    val result: T,
    val time: Long
)

fun<T> measureTimeInMillis(function: () -> T): TimeMeasureResult<T> {
    val startTime = System.currentTimeMillis()
    val result: T = function.invoke()
    val endTime = System.currentTimeMillis()
    return TimeMeasureResult(result, endTime - startTime)
}
