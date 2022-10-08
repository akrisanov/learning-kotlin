import java.util.ArrayList
import java.util.LinkedList
import java.util.TreeMap

// Arrays are rarely used in Kotlin. They are objects of Array<T>. -----------------------------------------------------
// Arrays are best used when you have code in Java. In other cases it is often better to use lists.

val strings = arrayOf("a", "b", "c")
println("%d, %s %s %s".format(strings.size, *strings)) // <- varargs
println(strings.contentToString())   // <- or java.utils.Arrays.toString(strings)

val nulls = arrayOfNulls<Int>(10);
nulls[0] // null

val letters = Array<String>(26) { i -> ('a' + i).toString() }
println(letters.contentToString())

// There are special classes for **primitive** types:
// IntArray
// ShortArray
// ByteArray
// CharArray
// ...
val chars = CharArray(26) { i -> 'a' + i }
println("There are %d letters.\nThe last letter is %c.".format(chars.size, chars[25]))

// Iterating over arrays -----------------------------------------------------------------------------------------------

val names = arrayOf("andrey", "maria", "mark")
for (name in names) {
    print("$name ")
}

val languages = arrayOf("English", "Russian", "German")
for ((index, lang) in languages.withIndex()) {
    println("Language at $index is $lang")
}

// Collections ---------------------------------------------------------------------------------------------------------

val mutLst = mutableListOf(6, 6, 6)
for (i in 0..2) mutLst[i] = i
println(mutLst.toTypedArray().contentToString())

val lst = listOf(1, "zzz", 3) // read-only collection of type List<Any>
println("%d/%s/%d".format(*lst.toTypedArray()))

// ⚠️ Both read-only and mutable references can point to the same collection at time.
// 😖 Read-only collections can still be modified in Java code or in another thread.

// Arrays of primitive types -------------------------------------------------------------------------------------------

// This is analogous to int[] from Java
var intArr = IntArray(3) // {0,0,0}
println(intArr.toString())    // [I@24d46ca6 – IntArray doesn't provide overloaded toString()
println(intArr.joinToString(", "))

// other forms of initialization
intArr = /*new*/ IntArray(3) { it + 1 }
intArr = IntArray(3) { i -> i + 1 }
intArr = IntArray(3, { i -> i + 1 }) // less preferred

// Arrays of reference types -------------------------------------------------------------------------------------------

// This is analogous to Integer[] from Java
var arr = Array<Int>(3) { 5 }
var nullableArr = Array<Int?>(3) { null }

// Converting to an array of primitive types
intArr = arr.toIntArray()
// and back again
arr = intArr.toTypedArray()

var arr1 = Array(3) { i -> "a".repeat(i + 1) }
println(arr1.contentToString())

// ⚠️ By adding a new element, we extend a copy of the array
arr1 = arr1.plus("x") // calling + operator as a function
arr1 += "y"
arr1 += "z"
println(arr1.contentToString())

// Immutable List ------------------------------------------------------------------------------------------------------

// Analogous from Java
// List<Integer> immutable = Arrays.asList(1, 2, 3);
// immutable.add(2) throws UnsupportedOperationException

// We can create a list via constructor:
val intLst = List<Int>(3) { it } // [0,1,2]
println("Immutable list: $intLst")    // there is overloaded toString()

// We can create a list via Java collections:
val lst1: List<String> = ArrayList(3) // empty list
val lst2: List<String> = LinkedList<String>(lst1)

// There are convenient functions in Kotlin to create collections:
var list = listOf(1, 2, 3)
list = emptyList()
list = listOfNotNull(1, null, 2, 3, null) // 💡 [1, 2, 3]

val linkToList = list       // copies the list
list = list.plus(0) // creates a new list
list += 0                   // basically the same, calling + operator

println("old: $linkToList, new: $list")
// old: [1, 2, 3], new: [1, 2, 3, 0, 0]

val (a, b, c) = list // destructuring!

// Maps and sets  ------------------------------------------------------------------------------------------------------

val map1: Map<String, String> = /*new*/ TreeMap<String, String>()
val map2: Map<String, String> = /*new*/ HashMap()

val map3 = mapOf("a" to 1, "b" to 2, "c" to 3) // LinkedList
val map4 = listOf(1 to 2, 3 to 4).toMap()

// What is `to`? That is just a function creating a pair!
var pair = /*new*/ Pair("a", 2) // from Java
pair = "a".to(2)
pair = "a" to 2

// ⚠️ Only mutable maps can be modified
val mutMap0 = sortedMapOf<String, String>() // TreeMap
val mutMap1 = mutableMapOf<String, String>() // LinkedList

mutMap1["a"] = "b"                               // => mutMap1.set("a", "b")
mutMap1 += mapOf("c" to "d", "kotlin" to "love") // => mutMap1.putAll(mapOf("c" to "d", "kotlin" to "love")) from Java

println(mutMap1)