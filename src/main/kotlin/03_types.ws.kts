import java.lang.IllegalStateException

val a: Int? = null // nullable
val a: Int = 1     // non-nullable, means "Int or null"

// 💡 All the things around nulls happens at compile time (except when types come from Java)!
// At runtime, JVM works with its standard types which doesn't slow down the byte-code.
// Compiler adds relatively fast checks like if something == null.

val s: String? = null

if (s != null) {
    val len = s.length // now we can call String methods
}
// but here we have String? again

// Can we do better? The Safe Call operator ----------------------------------------------------------------------------

val ns: String? = null
println(ns?.length)

// val a = b?.c?.d?.e

// Elvis operator makes handling null values concise and more readable -------------------------------------------------

val nsLen = ns?.length ?: "got empty string – can't tell its length"
println(nsLen)

// P.S. Kotlin doesn't provide a ternary operator

// Base types in Kotlin -----------------------------------------------------------------------------------------------

// Java has:
//  primitive types, e.g. int
//  reference types, e.g. Integer – a wrapper for int, String, etc.
//
// Primitive types can be stored in collections because of generics.

// 💡 Kotlin, at the same time, doesn't differentiate primitive and reference types.
//    We can use primitive types in collections and call functions from standard library on values of base types.
//    Kotlin deals with base types at compile time. E.g. Int automatically turns into a variable on stack whenever
//    that's possible (local variables on JVM, but Number in JS).
//    Unfortunately,that doesn't work for collections (List<Int>) and generics.
//    In such cases, compiler will wrap a primitive type into a wrapper type of java.lang
//    TLDR; Kotlin doesn't provide its own effective collections.

// Nullable types are wrapped into java.lang types ---------------------------------------------------------------------

// Int?    => java.lang.Integer
// Double? => java.lang.Double
// ...

// When Kotlin wraps types, identity of an object will be different, but we still can compare by value.

val tenK: Int = 10_000
tenK === tenK // true, deprecated in Kotlin 1.7.20
tenK == tenK  // true

val wrappedTenK: Int? = tenK
val anotherWrappedTenK: Int? = tenK
wrappedTenK === anotherWrappedTenK // false
wrappedTenK == anotherWrappedTenK // true

// Casting types -------------------------------------------------------------------------------------------------------

val intNum: Int = 1

// ⚠️ Comparing to Java, Kotlin base types are not extendable automatically.
// val longNum: Long = intNum <- 💥 type mismatch, have to use special methods for casting types
val longNum = intNum.toLong() // ✓ works!

// Why Kotlin doesn't cast types automatically?
// The answer is to not break equals().
// How would we compare values of two different types? E.g.
// val a: Int? = 1  -> java.lang.Integer
// val b: Long? = a -> java.lang.Long
// a == b -> false because equals() require both values to be the same type.

// Overloaded arithmetics ----------------------------------------------------------------------------------------------

// That's need for working with different types without casting them explicitly:
val l = 1L + 3 // Long + Int => Long

// Checking types with the `is` operator -------------------------------------------------------------------------------

val x: Any = 0

fun foo(x: Any) {
    if (x is String) println("String of length ${x.length}") // x: String
}

if (x !is String) println("x is not a string")
if (x is String && x.length > 0) println("x is non empty string")

when (x) {
    is Int -> println(x + 1)
    is String -> println(x.length + 1)
    is IntArray -> println(x.sum())
}

// Conversion operator `as` --------------------------------------------------------------------------------------------

// as  – unsafe conversion
// as? – safe conversion

val surprise: Any? = null
// val risky: String = surprise as String // hey, NPE! 💥
val lessRisky = surprise as? String       // lessRisky: String?

// How the safe casting works:
// surprise as? String
//      surprise is String => surprise as String
//      surprise !is String => null

// We can even combine safe casting with the elvis operator and exit or throw an error when we get null
// val requiredString = surprise as? String ?: return
// after the line above we'd have requiredString: String

// The `!!` (bang-bang) operator ---------------------------------------------------------------------------------------
// ⚠️ Don't use this operator if you can.

surprise!!
// surprise != null => surprise
// surprise == null => NPE 💥

// And please don't be like this:
// foo.bar!!.bazz!!.oops

// You might ask "Why has Kotlin developers kept the !! operator in a language?"
// The answer again is for Java interoperability.

// Kotlin Platform Types -----------------------------------------------------------------------------------------------
// https://kotlinlang.org/docs/java-interop.html
// https://www.baeldung.com/kotlin/platform-types

// They come from Java. Annotations are used for hints.
// [Java] @Nullable + Type = [Kotlin] Type?
// [Java] @NotNull + Type = [Kotlin] Type
// [Java] Type – no annotation, platform type could be Type or Type? => a risk of NPE!

// The Nothing type ----------------------------------------------------------------------------------------------------
// https://kotlinlang.org/spec/type-system.html
// https://youtu.be/juFkdMv4B9s - The Kotlin Type Hierarchy From Top to Bottom

// Nothing is the lowest Kotlin type. It doesn't have values.
// A function can return Nothing if its execution never stops, e.g. infinite loop or throws an exception

fun TODO(): Nothing = throw NotImplementedError()

fun fail(message: String): Nothing {
    throw IllegalStateException(message)
}

data class Address(val zip: Int, val city: String, val country: String)
data class Company(val name: String, val address: Address? = null)
val company = Company(name = "Statice")
val address = company.address ?: fail("No address")
println(address.city)

// The Unit type -------------------------------------------------------------------------------------------------------
// A type with a single value. The Unit type is a singleton!
// It makes Kotlin's type system more complete and avoids problems with void and Void (boxed void) from Java.

fun greeting(): Unit = println("Hello, World!")