import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull

suspend fun holdCoroutineUntilCompletes() = coroutineScope {
    val job = launch {
        delay(1000)
        println("hello")
    }

    // Suspends the coroutine until this job is complete
    job.join()
    println("world")

    // without join()... world and then hello
}

suspend fun cancelCoroutine(cancelAtSeconds: Long? = null) = coroutineScope {
    val job = launch {
        repeat(1000) {
            delay(10)
            print(".")
        }
    }

    // wait until it completes
    cancelAtSeconds?.let { seconds ->
        delay(seconds)
        job.cancel()
    } ?: run {
        job.join()
    }


    println("done")
}

suspend fun timeOutCoroutine(cancelAtSeconds: Long? = null) = coroutineScope {
    // wait until it completes
    cancelAtSeconds?.let { seconds ->
        try {
            withTimeout(seconds) {
                repeat(1000) {
                    delay(10)
                    print(".")
                }
            }
        } catch (e: TimeoutCancellationException) {
            println("\n$e")
        }

    } ?: run {
        launch {
            repeat(1000) {
                delay(10)
                print(".")
            }
        }.join()
    }


    println("done")
}

suspend fun timeOutWithNoException(cancelAtSeconds: Long) = withTimeoutOrNull(cancelAtSeconds) {
    repeat(1000) {
        delay(10)
        print(".")
    }
}