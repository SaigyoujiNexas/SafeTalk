package utils

import ImageLevel
import android.graphics.BitmapFactory
import android.util.Log
import org.pytorch.IValue
import org.pytorch.LiteModuleLoader
import org.pytorch.MemoryFormat
import org.pytorch.torchvision.TensorImageUtils

private const val TAG = "ImageUtil"
actual suspend fun checkImage(imageByteArray: ByteArray): ImageLevel {
    val bitmap = BitmapFactory
        .decodeByteArray(imageByteArray, 0, imageByteArray.size, null)
    val module = LiteModuleLoader.load(assetFilePath("nsfw.ptl"))
    val inputTensor = TensorImageUtils.bitmapToFloat32Tensor(
        bitmap,
        TensorImageUtils.TORCHVISION_NORM_MEAN_RGB,
        TensorImageUtils.TORCHVISION_NORM_STD_RGB, MemoryFormat.CHANNELS_LAST
    )
    val outputTensor = module.forward(IValue.from(inputTensor)).toTensor()
    var maxIndex = -1
    var maxValue: Float = Float.NaN
    val outArray = outputTensor.dataAsFloatArray
    Log.e(TAG, "checkImage: size: ${outArray.size}")
    outArray.forEachIndexed { index, value ->
        Log.e(TAG, "checkImage: current index : $index, current value: $value", )
        if (maxValue.isNaN() || maxValue < value) {
            maxIndex = index
            maxValue = value
        }
    }
    return ImageLevel.entries[maxIndex]
}