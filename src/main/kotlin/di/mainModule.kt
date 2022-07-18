package di

import com.naipofo.StuffStore
import com.naipofo._vinwatcher.BuildConfig
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import data.VinRepository
import dev.inmo.tgbotapi.bot.ktor.telegramBot
import io.ktor.client.*
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance

val mainModule = DI {
    bindSingleton("tg bot") { BuildConfig.TG_BOT_API_KEY }
    bindSingleton { StuffStore(JdbcSqliteDriver("./botstore.sqlite3")) }
    bindSingleton { HttpClient() }
    bindSingleton { telegramBot(instance<String>("tg bot")) }
    bindSingleton { VinRepository(instance(), instance()) }
}