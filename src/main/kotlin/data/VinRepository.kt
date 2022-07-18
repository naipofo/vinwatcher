package data

import com.naipofo.StuffStore
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import org.jsoup.Jsoup
import responses.CatalogItem
import util.traverseJson

class VinRepository(
    private val stuffStore: StuffStore, private val client: HttpClient
) {
    suspend fun getItems(url: String): List<CatalogItem> = (Json.parseToJsonElement(
        Jsoup.parse(client.get(url).bodyAsText()).select("[data-js-react-on-rails-store=\"MainStore\"]")[0].html()
    ) as JsonObject).traverseJson(listOf("items", "catalogItems", "byId")).values.map {
        Json.decodeFromJsonElement(it)
    }
}