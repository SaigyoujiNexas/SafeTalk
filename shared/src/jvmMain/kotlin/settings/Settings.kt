package settings

import com.russhwolf.settings.PreferencesSettings
import com.russhwolf.settings.Settings
import java.util.prefs.Preferences
import java.util.prefs.PreferencesFactory

val delegate: Preferences = Preferences.userRoot()
actual val settings: Settings = PreferencesSettings(delegate)