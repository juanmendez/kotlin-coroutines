import java.lang.Thread.sleep
import java.util.concurrent.atomic.AtomicInteger
import kotlin.concurrent.thread
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

const val NUMTASKS = 10_000
const val LOOPS = 500
const val WAITMS = 10L

// runBlocking is also a coroutine builder that bridges the non-coroutine world of a regular fun main()
// it is also a coroutine scope, which is a wrapper of child coroutines. in this way it completes when all of them complete.

// lets now use another type of coroutine scope which by the way is suspended, so it doesn't put on hold other code execution
// like runBlocking does, but it runs asynchronously.
// suspend is a qualifier that describes the function as asynchronous.
// Dart for example uses `void usingCoroutines async() {}`
suspend fun usingCoroutines() = coroutineScope {
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

// lets do a simple example
suspend fun sayHelloWorldAsync() = coroutineScope {
    var helloWorld = "!"

    val job1 = launch {
        delay(2000L)
        helloWorld += " world"
        println(helloWorld)
    }

    val job2 = launch {
        delay(1000L)
        helloWorld += "hello"
        println(helloWorld)
    }

    // both coroutines run concurrently
    println(helloWorld)

    // lets join all jobs and wait
    joinAll(job1, job2).apply {
        println("completed: $helloWorld")
    }
}

fun usingThreads() {
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