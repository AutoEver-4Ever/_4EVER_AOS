package com.autoever.everp.utils.serializer

import com.autoever.everp.domain.model.notification.NotificationSourceEnum
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.serialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object NotificationSourceEnumSerializer : KSerializer<NotificationSourceEnum> {
    override val descriptor: SerialDescriptor = serialDescriptor<String>()

    override fun serialize(encoder: Encoder, value: NotificationSourceEnum) {
//        encoder.encodeString(value.toApiString() ?: )
        encoder.encodeString(value.name)
    }

    override fun deserialize(decoder: Decoder): NotificationSourceEnum {
        val string = decoder.decodeString()
        return NotificationSourceEnum.fromStringOrNull(string)
            ?: NotificationSourceEnum.UNKNOWN // 기본값
    }
}
