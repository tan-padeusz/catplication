package pl.tanpadeusz.catplication

import android.app.Application
import timber.log.Timber

class Catplication: Application() {
    private var tree = object: Timber.DebugTree() {
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            super.log(priority, "tbc-$tag", message, t)
        }
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG)
            Timber.plant(this.tree)
    }
}