package org.example.project.util

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities

    fun Long.toDate(): String {
        val date = Date(this)
        return SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA).format(date)
    }
    fun Date.toLong(): Long {
        return this.time
    }