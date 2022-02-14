package sorting

import java.io.File

private const val SORT_ARGUMENT = "-sortingType"
private const val DATA_TYPE_ARGUMENT = "-dataType"
private const val DATA_INPUT_ARGUMENT = "-inputFile"
private const val DATA_OUTPUT_ARGUMENT = "-outputFile"

fun main(args: Array<String>) {
    if (args.size % 2 != 0) {
        println(
            if (args.contains(SORT_ARGUMENT)) {
                "No sorting type defined!"
            } else {
                "No data type defined!"
            }
        )
    } else {
        val settings: MutableMap<String, String> = mutableMapOf(
            SORT_ARGUMENT to "natural",
            DATA_TYPE_ARGUMENT to "word",
            DATA_INPUT_ARGUMENT to "none",
            DATA_OUTPUT_ARGUMENT to "none"
        )

        setupSettings(args, settings)

        val lines: MutableList<String>? =
            if (settings[DATA_INPUT_ARGUMENT] == "none") parseInput() else settings[DATA_INPUT_ARGUMENT]?.let {
                parseFile(
                    it
                )
            }

        if (lines != null) {
            when (settings[SORT_ARGUMENT]) {
                "natural" -> sortNaturally(lines, settings)
                "byCount" -> sortByCount(lines, settings)
            }
        }

    }
}

fun parseFile(fileName: String): MutableList<String> {
    return File(fileName).readLines().toMutableList()
}

fun sortNaturally(lines: MutableList<String>, settings: MutableMap<String, String>) {
    when (settings[DATA_TYPE_ARGUMENT]) {
        "long" -> sortLongsNaturally(lines, settings)
        "word" -> sortWordsNaturally(lines, settings)
        else -> sortLines(lines, settings)
    }
}

fun sortByCount(lines: MutableList<String>, settings: MutableMap<String, String>) {
    when (settings[DATA_TYPE_ARGUMENT]) {
        "long" -> sortLongsByCount(lines, settings)
        "word" -> sortWordsByCount(lines, settings)
        else -> sortLinesByCount(lines, settings)
    }
}

fun sortLinesByCount(lines: MutableList<String>, settings: MutableMap<String, String>) {
    val set = mutableSetOf<String>()
    set.addAll(lines)
    val output: MutableList<String> = mutableListOf()
    output.add("Total lines: ${lines.size}")
    for (line in sortLexicographically(set.toMutableList())) {
        output.add(line)
    }
    provideOutput(output, settings)
}

private fun provideOutput(
    output: MutableList<String>,
    settings: MutableMap<String, String>
) {
    for (line in output) {
        if (settings[DATA_OUTPUT_ARGUMENT] == "none") {
            println(line)
        } else {
            File("${settings[DATA_OUTPUT_ARGUMENT]}").appendText(line)
        }
    }
}

fun sortWordsByCount(lines: MutableList<String>, settings: MutableMap<String, String>) {
    val unsortedArray = parseStringsIntoArray(lines)

    val map = generateMapOfUnsortedArray(unsortedArray)
    val keys = generateSortedListOfKeys(map)

    provideOutput(displaySortedItemsByAsStrings(keys, map, unsortedArray), settings)
}

private fun generateSortedListOfKeys(map: MutableMap<Int, String>): MutableList<Int> {
    var keys = mutableListOf<Int>()
    map.keys.forEach { keys.add(it) }
    keys = mergeSort(keys).toMutableList()
    return keys
}

fun sortLongsByCount(lines: MutableList<String>, settings: MutableMap<String, String>) {
    val unsortedArray = parseStringsIntoArray(lines)
    val map = generateMapOfUnsortedArray(unsortedArray)

    val keys = generateSortedListOfKeys(map)

    provideOutput(storeOutput(keys, map, unsortedArray), settings)
}

private fun storeOutput(
    keys: MutableList<Int>,
    map: MutableMap<Int, String>,
    unsortedArray: MutableList<Int>,
    type: String = "longs"
): MutableList<String> {
    val mutableList: MutableList<String> = mutableListOf("Total ${type}: ${unsortedArray.size}.")
    for (key in keys) {
        val items = map[key]?.split(" ")?.map { it.toInt() }?.toMutableList()
        if (items != null) {
            val sortedItems = mergeSort(items)
            for (item in sortedItems) {
                val countItemInUnsorted = unsortedArray.count { it == item }
                mutableList.add(
                    "${item}: $key time(s), ${
                        calculatePercentage(
                            countItemInUnsorted.toFloat(),
                            unsortedArray.size.toFloat()
                        )
                    }%"
                )
            }
        }
    }
    return mutableList
}

private fun displaySortedItemsByAsStrings(
    keys: MutableList<Int>,
    map: MutableMap<Int, String>,
    unsortedArray: MutableList<Int>,
): MutableList<String> {
    val list = mutableListOf<String>("Total numbers: ${unsortedArray.size}.")
    for (key in keys) {
        val items = map[key]?.split(" ")?.toMutableList()
        if (items != null) {
            val sortedItems = sortLexicographically(items)
            for (item in sortedItems) {
                val countItemInUnsorted = unsortedArray.count { it == item.toInt() }
                list.add(
                    "${item}: $key time(s), ${
                        calculatePercentage(
                            countItemInUnsorted.toFloat(),
                            unsortedArray.size.toFloat()
                        )
                    }%"
                )
            }
        }
    }
    return list
}

fun sortLexicographically(items: MutableList<String>): List<String> {
    for (firstWordIndex in 0..items.size - 2) {
        for (secondWordIndex in firstWordIndex + 1 until items.size) {
            if (items[firstWordIndex].compareTo(items[secondWordIndex]) > 0) {
                val temp = items[firstWordIndex]
                items[firstWordIndex] = items[secondWordIndex]
                items[secondWordIndex] = temp
            }
        }
    }
    return items.toList()
}

private fun generateMapOfUnsortedArray(unsortedArray: MutableList<Int>): MutableMap<Int, String> {
    // get unique list of items
    val set = mutableSetOf<Int>()
    unsortedArray.forEach { set.add(it) }

    // generate map and fill it with count / date entry
    val map = mutableMapOf<Int, String>()

    for (item in set) {
        val numberOfItems = unsortedArray.count { it == item }
        if (map.containsKey(numberOfItems)) {
            map[numberOfItems] += " $item"
        } else {
            map[numberOfItems] = "$item"
        }
    }
    return map
}

private fun generateMapOfUnsortedArrayOfStrings(unsortedArray: MutableList<String>): MutableMap<Int, String> {
    // get unique list of items
    val set = mutableSetOf<String>()
    unsortedArray.forEach { set.add(it) }

    // generate map and fill it with count / date entry
    val map = mutableMapOf<Int, String>()

    for (item in set) {
        val numberOfItems = unsortedArray.count { it == item }
        if (map.containsKey(numberOfItems)) {
            map[numberOfItems] += "|$item"
        } else {
            map[numberOfItems] = item
        }
    }
    return map
}

fun sortLines(lines: MutableList<String>, settings: MutableMap<String, String>) {
    TODO("Not yet implemented")
}

fun sortWordsNaturally(lines: MutableList<String>, settings: MutableMap<String, String>) {
    val unsortedArray = parseStringsIntoArray(lines)
    TODO("Not yet implemented")
}

private fun setupSettings(
    args: Array<String>,
    settings: MutableMap<String, String>
) {
    val options = mutableListOf<String>()
    val values = mutableListOf<String>()
    for ((counter, arg) in args.withIndex()) {
        if (counter % 2 == 0) {
            options.add(arg)
        } else {
            values.add(arg)
        }
    }

    for (index in 0 until options.size) {
        settings[options[index]] = values[index]
    }
}

fun sortLongsNaturally(lines: MutableList<String>, settings: MutableMap<String, String>) {
    val unsortedArray = parseStringsIntoArray(lines)

    val sortedArray: List<Int> = mergeSort(unsortedArray)

    println("Total numbers: ${sortedArray.size}.")
    println("Sorted data: ${sortedArray.joinToString(" ")}")
}

fun mergeSort(unsortedArray: MutableList<Int>): List<Int> {
    if (unsortedArray.size <= 1) {
        return unsortedArray
    }
    var leftList = unsortedArray.subList(0, unsortedArray.size / 2).toMutableList()
    var rightList = unsortedArray.subList(unsortedArray.size / 2, unsortedArray.lastIndex + 1).toMutableList()

    leftList = mergeSort(leftList) as MutableList<Int>
    rightList = mergeSort(rightList) as MutableList<Int>

    return mergeList(leftList, rightList)
}

fun mergeList(leftList: MutableList<Int>, rightList: MutableList<Int>): List<Int> {
    val result = mutableListOf<Int>()

    do {
        if (leftList.first() <= rightList.first()) {
            result.add(leftList.first())
            leftList.removeAt(0)
        } else {
            result.add(rightList.first())
            rightList.removeAt(0)
        }
    } while (leftList.size > 0 && rightList.size > 0)

    do {
        if (leftList.size > 0) {
            result.add(leftList.first())
            leftList.removeAt(0)
        }
    } while (leftList.size > 0)

    do {
        if (rightList.size > 0) {
            result.add(rightList.first())
            rightList.removeAt(0)
        }
    } while (rightList.size > 0)

    return result
}

private fun parseStringsIntoArray(lines: MutableList<String>): MutableList<Int> {
    val unsortedArray = mutableListOf<Int>()
    if (lines.filter { it != "" }.isEmpty()) {
        return unsortedArray
    }
    lines.forEach { s -> s.split("\\s+".toRegex()).map { it.toInt() }.forEach { unsortedArray.add(it) } }
    return unsortedArray
}

private fun calculatePercentage(longestWordCounter: Float, totalWordsCounter: Float) =
    (longestWordCounter / (totalWordsCounter / 100.0)).toInt()

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
