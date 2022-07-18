import com.naipofo.StuffStore
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import org.jsoup.Jsoup
import responses.CatalogItem

private val json = Json { ignoreUnknownKeys = true }
private val http = HttpClient {}
private val database = StuffStore(JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY))

suspend fun main(args: Array<String>) {
    val doc =
        http.get("url")
            .bodyAsText()
    val jsonText = Jsoup.parse(doc).select("[data-js-react-on-rails-store=\"MainStore\"]")[0].html()
    val data = json.parseToJsonElement(jsonText) as JsonObject
    val elements = (((data["items"] as JsonObject)["catalogItems"] as JsonObject)["byId"] as JsonObject).values.map {
        json.decodeFromJsonElement<CatalogItem>(it)
    }
    elements.forEach(::println)
}