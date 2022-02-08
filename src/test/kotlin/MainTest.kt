import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class MainTest {
    @Test
    fun `this is just a test1`() = runTest {
        // since method is asynchronous it has to be access through another scope.
        // just like what runBlocking did in the previous branch
        usingCoroutines()
    }

    @Test
    fun `say hello world asyncronosly`() = runTest {
        val result = sayHelloWorldAsync()

        assertEquals(result, "hello world")
    }
}