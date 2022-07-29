import com.naipofo.StuffStore
import data.VinRepository
import dev.inmo.tgbotapi.bot.TelegramBot
import dev.inmo.tgbotapi.extensions.api.send.sendMessage
import dev.inmo.tgbotapi.types.ChatId
import kotlinx.coroutines.delay
import org.kodein.di.instance
import kotlin.time.Duration.Companion.seconds

suspend fun checkPages() {
    val stuffStore: StuffStore by di.mainModule.instance()
    val vinRepository: VinRepository by di.mainModule.instance()
    val telegramBot: TelegramBot by di.mainModule.instance()

    println("checking pages!")
    stuffStore.savedSearchesQueries.listAll().executeAsList().forEach { search ->
        delay(10.seconds)
        try {
            vinRepository.getItems(search.searchurl).forEach { item ->
                if (!stuffStore.seenPagesQueries.isSeen(search.user, extractId(item.url)).executeAsOne()) {
                    stuffStore.seenPagesQueries.insert(search.user, extractId(item.url))
                    telegramBot.sendMessage(ChatId(search.user), item.displayMessage())
                }
            }
        } catch (e: Exception) {
            println(e)
            println("during ${search.searchurl}")
        }
    }
}

suspend fun silentCheckPages(url: String, user: Long) {
    val stuffStore: StuffStore by di.mainModule.instance()
    val vinRepository: VinRepository by di.mainModule.instance()
    vinRepository.getItems(url).forEach { stuffStore.seenPagesQueries.insert(user, extractId(it.url)) }
}

fun extractId(url: String) = url.indexOfLast { it == '/' }.let { url.substring(it + 1 until url.indexOf("-", it)) }