import com.example.commons.dtos.ResDataDto
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.*

class ResDataSerializer<T : Any>(tSerializer: KSerializer<T>) :
    JsonTransformingSerializer<ResDataDto<T>>(ResDataDto.serializer(tSerializer)) {

    override fun transformSerialize(element: JsonElement): JsonElement {
        return removeTypeFields(element)
    }

    private fun removeTypeFields(element: JsonElement): JsonElement {
        return when (element) {
            is JsonObject -> {
                buildJsonObject {
                    element.forEach { (key, value) ->
                        if (key != "type") {
                            put(key, removeTypeFields(value))
                        }
                    }
                }
            }

            is JsonArray -> {
                buildJsonArray {
                    element.forEach { item ->
                        add(removeTypeFields(item))
                    }
                }
            }

            else -> element
        }
    }
}