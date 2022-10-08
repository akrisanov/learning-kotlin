// defining function as a block ----------------------------------------------------------------------------------------

fun max(a: Int, b: Int): Int {
    if (a > b)
        return a
    return b
}

// defining function as an expression ----------------------------------------------------------------------------------

fun max2(a: Int, b: Int): Int = if (a > b) a else b

val times = max2(1, 0)

// loops ---------------------------------------------------------------------------------------------------------------

repeat(times) {
    "Hi!"
}

// ranges --------------------------------------------------------------------------------------------------------------

val oneToTen = 1..10

for (i in oneToTen) println(i)
for (c in 'a'..'z') println(c)
for (i in 10 downTo 1) println(i)
for (i in 3..6 step 2) println(i)

// when expression -----------------------------------------------------------------------------------------------------

enum class FunnyEnum {
    SUN, MUSHROOM, BEETLE
}

fun getFunStyle(funny: FunnyEnum) =
    when (funny) {
        FunnyEnum.SUN -> "Смяяшно!"
        FunnyEnum.MUSHROOM -> "Весяяялоо!"
        FunnyEnum.BEETLE -> "Приуныл"
    }

getFunStyle(FunnyEnum.SUN)

// example of a more complex enum
enum class ColorEnum(private val r: Int, private val g: Int, private val b: Int) {
    RED(255, 0, 0), GREEN(0, 255, 0), BLUE(0, 0, 255);

    fun rgb() = (r * 256 + g) * 256 + b
}

ColorEnum.BLUE.rgb()
ColorEnum.RED