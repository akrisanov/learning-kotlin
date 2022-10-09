import java.lang.Exception

// Extensions ----------------------------------------------------------------------------------------------------------
// https://kotlinlang.org/docs/extensions.html

// Extensions are widely used in standard library in Kotlin.
// Example: https://github.com/JetBrains/kotlin/blob/master/libraries/stdlib/src/kotlin/util/Tuples.kt#L43

fun String.lastChar(): Char = this[this.length - 1]

val c = "Kotlin".lastChar()

/* from Java
import String.lastChar as last

char c = StringUtilKt.lastChar("Java");
 */

// ⚠️ Extensions are not overridden in inheritance!

// Varargs -------------------------------------------------------------------------------------------------------------

// One of the common examples is creating a list of values:
// fun listOf<T>(vararg values: T): List<T> { /* ... */ }
// where values are an object of type Array<out T>

// Kotlin allows to define only one vararg for a function.
// It'd be better if that argument will be the last one.
// Otherwise, we'd have to name arguments.

// Operator * is a "spread" operator:
fun pp(vararg strings: String) {
    for (s in strings)
        println(s)
}

pp("a", "b", "c")
pp(*listOf("0", "1", "2").toTypedArray())

// Handling exceptions -------------------------------------------------------------------------------------------------
// https://kotlinlang.org/docs/exceptions.html

try {
    // ...
} catch (e: Exception) {
    // ...
} finally {
    // ...
}

// throw Exception("Something bad happened")

fun main(args: Array<String>) {
    val number = try {
        args[0].toInt()
    } catch (e: ArrayIndexOutOfBoundsException) {
        println("Not enough arguments")
        return
    } catch (e: NumberFormatException) {
        println("Bad argument")
        return
        // throw IllegalStateException(e)
        // null
    }
    println(if (number % 2 == 0) "even" else "odd")
}

main(arrayOf("33", "34", "9"))