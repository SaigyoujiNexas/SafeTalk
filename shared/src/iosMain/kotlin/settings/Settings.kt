package settings

import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.Settings
import platform.Foundation.NSUserDefaults

val delegate: NSUserDefaults = NSUserDefaults.standardUserDefaults()
actual val settings: Settings = NSUserDefaultsSettings(delegate)