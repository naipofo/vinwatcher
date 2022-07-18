import dev.inmo.tgbotapi.bot.TelegramBot
import dev.inmo.tgbotapi.extensions.behaviour_builder.buildBehaviourWithLongPolling
import org.kodein.di.instance

suspend fun main(args: Array<String>) {
    val bot: TelegramBot by di.mainModule.instance()
    bot.buildBehaviourWithLongPolling { vinBot() }.join()
}