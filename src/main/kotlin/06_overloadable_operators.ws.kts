import java.lang.IndexOutOfBoundsException
import java.time.LocalDate

// Unlike Scala, the set of overloadable operators in Kotlin is limited.
// https://kotlinlang.org/docs/keyword-reference.html#operators-and-special-symbols

data class Point(val x: Int, val y: Int) {
    operator fun plus(other: Point): Point {
        return Point(x + other.x, y + other.y)
    }
}

val p = Point(0, 0) + Point(1, 1)
println("New point is $p")

// Another way of overloading an operator is via an extension function -------------------------------------------------

data class Point2D(val x: Int, val y: Int)

operator fun Point2D.plus(other: Point): Point = Point(x + other.x, y + other.y)
operator fun Point2D.times(scale: Double): Point = Point((x * scale).toInt(), (y * scale).toInt())
operator fun Point2D.unaryMinus(): Point = Point(-x, -y)

// For base types, method calls will be optimized by a compiler.

// Equality operators --------------------------------------------------------------------------------------------------
// ==  <= a?.equals(b) ?: (b === null)
// !=  <= !(a == b)
// in  <= b.contains(a)
// !in <= !b.contains(a)

class MyPoint(val x: Int, val y: Int) {
    override fun equals(other: Any?): Boolean {
        if (other === this) return true
        if (other !is Point) return false
        return other.x == x && other.y == y
    }
}

// Comparison operator -------------------------------------------------------------------------------------------------
// >  – a.compareTo(b) > 0
// <  – a.compareTo(b) < 0
// >= – a.compareTo(b) >= 0
// <= – a.compareTo(b) <= 0
// compareTo must return Int

class Person(val firstName: String, val lastName: String) : Comparable<Person> {
    override fun compareTo(other: Person): Int {
        return compareValuesBy(Person::lastName, Person::firstName)
    }
}

// Range operator ------------------------------------------------------------------------------------------------------
// .. <= a.rangeTo(b)

// from the standard library:
// operator fun <T: Comparable<T>> T.rangeTo(that: T): ClosedRange
(0..3).forEach { println(it) }

// Iterator for the for loop -------------------------------------------------------------------------------------------

operator fun ClosedRange<LocalDate>.iterator(): Iterator<LocalDate> =
    object : Iterator<LocalDate> {
        var current = start
        override fun hasNext() = current <= endInclusive
        override fun next() = current.apply { current = plusDays(1) }
    }

val days = LocalDate.of(2023, 6, 1)..LocalDate.of(2023, 6, 30)
for (day in days) {
    println(day)
}

// Access operators and definition by index ----------------------------------------------------------------------------
// a[i]                 <= a.get(i)
// a[i, j]              <= a.get(i, j)
// a[i_1, ..., i_n]     <= a.get(i_1, ..., i_n)
// a[i] = b             <= a.set(i, b)
// a[i, j] = b          <= a.set(i, j, b)
// a[i_1, ..., i_n] = b <= a.set(i_1, ..., i_n, b)

operator fun Point.get(index: Int): Int {
    return when (index) {
        0 -> x
        1 -> y
        else -> throw IndexOutOfBoundsException("Invalid coordinate $index")
    }
}

val point = Point(10, 20)
point[0]
point[1]

// Invocation operators ------------------------------------------------------------------------------------------------

// a()          <= a.invoke()
// a(i)         <= a.invoke(i)
// a(i, ..., j) <= a.invoke(i, ..., j)