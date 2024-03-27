package settings

import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.Settings

val delegate: platform.Foundation.NSUserDefaults = platform.Foundation.NSUserDefaults.standardUserDefaults()
actual val settings: Settings = NSUserDefaultsSettings(delegate)