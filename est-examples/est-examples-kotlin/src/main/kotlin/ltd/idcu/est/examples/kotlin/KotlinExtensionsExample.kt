package ltd.idcu.est.examples.kotlin

import ltd.idcu.est.kotlin.extensions.*
import ltd.idcu.est.collection.api.Seq
import ltd.idcu.est.collection.api.Map as EstMap

fun main() {
    println("=".repeat(80))
    println("  EST Framework Kotlin Extension Functions Example")
    println("=".repeat(80))
    
    example1_CollectionExtensions()
    println()
    example2_StringExtensions()
    println()
    example3_TypeConversions()
    println()
    println("=".repeat(80))
    println("  All examples completed!")
    println("=".repeat(80))
}

fun example1_CollectionExtensions() {
    println("\n[Example 1] Collection Extension Functions")
    println("-".repeat(80))
    
    val list = listOf(1, 2, 3, 4, 5)
    
    println("Original list: $list")
    
    val evenNumbers = list.filter { it % 2 == 0 }
    println("Even numbers filter: $evenNumbers")
    
    val doubled = list.map { it * 2 }
    println("Doubled mapping: $doubled")
    
    val sum = list.reduce { acc, i -> acc + i }
    println("Sum: $sum")
    
    val first = list.firstOrNull()
    println("First element: $first")
    
    val last = list.lastOrNull()
    println("Last element: $last")
    
    println()
    println("Chaining example:")
    val result = list
        .filter { it > 2 }
        .map { it * 3 }
        .sum()
    println("  Result: $result")
}

fun example2_StringExtensions() {
    println("\n[Example 2] String Extension Functions")
    println("-".repeat(80))
    
    val str = "Hello, EST Framework!"
    
    println("Original string: \"$str\"")
    
    val reversed = str.reversed()
    println("Reversed: \"$reversed\"")
    
    val uppercase = str.uppercase()
    println("Uppercase: \"$uppercase\"")
    
    val lowercase = str.lowercase()
    println("Lowercase: \"$lowercase\"")
    
    val trimmed = "  Hello  ".trim()
    println("Trimmed whitespace: \"$trimmed\"")
    
    val contains = str.contains("EST")
    println("Contains 'EST': $contains")
    
    val startsWith = str.startsWith("Hello")
    println("Starts with 'Hello': $startsWith")
    
    val endsWith = str.endsWith("!")
    println("Ends with '!': $endsWith")
    
    val split = str.split(", ")
    println("Split: $split")
    
    val substring = str.substring(7, 10)
    println("Substring (7-10): \"$substring\"")
}

fun example3_TypeConversions() {
    println("\n[Example 3] Type Conversions")
    println("-".repeat(80))
    
    val strNum = "123"
    val intNum = strNum.toIntOrNull()
    println("String '$strNum' to integer: $intNum")
    
    val strDouble = "3.14"
    val doubleNum = strDouble.toDoubleOrNull()
    println("String '$strDouble' to double: $doubleNum")
    
    val boolStr = "true"
    val bool = boolStr.toBooleanStrictOrNull()
    println("String '$boolStr' to boolean: $bool")
    
    val intToStr = 456.toString()
    println("Integer 456 to string: \"$intToStr\"")
    
    val doubleToStr = 2.718.toString()
    println("Double 2.718 to string: \"$doubleToStr\"")
    
    val boolToStr = false.toString()
    println("Boolean false to string: \"$boolToStr\"")
    
    println()
    println("Safe conversion example:")
    val invalidNum = "abc"
    val safeInt = invalidNum.toIntOrNull() ?: 0
    println("  Invalid string '$invalidNum' safe conversion: $safeInt")
}
