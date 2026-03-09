package ltd.idcu.est.examples.kotlin

import ltd.idcu.est.kotlin.extensions.*
import ltd.idcu.est.collection.api.Seq
import ltd.idcu.est.collection.api.Map as EstMap

fun main() {
    println("=".repeat(80))
    println("  EST Framework Kotlin 扩展函数示例")
    println("=".repeat(80))
    
    example1_CollectionExtensions()
    println()
    example2_StringExtensions()
    println()
    example3_TypeConversions()
    println()
    println("=".repeat(80))
    println("  所有示例运行完成！")
    println("=".repeat(80))
}

fun example1_CollectionExtensions() {
    println("\n【示例 1】集合扩展函数")
    println("-".repeat(80))
    
    val list = listOf(1, 2, 3, 4, 5)
    
    println("原始列表: $list")
    
    val evenNumbers = list.filter { it % 2 == 0 }
    println("偶数过滤: $evenNumbers")
    
    val doubled = list.map { it * 2 }
    println("双倍映射: $doubled")
    
    val sum = list.reduce { acc, i -> acc + i }
    println("求和: $sum")
    
    val first = list.firstOrNull()
    println("第一个元素: $first")
    
    val last = list.lastOrNull()
    println("最后一个元素: $last")
    
    println()
    println("链式调用示例:")
    val result = list
        .filter { it > 2 }
        .map { it * 3 }
        .sum()
    println("  结果: $result")
}

fun example2_StringExtensions() {
    println("\n【示例 2】字符串扩展函数")
    println("-".repeat(80))
    
    val str = "Hello, EST Framework!"
    
    println("原始字符串: \"$str\"")
    
    val reversed = str.reversed()
    println("反转: \"$reversed\"")
    
    val uppercase = str.uppercase()
    println("大写: \"$uppercase\"")
    
    val lowercase = str.lowercase()
    println("小写: \"$lowercase\"")
    
    val trimmed = "  Hello  ".trim()
    println("去除空白: \"$trimmed\"")
    
    val contains = str.contains("EST")
    println("包含 'EST': $contains")
    
    val startsWith = str.startsWith("Hello")
    println("以 'Hello' 开头: $startsWith")
    
    val endsWith = str.endsWith("!")
    println("以 '!' 结尾: $endsWith")
    
    val split = str.split(", ")
    println("分割: $split")
    
    val substring = str.substring(7, 10)
    println("子串 (7-10): \"$substring\"")
}

fun example3_TypeConversions() {
    println("\n【示例 3】类型转换")
    println("-".repeat(80))
    
    val strNum = "123"
    val intNum = strNum.toIntOrNull()
    println("字符串 '$strNum' 转整数: $intNum")
    
    val strDouble = "3.14"
    val doubleNum = strDouble.toDoubleOrNull()
    println("字符串 '$strDouble' 转双精度: $doubleNum")
    
    val boolStr = "true"
    val bool = boolStr.toBooleanStrictOrNull()
    println("字符串 '$boolStr' 转布尔: $bool")
    
    val intToStr = 456.toString()
    println("整数 456 转字符串: \"$intToStr\"")
    
    val doubleToStr = 2.718.toString()
    println("双精度 2.718 转字符串: \"$doubleToStr\"")
    
    val boolToStr = false.toString()
    println("布尔 false 转字符串: \"$boolToStr\"")
    
    println()
    println("安全转换示例:")
    val invalidNum = "abc"
    val safeInt = invalidNum.toIntOrNull() ?: 0
    println("  无效字符串 '$invalidNum' 安全转换: $safeInt")
}
