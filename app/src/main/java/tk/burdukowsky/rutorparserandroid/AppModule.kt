package tk.burdukowsky.rutorparserandroid

import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import android.app.Application

@Module
class AppModule(private var application: App) {

    @Provides
    @Singleton
    fun provideApplication(): Application {
        return application
    }

    @Singleton
    @Provides
    fun provideApiService(): ApiService {
        return ApiService.create()
    }

}
