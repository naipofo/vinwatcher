package util

import kotlinx.serialization.json.JsonObject

fun JsonObject.traverseJson(path: List<String>): JsonObject = if (path.isNotEmpty()) {
    (get(path.first()) as JsonObject).traverseJson(path.slice(1 until path.size))
} else this