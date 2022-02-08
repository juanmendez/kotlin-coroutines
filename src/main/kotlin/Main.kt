import java.lang.Thread.sleep
import java.util.concurrent.atomic.AtomicInteger
import kotlin.concurrent.thread
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

const val NUMTASKS = 10_000
const val LOOPS = 500
const val WAITMS = 10L

// runBlocking is also a coroutine builder that bridges the non-coroutine world of a regular fun main()
fun usingCoroutines() = runBlocking {
    val result = AtomicInteger()

    // Job is a lifecycle observer/controller of a coroutine
    val jobs = mutableListOf<Job>()

    for (i in 1..NUMTASKS) {
        jobs.add(
            // launch is a coroutine builder and returns a job. It launches a new coroutine concurrently with the rest of the code.
            // think of coroutine as a lightweight thread. here each coroutine mocks what goes in `usingThreads` method
            launch(Dispatchers.IO) {
                for (x in 1..LOOPS) {
                    // delay is a special suspending function. It suspends the coroutine for a specific time.
                    delay(WAITMS)
                }

                println("result: $result")
                result.getAndIncrement()
            }
        )
    }
}

fun usingThreads(args: Array<String>) {
    val result = AtomicInteger()
    val threads = mutableListOf<Thread>()

    for (i in 1..NUMTASKS) {
        threads.add(
            thread {
                for (x in 1..LOOPS) {
                    sleep(WAITMS)
                }

                result.getAndIncrement()
            }
        )
    }
}