import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class MainTest {
    @Test
    fun `this is just a test1`() = runTest {
        holdCoroutineUntilCompletes()
    }

    @Test
    fun `coroutines with cancellation`() = runTest {
        // we can use timeouts as well.. :)
        cancelCoroutine(250)
    }

    @Test
    fun `coroutines with time-out`() = runTest {
        timeOutCoroutine(300)
    }

    @Test
    fun `coroutines time-out with no exception`() = runTest {
        val job = timeOutWithNoException(300)

        if (job == null) {
            println("done")
        }
    }
}