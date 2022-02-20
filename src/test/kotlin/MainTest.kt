import kotlinx.coroutines.delay
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class MainTest {
    @Test
    fun `wait for coroutine to be done`() = runTest {
        // if we only use the same scope, then we run into issues while cancelling.
        // we need to make new scopes which derive from testScope
        // @see https://codingwithmohit.com/coroutines/custom-coroutine-context-uses-cases/
        val activity = Activity(coroutineContext)
        val fragment = Fragment(coroutineContext)

        // initiate both..
        println("both views are in the background, $currentTime")
        activity.onDock()
        fragment.onDock()

        delay(2000)

        fragment.onUnDock()
        println("fragment is in the background, $currentTime")

        delay(500)
        println("fragment is in the foreground, $currentTime")
        fragment.onDock()

        delay(2000)
        println("both views are in the background, $currentTime")
        fragment.onUnDock()
        activity.onUnDock()

        /**
         * TestScopeImpl was cancelled
         * kotlinx.coroutines.JobCancellationException: TestScopeImpl was cancelled; job=TestScope[test ended]
         * cancel()
         */
    }
}