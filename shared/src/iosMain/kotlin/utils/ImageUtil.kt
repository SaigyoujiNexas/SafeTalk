package utils

import ImageLevel

actual suspend fun checkImage(imageByteArray: ByteArray): ImageLevel {
    return ImageLevel.NEUTRAL
}