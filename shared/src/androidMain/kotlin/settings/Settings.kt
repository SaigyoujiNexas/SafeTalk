package settings

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings

lateinit var delegate: SharedPreferences
actual val settings: Settings
    get () = SharedPreferencesSettings(delegate)