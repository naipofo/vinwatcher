import responses.CatalogItem

fun CatalogItem.displayMessage() = """
    ${photo.fullSizeUrl}
    $title by ${user.login}
    $price $currency
    $url
""".trimIndent()