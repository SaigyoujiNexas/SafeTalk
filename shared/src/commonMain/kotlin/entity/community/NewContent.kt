package entity.community

data class NewContent(
    val title: String = "",
    val content: String = "",
    val images: List<ByteArray> = emptyList()
)
