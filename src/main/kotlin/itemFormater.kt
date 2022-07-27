import dev.inmo.tgbotapi.extensions.utils.formatting.buildEntities
import dev.inmo.tgbotapi.extensions.utils.formatting.italicln
import dev.inmo.tgbotapi.extensions.utils.formatting.link
import dev.inmo.tgbotapi.extensions.utils.formatting.linkln
import responses.CatalogItem

fun CatalogItem.displayMessage() = buildEntities {
    linkln(title, url)
    italicln("by ${user.login}")
    +"$sizeTitle \u30FB $brandTitle \u30FB $price $currency\n"
    link("fullrez \uD83D\uDDBC️", photo.fullSizeUrl)
}