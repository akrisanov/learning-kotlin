import kotlin.properties.Delegates

// https://kotlinlang.org/docs/destructuring-declarations.html
// Destructuring declarations are automatically created by a compiler for data classes.

data class Point(val x: Int, val y: Int)

val p = Point(10, 20)
val (x, y) = p // destructuring declaration

for ((key, value) in mapOf("a" to "b", "c" to "d")) {
    println("$key -> $value")
}

// Another example
data class NamedComponents(val name: String, val extension: String)

fun splitFileName(fullName: String): NamedComponents {
    val result = fullName.split(".", limit = 2)
    return NamedComponents(result[0], result[1])
}

val (name, ext) = splitFileName("example.kt")

// Destructuring declarations can be defined by hand with component functions
class Point2D(val x: Int, val y: Int) {
    operator fun component1() = x
    operator fun component2() = y
}
val (x1, y1) = Point2D(1, 0)

// Delegated properties ------------------------------------------------------------------------------------------------
// https://kotlinlang.org/docs/delegated-properties.html

/* Delegation pattern is embedded into the language.

class Klass {
    var p: Type by Delegate()
}
 */

/* Compiler generates a hidden property

class Foo {
    private val delegate = Delegate()

    var p: Type
        set(value: Type) = delegate.setValue(..., value)
        get() = delegate.getValue(...)
}
 */

/*
A delegate must have getValue() and setValue() methods (for var).
They can also be extensions.

class Delegate {
    operator fun getValue(...) {...}
    operator fun setValue(..., value: Type) {...}
}

now we can do the following

val obj = Klass()
val oldValue = obj.p
obj.p = newValue

now the compiler will delegate a property methods to the Delegate class.
 */

// Example
class User(val map: Map<String, Any?>) {
    val name: String by map
    val age: Int by map
}

val user = User(mapOf("name" to "John Doe", "age" to 25))

user.name // John Doe
user.age  // 25

/*
We can delegate stuff to:
    property in the same file
    another property of the same class or to a property of the extension
    another foreign property
 */

var topLevelInt: Int = 0

class ClassWithDelegate(val anotherClassInt: Int)

class MyClass(var memberInt: Int, val anotherClassInstance: ClassWithDelegate) {
    var delegatedToMember: Int by this::memberInt
    var delegatedToTopLevel: Int by ::topLevelInt
    val delegatedToAnotherClass: Int by anotherClassInstance::anotherClassInt
}

var MyClass.extensionDelegated: Int by ::topLevelInt

// Lazy properties -----------------------------------------------------------------------------------------------------
// Lazy properties are initialized with their first getter call.

val value: String by lazy {
    println("init")
    "lazyval"
}

fun main() {
    println(value) // "init", "lazyval"
    println(value) // lazyval
    println(value) // lazyval
}

main()

// Observable properties -----------------------------------------------------------------------------------------------
// kotlin.properties.Delegates

class Person {
    var name: String by Delegates.observable("<no name>") { prop, old, new ->
        println("$old -> $new")
    }
}

val person = Person()
person.name = "first" // <no name> -> first
person.name = "second" // first -> second

// Vetoable properties -------------------------------------------------------------------------------------------------
// https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.properties/-delegates/vetoable.html

var max: Int by Delegates.vetoable(0) { property, oldValue, newValue -> newValue > oldValue }

max // 0

max = 10
max // 10

max = 5
max // 10