// Defining a class ----------------------------------------------------------------------------------------------------
// https://kotlinlang.org/docs/classes.html

// A class has a default constructor and two properties:
// var => getter + setter
// val => getter
open class Tree(var age: Int, val treeType: TreeType)

enum class TreeType {
    FIR, BIRCH, PINE, OAK, MAPLE
}

val fir = Tree(5, TreeType.FIR)
val age = fir.age

// Another example
class AnotherTree(var age: Int = 0, val treeType: TreeType) {
    val height = age * when (treeType) {
        TreeType.FIR -> 5
        TreeType.BIRCH -> 10
        TreeType.MAPLE -> 7
        TreeType.OAK -> 1
        TreeType.PINE -> 5
    }

    val isCounter = when (treeType) {
        TreeType.FIR, TreeType.PINE -> true
        TreeType.BIRCH, TreeType.MAPLE, TreeType.OAK -> false
    }

    override fun toString(): String {
        return when (treeType) {
            TreeType.FIR -> "fir"
            TreeType.OAK -> "oak"
            TreeType.MAPLE -> "maple"
            TreeType.BIRCH -> "birch"
            TreeType.PINE -> "pine"
        }
    }
}

val pine = AnotherTree(10, TreeType.PINE)
println("$pine, ${pine.age} y.o., height of ${pine.height}")

// Initializing an object ----------------------------------------------------------------------------------------------

class Person(name: String, val age: Int, val height: Float) {
    val name: String = if (name != "") name else "John Doe"

    init {
        require(age >= 18) { "must be an adult" } // raises java.lang.IllegalArgumentException
        require(height > 1.0f)
    }
}

val me = Person("Andrey", 32, height = 1.74f)
val john = Person("", 18, height = 1.8f)

// Private constructor -------------------------------------------------------------------------------------------------
// https://kotlinlang.org/docs/visibility-modifiers.html
// That can be useful when we want to implement a factory of objects and call the constructor inside the class.

class Connection private constructor() {}
//val conn = Connection() doesn't work

// Secondary constructors ----------------------------------------------------------------------------------------------
// https://kotlinlang.org/docs/classes.html#secondary-constructors
// A class can have multiple constructors

data class MyClass(val a: Int?, val b: String?, val c: Double?) {
    constructor() : this(null, null, null)
    constructor(a: Int) : this(a, null, null)
    constructor(a: Int, b: String) : this(a, b, null)
}

println(MyClass().toString())
println(MyClass(1).toString())
println(MyClass(1, "String").toString())
println(MyClass(1, "String", 0.5).toString())

// Properties ----------------------------------------------------------------------------------------------------------
// https://kotlinlang.org/docs/properties.html
// Property is a closed field with access methods, i.e. getters and setters.
// Note: val doesn't have a backing field.

class Student(val name: String = "John Doe", val age: Int) {
    // a property without a setter
    val isYoung: Boolean
        get() {
            return age < 18
        }

    // we could implement a property without a setter by hand, but it's not recommended
    var isHuman: Boolean = true
        set(value) {
            // do nothing
        }
}

val s = Student(age = 16)

// var without a backing field
class AnotherStudent(val name: String = "John Doe", val age: Int) {
    private var graduatingClass = true // <- accessible only inside the class

    var graduating: String
        get() = if (graduatingClass) "yes" else "no"
        set(value) {
            val possibleValues = setOf("yes", "no")
            require(value in possibleValues) {
                "$value is invalid value. Possible values: $possibleValues"
            }
            graduatingClass = value == "yes"
        }
}

val jd = AnotherStudent(age = 25)
jd.graduating = "no"
jd.graduating

// redefining a setter
// https://kotlinlang.org/docs/properties.html#backing-fields
class Teacher(age: Int) {
    var age: Int = age
        set(newAge) {
            require(newAge >= field) {
                "You can't become younger"
            }
            if (newAge > field) {
                println("You're getting older: $field -> $newAge")
            }
            field = newAge
        }
}

val teacher = Teacher(40)
teacher.age
teacher.age = 39 // => java.lang.IllegalArgumentException: You can't become younger

// 💡 https://hugomartins.io/essays/2021/02/using-require-and-check-in-kotlin/

// Interfaces ----------------------------------------------------------------------------------------------------------
// https://kotlinlang.org/docs/interfaces.html

interface Runnable {
    fun run() = println("I can run!")
}

class Tiger : Runnable {
    override fun run() {
        TODO("Not yet implemented")
    }
}

// Interfaces can have a default implementation, but only for methods and properties that don't require data:
interface Chillable {
    fun run() = println("Thanks, no...")
    fun chill() = println("I chill all day long")
}

// A class can implement many interfaces
class CoolKid : Runnable, Chillable {
    // if multiple interfaces describe a method of the same name,
    // the class have to explicitly specify which one to use
    override fun run() = super<Chillable>.run()
}

// Abstract classes ----------------------------------------------------------------------------------------------------
// We can't create instances of abstract classes.
// Abstract classes are opened by default.

abstract class Car {
    abstract fun Drive() // must be overridden
    open fun forOverride() {} // can be overridden
}

// Nested classes ------------------------------------------------------------------------------------------------------
// https://kotlinlang.org/docs/nested-classes.html
// Nested class doesn't have a reference to an outer class

class Outer {
    class Nested {
        // ...
    }
}

// Inner classes -------------------------------------------------------------------------------------------------------
// Inner class does have a reference to an outer class

class AnotherOuter {
    inner class Inner {
        fun getOuterReference(): AnotherOuter = this@AnotherOuter
    }
}

// Sealed classes  -----------------------------------------------------------------------------------------------------
// https://kotlinlang.org/docs/sealed-classes.html
// Sealed classes can't have outside subclasses.

sealed class Oak : Tree(300, TreeType.OAK) {
    // All the subclasses must be defined inside the sealed class
    class MajorOak : Oak() {}
    class SevenSisters : Oak() {}
}

// Sealed classes might be useful when you want to know all the subclasses upfront, e.g.
// to use them in a `when` block.

// Data classes  -------------------------------------------------------------------------------------------------------
// https://kotlinlang.org/docs/data-classes.html
// For an instance of a data class, Kotlin provides default implementations of
// `equals()`, `hashCode()`, `toString()`, `copy()`.

data class User(val name: String, val age: Int)

// Object --------------------------------------------------------------------------------------------------------------
// https://kotlinlang.org/docs/object-declarations.html
// A singleton pattern built-in into the language.

object Forest {
    val trees: MutableList<Tree>

    init {
        trees = mutableListOf()
    }

    val numberOfTrees = trees.size
}

// Companion Object ----------------------------------------------------------------------------------------------------
// https://kotlinlang.org/docs/object-declarations.html#companion-objects
// Kotlin doesn't provide static (class) methods, but we can define a companion object
// that will be unique for all instances of the class.

data class SpecialTree private constructor(val age: Int = 0) {
    companion object {
        fun create() = SpecialTree(0)
        fun create(age: Int) = SpecialTree(age)

        @JvmStatic // for working from Java
        fun staticFunction() {}
    }

    // Nested class == static class Crown in Java
    class Crown {
        // val treeAge = this@Tree
        // we can not do like this
    }

    // Inner class == class Roots in Java, there is an implicit reference
    inner class Roots {
        val treeAge: Int = this@SpecialTree.age
    }
}

// A companion object can implement interfaces.
// 💡Use it for "static" member and factory methods.