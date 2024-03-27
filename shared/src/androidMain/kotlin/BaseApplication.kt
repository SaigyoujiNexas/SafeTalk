import android.app.Application
import settings.delegate

open class BaseApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        delegate = getSharedPreferences("settings", MODE_PRIVATE)
    }

    companion object {
        lateinit var instance: BaseApplication
    }
}