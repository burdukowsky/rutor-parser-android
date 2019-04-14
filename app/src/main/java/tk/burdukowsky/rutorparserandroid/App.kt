package tk.burdukowsky.rutorparserandroid

import android.app.Application

class App : Application() {

    private var appComponent: AppComponent? = null
    // private var app: App? = null

    override fun onCreate() {
        super.onCreate()
        //buildComponentGraph()
        AppComponent.Initializer.init(this)
    }

    /*fun component(): AppComponent? {
        return appComponent
    }

    private fun buildComponentGraph() {
        appComponent = AppComponent.Initializer.init(this)
    }*/
}
