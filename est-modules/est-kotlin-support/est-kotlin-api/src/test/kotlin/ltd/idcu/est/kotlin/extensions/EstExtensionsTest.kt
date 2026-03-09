package ltd.idcu.est.kotlin.extensions

import org.junit.jupiter.api.Test
import java.time.Duration
import java.time.Instant
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class EstExtensionsTest {

    @Test
    fun testNullIfBlank() {
        val nullString: String? = null
        assertNull(nullString.nullIfBlank())
        
        val emptyString = ""
        assertNull(emptyString.nullIfBlank())
        
        val blankString = "   "
        assertNull(blankString.nullIfBlank())
        
        val nonBlankString = "test"
        assertEquals("test", nonBlankString.nullIfBlank())
    }

    @Test
    fun testOrEmpty() {
        val nullString: String? = null
        assertEquals("", nullString.orEmpty())
        
        val nonNullString = "test"
        assertEquals("test", nonNullString.orEmpty())
    }

    @Test
    fun testTruncate() {
        val shortString = "Hello"
        assertEquals("Hello", shortString.truncate(10))
        
        val longString = "Hello World, this is a long string"
        val truncated = longString.truncate(15)
        assertTrue(truncated.length <= 15)
        assertTrue(truncated.endsWith("..."))
        
        val customSuffix = longString.truncate(15, " [more]")
        assertTrue(customSuffix.endsWith(" [more]"))
    }

    @Test
    fun testListNullOrEmpty() {
        val nullList: List<String>? = null
        assertNull(nullList.nullIfEmpty())
        
        val emptyList = emptyList<String>()
        assertNull(emptyList.nullIfEmpty())
        
        val nonEmptyList = listOf("a", "b")
        assertEquals(listOf("a", "b"), nonEmptyList.nullIfEmpty())
    }

    @Test
    fun testListOrEmpty() {
        val nullList: List<String>? = null
        assertEquals(emptyList(), nullList.orEmpty())
        
        val nonEmptyList = listOf("a", "b")
        assertEquals(listOf("a", "b"), nonEmptyList.orEmpty())
    }

    @Test
    fun testMutableListRemoveIf() {
        val list = mutableListOf(1, 2, 3, 4, 5)
        val removed = list.removeIf { it % 2 == 0 }
        assertTrue(removed)
        assertEquals(listOf(1, 3, 5), list)
        
        val noMatchList = mutableListOf(1, 3, 5)
        val noRemoved = noMatchList.removeIf { it % 2 == 0 }
        assertFalse(noRemoved)
    }

    @Test
    fun testMapNullOrEmpty() {
        val nullMap: Map<String, Int>? = null
        assertNull(nullMap.nullIfEmpty())
        
        val emptyMap = emptyMap<String, Int>()
        assertNull(emptyMap.nullIfEmpty())
        
        val nonEmptyMap = mapOf("a" to 1)
        assertEquals(mapOf("a" to 1), nonEmptyMap.nullIfEmpty())
    }

    @Test
    fun testMapOrEmpty() {
        val nullMap: Map<String, Int>? = null
        assertEquals(emptyMap(), nullMap.orEmpty())
        
        val nonEmptyMap = mapOf("a" to 1)
        assertEquals(mapOf("a" to 1), nonEmptyMap.orEmpty())
    }

    @Test
    fun testIfNull() {
        val nullValue: String? = null
        val result = nullValue.ifNull { "default" }
        assertEquals("default", result)
        
        val nonNullValue = "test"
        val result2 = nonNullValue.ifNull { "default" }
        assertEquals("test", result2)
    }

    @Test
    fun testIfNullOrEmpty() {
        val nullValue: String? = null
        val result = nullValue.ifNullOrEmpty { "default" }
        assertEquals("default", result)
        
        val emptyValue = ""
        val result2 = emptyValue.ifNullOrEmpty { "default" }
        assertEquals("default", result2)
        
        val nonEmptyValue = "test"
        val result3 = nonEmptyValue.ifNullOrEmpty { "default" }
        assertEquals("test", result3)
    }

    @Test
    fun testTakeIf() {
        val value = "test"
        val result = value.takeIf { it.length > 2 }
        assertEquals("test", result)
        
        val result2 = value.takeIf { it.length > 10 }
        assertNull(result2)
    }

    @Test
    fun testTakeUnless() {
        val value = "test"
        val result = value.takeUnless { it.length > 10 }
        assertEquals("test", result)
        
        val result2 = value.takeUnless { it.length > 2 }
        assertNull(result2)
    }

    @Test
    fun testCoalesce() {
        val nullValue: String? = null
        val result = nullValue coalesce "default"
        assertEquals("default", result)
        
        val nonNullValue = "test"
        val result2 = nonNullValue coalesce "default"
        assertEquals("test", result2)
    }

    @Test
    fun testLetIf() {
        val value = "test"
        val result = value.letIf(true) { it.uppercase() }
        assertEquals("TEST", result)
        
        val result2 = value.letIf(false) { it.uppercase() }
        assertNull(result2)
        
        val nullValue: String? = null
        val result3 = nullValue.letIf(true) { it.uppercase() }
        assertNull(result3)
    }

    @Test
    fun testApplyIf() {
        val sb = StringBuilder()
        val result = sb.applyIf(true) { append("test") }
        assertEquals("test", result.toString())
        
        val sb2 = StringBuilder()
        val result2 = sb2.applyIf(false) { append("test") }
        assertEquals("", result2.toString())
    }

    @Test
    fun testDurationToHumanReadable() {
        val seconds = Duration.ofSeconds(30)
        assertEquals("30s", seconds.toHumanReadable())
        
        val minutes = Duration.ofSeconds(90)
        assertEquals("1m 30s", minutes.toHumanReadable())
        
        val hours = Duration.ofSeconds(3665)
        assertEquals("1h 1m", hours.toHumanReadable())
    }

    @Test
    fun testInstantElapsed() {
        val past = Instant.now().minusSeconds(60)
        val elapsed = past.elapsed()
        assertTrue(elapsed.seconds >= 60)
    }

    @Test
    fun testInstantIsOlderThan() {
        val past = Instant.now().minusSeconds(120)
        assertTrue(past.isOlderThan(Duration.ofSeconds(60)))
        
        val recent = Instant.now().minusSeconds(30)
        assertFalse(recent.isOlderThan(Duration.ofSeconds(60)))
    }

    @Test
    fun testInstantIsNewerThan() {
        val past = Instant.now().minusSeconds(120)
        assertFalse(past.isNewerThan(Duration.ofSeconds(60)))
        
        val recent = Instant.now().minusSeconds(30)
        assertTrue(recent.isNewerThan(Duration.ofSeconds(60)))
    }

    @Test
    fun testTimer() {
        val (result, duration) = timer {
            Thread.sleep(100)
            "done"
        }
        assertEquals("done", result)
        assertTrue(duration.toMillis() >= 100)
    }

    @Test
    fun testListPaginate() {
        val list = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        
        val page0 = list.paginate(0, 3)
        assertEquals(listOf(1, 2, 3), page0)
        
        val page1 = list.paginate(1, 3)
        assertEquals(listOf(4, 5, 6), page1)
        
        val page3 = list.paginate(3, 3)
        assertEquals(listOf(10), page3)
        
        val page10 = list.paginate(10, 3)
        assertTrue(page10.isEmpty())
    }

    @Test
    fun testListGroupByIntoList() {
        val list = listOf(1, 2, 3, 4, 5)
        val result = list.groupByIntoList { it % 2 }
        assertEquals(2, result.size)
        assertTrue(result.containsKey(0))
        assertTrue(result.containsKey(1))
    }

    @Test
    fun testListAssociateByNonNull() {
        val list = listOf("a", "b", null, "c")
        val result = list.associateByNonNull({ it }, { it?.uppercase() })
        assertEquals(3, result.size)
        assertEquals("A", result["a"])
        assertEquals("B", result["b"])
        assertEquals("C", result["c"])
    }

    @Test
    fun testStringToCamelCase() {
        assertEquals("helloWorld", "hello_world".toCamelCase())
        assertEquals("helloWorld", "hello-world".toCamelCase())
        assertEquals("helloWorld", "hello world".toCamelCase())
        assertEquals("helloWorldTest", "hello_world_test".toCamelCase())
    }

    @Test
    fun testStringToSnakeCase() {
        assertEquals("hello_world", "helloWorld".toSnakeCase())
        assertEquals("hello_world", "HelloWorld".toSnakeCase())
        assertEquals("hello_world", "hello world".toSnakeCase())
        assertEquals("hello_world", "hello-world".toSnakeCase())
    }

    @Test
    fun testStringToKebabCase() {
        assertEquals("hello-world", "helloWorld".toKebabCase())
        assertEquals("hello-world", "HelloWorld".toKebabCase())
        assertEquals("hello-world", "hello world".toKebabCase())
        assertEquals("hello-world", "hello_world".toKebabCase())
    }
}
