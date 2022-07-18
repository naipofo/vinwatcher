import dev.inmo.tgbotapi.bot.TelegramBot
import dev.inmo.tgbotapi.extensions.behaviour_builder.buildBehaviourWithLongPolling
import kotlinx.coroutines.*
import org.kodein.di.instance
import kotlin.time.Duration.Companion.seconds

suspend fun main(args: Array<String>) {
    val bot: TelegramBot by di.mainModule.instance()
    listOf(
        bot.buildBehaviourWithLongPolling { vinBot() }, backgroundCheck
    ).joinAll()
}

@OptIn(DelicateCoroutinesApi::class)
val backgroundCheck: Job = GlobalScope.launch {
    coroutineScope {
        while (true) {
            delay(30.seconds)
            checkPages()
        }
    }
}