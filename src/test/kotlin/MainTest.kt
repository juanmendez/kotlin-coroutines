import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull
import org.junit.jupiter.api.Test

class MainTest {
    @Test
    fun `wait for coroutine to be done`() = runTest {
        val job = launch {
            delay(1000)
            println("hello world")
        }

        // little arrow on the left (->) means we call a suspend function
        job.join()
        println("we waited for job to be done")
    }

    @Test
    fun `how to stop a coroutine`() = runTest {
        // cancellation is cooperative
        // you have to check for cancellation, so it can be cancelled
        val job = launch {
            repeat(1000) {
                delay(10)
                print(".")
            }
        }

        delay(250)

        // So join is addressed once cancel finishes.
        // If a parent coroutine invoking cancelAndJoin on a child coroutine throws
        // CancellationException then it means the child had failed, since a failure of a child coroutine cancels parent by default
        job.cancelAndJoin() // same as job.cancel and then job.join
        println("done")
    }

    @Test
    fun `how to timeout`() = runTest {
        var times = 0
        val job = withTimeoutOrNull(97) {
            repeat(1000) {
                print("${++times}, ")
                delay(5)
            }
        }

        if (job == null) {
            println("\nbuilder time out")
        }
    }

    @Test
    fun `how to timeout with exception`() = runTest {
        var times = 0
        try {
            // when a coroutine cancels without completion, this is when the exception gets caught
            withTimeout(97) {
                repeat(1000) {
                    print("${++times}, ")
                    delay(5)
                }
            }
        } catch (e: TimeoutCancellationException) {
            println("\nEXCEPTION CAUGHT ${e.message}")
        }
    }

    @Test
    fun `breaking the rules of structured concurrency` () = runTest {

        suspend  fun runWithGlobalScope() {
            GlobalScope.launch {
                println("scope 1")
                delay(1000)
            }

            GlobalScope.launch {
                println("scope 2")
                delay(2000)
            }

            println("scope 0")
        }

        launch {
            runWithGlobalScope()
            println("fn completed")
        }

        /***
         * printed result
         *  scope 1
         *  scope 0
         *  fn completed
         *  scope 2
         */
    }


    @Test
    fun `backing up the rules of structured concurrency` () = runTest {

        // 0 hosting coroutine waits for children to finish
        suspend  fun runWithLocalScope()  = coroutineScope {
            launch {
                println("scope 1")
                delay(1000)
            }

            launch {
                println("scope 2")
                delay(2000)
            }

            println("scope 0")
        }

        launch {
            runWithLocalScope()

            // 1 once top coroutine finishes, then this gets printed
            println("fn completed")
        }

        /***
         * printed result
         *  scope 0
         *  scope 1
         *  scope 2
         *  fn completed
         */
    }
}