package com.example.xd.utils

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date

object DateSerializer: KSerializer<Date>{
    private val formatter = DateTimeFormatter.ISO_DATE_TIME

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Date", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Date {
        val dateString = decoder.decodeString()
        val localDateTime = LocalDateTime.parse(dateString, formatter)
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant())
    }

    override fun serialize(encoder: Encoder, value: Date) {
        val localDateTime = LocalDateTime.ofInstant(value.toInstant(), ZoneId.systemDefault())
        encoder.encodeString(localDateTime.format(formatter))
    }
}