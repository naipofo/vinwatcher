import com.naipofo.StuffStore
import dev.inmo.tgbotapi.extensions.api.send.reply
import dev.inmo.tgbotapi.extensions.api.send.sendMessage
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContextReceiver
import dev.inmo.tgbotapi.extensions.behaviour_builder.expectations.waitText
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onCommand
import dev.inmo.tgbotapi.extensions.utils.formatting.buildEntities
import dev.inmo.tgbotapi.extensions.utils.formatting.linkln
import dev.inmo.tgbotapi.extensions.utils.types.buttons.ReplyKeyboardMarkup
import dev.inmo.tgbotapi.types.buttons.SimpleKeyboardButton
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
        val url = waitText().first().text
        reply(it, "How do you want to name this filter:")
        waitText().first().text.let { name ->
            sendMessage(
                it.chat, if (stuffStore.savedSearchesQueries.isSaved(it.chat.id.chatId, url, name).executeAsOne()) {
                    "Link with the same url or name is already saved"
                } else {
                    stuffStore.savedSearchesQueries.insert(it.chat.id.chatId, url, name)
                    silentCheckPages(url, it.chat.id.chatId)
                    "Link saved \uD83D\uDCBE"
                }
            )
        }
    }
    onCommand("list") {
        reply(it, buildEntities {
            stuffStore.savedSearchesQueries.getForUser(it.chat.id.chatId).executeAsList().forEach { query ->
                linkln(query.name, query.searchurl)
            }
        }, disableWebPagePreview = true)
    }
    onCommand("remove") {
        sendTextMessage(
            it.chat.id, "What link do you want to remove:", replyMarkup = ReplyKeyboardMarkup(
                *stuffStore.savedSearchesQueries.getForUser(it.chat.id.chatId).executeAsList().map { query ->
                    SimpleKeyboardButton(query.name)
                }.toTypedArray()
            )
        )
        waitText().first().text.let { name ->
            stuffStore.savedSearchesQueries.delete(it.chat.id.chatId, name)
            sendMessage(it.chat.id, "Link deleted \u274C")
        }
    }
}