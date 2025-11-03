package com.autoever.everp.utils.serializer

import com.autoever.everp.domain.model.notification.NotificationLinkEnum
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.serialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object NotificationLinkEnumSerializer : KSerializer<NotificationLinkEnum> {
    override val descriptor: SerialDescriptor = serialDescriptor<String>()

    override fun serialize(encoder: Encoder, value: NotificationLinkEnum) {
        encoder.encodeString(value.toApiString())
    }

    override fun deserialize(decoder: Decoder): NotificationLinkEnum {
        val string = decoder.decodeString()
        return NotificationLinkEnum.fromStringOrNull(string)
            ?: NotificationLinkEnum.UNKNOWN // 기본값
    }
}
