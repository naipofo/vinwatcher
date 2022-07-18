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
        waitText().first().text.let { url ->
            stuffStore.savedSearchesQueries.insert(it.chat.id.chatId, url)
            sendMessage(
                it.chat,
                "Link saved \uD83D\uDCBE The bot might send a large amount of entrees for the first 5 minutes"
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
    onCommand("remove") {
        reply(it, "Send me a link:")
        waitText().first().text.trim().let { url ->
            sendMessage(
                it.chat.id, if (url.isEmpty()) {
                    "Link not removed - can't be blank"
                } else if (stuffStore.savedSearchesQueries.isSaved(it.chat.id.chatId, url).executeAsOne()) {
                    stuffStore.savedSearchesQueries.delete(it.chat.id.chatId, url)
                    "Link deleted \u274C"
                } else {
                    "Link is not saved"
                }
            )
        }
    }
}