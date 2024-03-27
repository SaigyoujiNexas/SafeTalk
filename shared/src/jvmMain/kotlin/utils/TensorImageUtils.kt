package utils

import org.pytorch.MemoryFormat
import org.pytorch.Tensor
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.IOException
import java.nio.FloatBuffer
import javax.imageio.ImageIO

object TensorImageUtils {
    var TORCHVISION_NORM_MEAN_RGB: FloatArray = floatArrayOf(0.485f, 0.456f, 0.406f)
    var TORCHVISION_NORM_STD_RGB: FloatArray = floatArrayOf(0.229f, 0.224f, 0.225f)

    /**
     * @param normMeanRGB means for RGB channels normalization, length must equal 3, RGB order
     * @param normStdRGB  standard deviation for RGB channels normalization, length must equal 3, RGB
     * order
     */
    @Throws(IOException::class)
    fun bitmapToFloat32Tensor(
        imageByteArray: ByteArray,
        normMeanRGB: FloatArray,
        normStdRGB: FloatArray,
        memoryFormat: MemoryFormat
    ): Tensor {
        checkNormMeanArg(normMeanRGB)
        checkNormStdArg(normStdRGB)
        val image = ImageIO.read(ByteArrayInputStream(imageByteArray))
        return bitmapToFloat32Tensor(
            image, 0, 0, image.width, image.height, normMeanRGB, normStdRGB, memoryFormat
        )
    }

    private fun bitmapToFloatBuffer(
        bitmap: BufferedImage,
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        normMeanRGB: FloatArray,
        normStdRGB: FloatArray,
        outBuffer: FloatBuffer,
        outBufferOffset: Int,
        memoryFormat: MemoryFormat
    ) {
        checkOutBufferCapacity(outBuffer, outBufferOffset, width, height)
        checkNormMeanArg(normMeanRGB)
        checkNormStdArg(normStdRGB)
        require(!(memoryFormat != MemoryFormat.CONTIGUOUS && memoryFormat != MemoryFormat.CHANNELS_LAST)) { "Unsupported memory format $memoryFormat" }

        val pixelsCount = height * width
        val pixels = IntArray(pixelsCount)
        bitmap.getRGB(0, 0, width, height, pixels, 0, width)
        if (MemoryFormat.CONTIGUOUS == memoryFormat) {
            val offsetB = 2 * pixelsCount
            for (i in 0 until pixelsCount) {
                val c = pixels[i]
                val r = ((c shr 16) and 0xff) / 255.0f
                val g = ((c shr 8) and 0xff) / 255.0f
                val b = ((c) and 0xff) / 255.0f
                outBuffer.put(outBufferOffset + i, (r - normMeanRGB[0]) / normStdRGB[0])
                outBuffer.put(outBufferOffset + pixelsCount + i, (g - normMeanRGB[1]) / normStdRGB[1])
                outBuffer.put(outBufferOffset + offsetB + i, (b - normMeanRGB[2]) / normStdRGB[2])
            }
        } else {
            for (i in 0 until pixelsCount) {
                val c = pixels[i]
                val r = ((c shr 16) and 0xff) / 255.0f
                val g = ((c shr 8) and 0xff) / 255.0f
                val b = ((c) and 0xff) / 255.0f
                outBuffer.put(outBufferOffset + 3 * i + 0, (r - normMeanRGB[0]) / normStdRGB[0])
                outBuffer.put(outBufferOffset + 3 * i + 1, (g - normMeanRGB[1]) / normStdRGB[1])
                outBuffer.put(outBufferOffset + 3 * i + 2, (b - normMeanRGB[2]) / normStdRGB[2])
            }
        }
    }

    private fun bitmapToFloat32Tensor(
        bitmap: BufferedImage,
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        normMeanRGB: FloatArray,
        normStdRGB: FloatArray,
        memoryFormat: MemoryFormat
    ): Tensor {
        checkNormMeanArg(normMeanRGB)
        checkNormStdArg(normStdRGB)

        val floatBuffer = Tensor.allocateFloatBuffer(3 * width * height)
        bitmapToFloatBuffer(
            bitmap, x, y, width, height, normMeanRGB, normStdRGB, floatBuffer, 0, memoryFormat
        )
        return Tensor.fromBlob(floatBuffer, longArrayOf(1, 3, height.toLong(), width.toLong()), memoryFormat)
    }

    private fun checkOutBufferCapacity(
        outBuffer: FloatBuffer, outBufferOffset: Int, tensorWidth: Int, tensorHeight: Int
    ) {
        check(outBufferOffset + 3 * tensorWidth * tensorHeight <= outBuffer.capacity()) { "Buffer underflow" }
    }

    private fun checkNormStdArg(normStdRGB: FloatArray) {
        require(normStdRGB.size == 3) { "normStdRGB length must be 3" }
    }

    private fun checkNormMeanArg(normMeanRGB: FloatArray) {
        require(normMeanRGB.size == 3) { "normMeanRGB length must be 3" }
    }
}