package sorting

fun main(args: Array<String>) {
    val type = if (args[0] == "-dataType") {
        args[1]
    } else {
        "word"
    }

    val lines: MutableList<String> = parseInput()

    when (type) {
        "long" -> countGreatestNumber(lines)
        "word" -> findLongestWord(lines)
        else -> findLongestLine(lines)
    }
}

fun findLongestLine(lines: MutableList<String>) {
    TODO("Not yet implemented")
}

fun findLongestWord(lines: MutableList<String>) {
    TODO("Not yet implemented")
}

private fun countGreatestNumber(lines: MutableList<String>) {
    val numbers: MutableList<Int> = mutableListOf()
    for (line in lines) {
        for (number in line.split("\\s+".toRegex()).map { it.toInt() }) {
            numbers.add(number)
        }
    }

    var greatestNumber: Int = Int.MIN_VALUE
    var counter = 0
    for (number in numbers) {
        if (number > greatestNumber) {
            greatestNumber = number
            counter = 1
        } else if (number == greatestNumber) {
            counter++
        }
    }

    println("Total numbers: ${numbers.size}.")
    println("The greatest number: $greatestNumber (${counter} time(s)).")
}

private fun parseInput(): MutableList<String> {
    val lines = mutableListOf<String>()
    do {
        val input = readLine()
        if (input != null) {
            lines.add(input)
        }
    } while (input != null)
    return lines
}
