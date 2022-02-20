import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

interface View {
    fun onDock()
    fun onUnDock()
}

val channel = Channel<Int>()

class Activity(private val context: CoroutineContext) : View {
    private lateinit var coroutineScope: CoroutineScope

    override fun onDock() {
        coroutineScope = CoroutineScope(context + Job())
        coroutineScope.launch {
            channel.consumeEach {
                println("consuming $it")
            }
        }
    }

    override fun onUnDock() {
        coroutineScope.cancel()
    }
}

class Fragment(private val context: CoroutineContext) : View {
    private lateinit var coroutineScope: CoroutineScope
    private var times = 0

    override fun onDock() {
        // a scope is alive only once, so we need to make a new scope after cancellation
        coroutineScope = CoroutineScope(context + Job())
        coroutineScope.launch {
            do {
                delay(100)
                ++times
                println("sending $times")
                channel.trySend(times)
            } while (coroutineScope.isActive)
        }
    }

    override fun onUnDock() {
        coroutineScope.cancel()
    }
}