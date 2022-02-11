package sorting

fun main() {
    val lineNumbers = mutableListOf<String>()
    do {
        val input = readLine()
        if (input != null) {
            lineNumbers.add(input)
        }
    } while (input != null)

    val numbers: MutableList<Int> = mutableListOf()
    for (line in lineNumbers) {
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
