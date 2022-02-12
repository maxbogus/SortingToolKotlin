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
    var largestLine = ""
    for (line in lines) {
        if (line.length > largestLine.length) {
            largestLine = line
        }
    }
    val count = lines.count { it == largestLine }

    println("Total lines: ${lines.size}.")
    println("The longest line:")
    println(largestLine)
    println("(${count} time(s), ${calculatePercentage(count.toFloat(), lines.size.toFloat())}%).")
}

fun findLongestWord(lines: MutableList<String>) {
    var longestWord = ""
    var longestWordCounter = 1
    var totalWordsCounter = 0
    for (line in lines) {
        val words = line.split("\\s+".toRegex())
        for (word in words) {
            if (word.length > longestWord.length) {
                longestWord = word
                longestWordCounter = 1
            } else if (word == longestWord) {
                longestWordCounter += 1
            }
            totalWordsCounter++
        }
    }
    println("Total words: ${totalWordsCounter}.")
    println(
        "The longest word: $longestWord (${longestWordCounter} time(s), ${
            calculatePercentage(
                longestWordCounter.toFloat(),
                totalWordsCounter.toFloat()
            )
        }%)."
    )
}

private fun calculatePercentage(longestWordCounter: Float, totalWordsCounter: Float) =
    (longestWordCounter / (totalWordsCounter / 100.0)).toInt()

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
