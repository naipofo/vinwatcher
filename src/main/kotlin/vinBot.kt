import com.naipofo.StuffStore
import dev.inmo.tgbotapi.extensions.api.send.reply
import dev.inmo.tgbotapi.extensions.api.send.sendMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContextReceiver
import dev.inmo.tgbotapi.extensions.behaviour_builder.expectations.waitText
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onCommand
import di.mainModule
import kotlinx.coroutines.flow.first
import org.kodein.di.instance

val vinBot: BehaviourContextReceiver<Unit> = {
    val stuffStore: StuffStore by mainModule.instance()

    onCommand("start") {
        reply(it, "Hello \uD83D\uDC4B\nThis bot will help you track searches!")
    }
    onCommand("add") {
        reply(it, "Send me a link:")
        waitText().first().text.trim().let { url ->
            sendMessage(
                it.chat.id, if (url.isEmpty()) {
                    "Link not saved - can't be blank"
                } else {
                    stuffStore.savedSearchesQueries.insert(it.chat.id.chatId, url)
                    "Link saved \uD83D\uDCBE"
                }
            )
        }
    }
    onCommand("list") {
        reply(
            it,
            stuffStore.savedSearchesQueries.getForUser(it.chat.id.chatId).executeAsList()
                .joinToString(separator = "\n", prefix = "Saved links:\n")
        )
    }
    onCommand("remove") { TODO() }
}