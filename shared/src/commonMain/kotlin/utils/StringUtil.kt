package utils

fun String.isEmail(): Boolean {
    return this.contains("@")
}

fun String.isTel(): Boolean {
    return this.length == 11
}