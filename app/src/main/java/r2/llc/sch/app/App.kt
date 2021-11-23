package r2.llc.sch.app

import android.app.Application
import com.mocklets.pluto.Pluto
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        Pluto.initialize(this)
    }
}