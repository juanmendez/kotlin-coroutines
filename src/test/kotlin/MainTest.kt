import java.util.Random
import kotlin.system.measureTimeMillis
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class MainTest {
    // https://kotlinlang.org/docs/multiplatform-mobile-concurrency-and-coroutines.html#asynchronous-vs-parallel-processing
    @Test
    fun `async 101`() = runTest {
        // dart uses async async/await and is based on futures or streams
        // Future<Int> doWorkOne() async {
        //  return value;
        // }
        // final result = await doWorkOne()

        // here result is just assigned, not awaited, and suspend with return type
        // is treated as a future
        suspend fun doWorkOne(): Int {
            delay(1000)
            println("working 1 ${Thread.currentThread().name}")
            return Random(System.currentTimeMillis()).nextInt(42)
        }

        suspend fun doWorkTwo(): Int {
            delay(10)
            println("working 2 ${Thread.currentThread().name}")
            return Random(System.currentTimeMillis()).nextInt(42)
        }

        fun doWorkThreeAsync(): Deferred<Int> = async {
            delay(300)
            println("working 3 ${Thread.currentThread().name}")
            return@async Random(System.currentTimeMillis()).nextInt(42)
        }

        // working concurrently
        val time = measureTimeMillis {
            val result1 = doWorkOne()
            val result2 = doWorkTwo()
            println("the result is $result1, $result2")
        }

        println("This took $time ms to run concurrently")


        // working concurrently
        val concurrentJob = launch {
            val time = measureTimeMillis {
                val result1 = doWorkOne()
                val result2 = doWorkTwo()
                println("the concurrent result is ${result1}, ${result2}")
            }

            println("This took $time ms to run concurrently")
        }
        concurrentJob.join()

        // here is async method and awaited like in Dart
        val asyncJob = launch {
            val time = measureTimeMillis {
                val result1 = async { doWorkOne() }
                val result2 = async { doWorkTwo() }
                println("the async result is ${result1.await()}, ${result2.await()}")
            }

            println("This took $time ms to run asynchronously")
        }
        asyncJob.join()


        val asyncJob2 = launch {
            val time = measureTimeMillis {
                val result1 = async { doWorkOne() }
                val result2 = async { doWorkTwo() }
                val result3 = doWorkThreeAsync()
                println("the async result is ${result1.await()}, ${result2.await()}, ${result3.await()}")
            }

            println("This took $time ms to run asynchronously")
        }
        asyncJob2.join()
    }
}