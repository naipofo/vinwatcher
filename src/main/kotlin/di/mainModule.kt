package di

import com.naipofo.StuffStore
import com.naipofo.vinwatcher.BuildConfig
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import data.VinRepository
import dev.inmo.tgbotapi.bot.ktor.telegramBot
import io.ktor.client.*
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance

val mainModule = DI {
    bindSingleton("tg bot") { BuildConfig.TG_BOT_API_KEY }
    bindSingleton {
        val driver = JdbcSqliteDriver("jdbc:sqlite:botstore.sqlite3")
        try {
            StuffStore.Schema.create(driver)
        } catch (_: Exception) {
        }
        StuffStore(driver)
    }
    bindSingleton { HttpClient() }
    bindSingleton { telegramBot(instance<String>("tg bot")) }
    bindSingleton { VinRepository(instance()) }
}