package utils

import ImageLevel
import org.pytorch.IValue
import org.pytorch.MemoryFormat
import org.pytorch.Module

actual suspend fun checkImage(imageByteArray: ByteArray): ImageLevel {
    val module = Module.load("model.pt1")
    val float32 = TensorImageUtils.bitmapToFloat32Tensor(imageByteArray, TensorImageUtils.TORCHVISION_NORM_MEAN_RGB,
        TensorImageUtils.TORCHVISION_NORM_STD_RGB, MemoryFormat.CHANNELS_LAST)
    val guess = module.forward(IValue.from(float32))
    val ans = guess.toDoubleList()
    var maxIndex = -1
    var maxValue = Double.NaN
    ans.forEachIndexed{index, value ->
        if(maxValue.isNaN() || maxValue < value){
            maxIndex = index
            maxValue = value
        }
    }
    return ImageLevel.entries[maxIndex]
}