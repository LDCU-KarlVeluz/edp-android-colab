package com.example.myapplication.data.network.dto

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.longOrNull
import java.time.Instant
import java.time.format.DateTimeParseException

object FlexibleLongSerializer : KSerializer<Long?> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("FlexibleLong", PrimitiveKind.LONG)

    override fun deserialize(decoder: Decoder): Long? {
        val input = decoder as? JsonDecoder ?: return decoder.decodeLong()
        val element = input.decodeJsonElement() as? JsonPrimitive ?: return null
        
        val longVal = element.longOrNull
        if (longVal != null) return longVal
        
        return try {
            Instant.parse(element.content).toEpochMilli()
        } catch (e: DateTimeParseException) {
            null
        }
    }

    override fun serialize(encoder: Encoder, value: Long?) {
        if (value == null) encoder.encodeNull()
        else encoder.encodeLong(value)
    }
}
