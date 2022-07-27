import com.naipofo.StuffStore
import data.VinRepository
import dev.inmo.tgbotapi.bot.TelegramBot
import dev.inmo.tgbotapi.extensions.api.send.sendMessage
import dev.inmo.tgbotapi.types.ChatId
import org.kodein.di.instance

suspend fun checkPages() {
    val stuffStore: StuffStore by di.mainModule.instance()
    val vinRepository: VinRepository by di.mainModule.instance()
    val telegramBot: TelegramBot by di.mainModule.instance()

    println("checking pages!")
    stuffStore.savedSearchesQueries.listAll().executeAsList().forEach { search ->
        vinRepository.getItems(search.searchurl).forEach { item ->
            if (!stuffStore.seenPagesQueries.isSeen(search.user, item.url).executeAsOne()) {
                stuffStore.seenPagesQueries.insert(search.user, item.url)
                telegramBot.sendMessage(ChatId(search.user), item.displayMessage())
            }
        }
    }
}

suspend fun silentCheckPages(url: String, user: Long) {
    val stuffStore: StuffStore by di.mainModule.instance()
    val vinRepository: VinRepository by di.mainModule.instance()
    vinRepository.getItems(url).forEach { stuffStore.seenPagesQueries.insert(user, it.url) }
}