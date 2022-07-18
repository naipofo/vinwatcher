package util

import kotlinx.serialization.json.JsonObject

fun JsonObject.traverseJson(path: List<String>): JsonObject = if (path.size > 1) {
    (get(path.first()) as JsonObject).traverseJson(path.slice(1..path.size))
} else this