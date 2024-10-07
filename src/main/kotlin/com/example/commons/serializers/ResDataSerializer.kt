import com.example.commons.dtos.ResDataDto
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.*

class ResDataSerializer<T : Any>(private val tSerializer: KSerializer<T>) :
    JsonTransformingSerializer<ResDataDto<T>>(ResDataDto.serializer(tSerializer)) {

    override fun transformSerialize(element: JsonElement): JsonElement {
        return removeTypeAndEntityFields(element)
    }

    private fun removeTypeAndEntityFields(element: JsonElement): JsonElement {
        return when (element) {
            is JsonObject -> {
                if (element.containsKey("entity")) {
                    val entityContent = element["entity"] ?: JsonNull

                    if (entityContent is JsonObject) {
                        buildJsonObject {
                            entityContent.forEach { (key, value) ->
                                if (key != "type") {
                                    put(key, value)
                                }
                            }
                        }
                    } else {
                        entityContent
                    }
                } else if (element.containsKey("entities")) {
                    val entitiesContent = element["entities"] ?: JsonNull

                    if (entitiesContent is JsonArray) {
                        buildJsonArray {
                            entitiesContent.forEach { item ->
                                add(removeTypeAndEntityFields(item))
                            }
                        }
                    } else {
                        entitiesContent
                    }
                } else {
                    buildJsonObject {
                        element.forEach { (key, value) ->
                            if (key != "type") {
                                put(key, removeTypeAndEntityFields(value))
                            }
                        }
                    }
                }
            }

            is JsonArray -> {
                buildJsonArray {
                    element.forEach { item ->
                        add(removeTypeAndEntityFields(item))
                    }
                }
            }

            else -> element
        }
    }

    override fun transformDeserialize(element: JsonElement): JsonElement {
        return element
    }
}