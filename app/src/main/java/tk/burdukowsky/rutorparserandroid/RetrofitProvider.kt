package tk.burdukowsky.rutorparserandroid

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitProvider {
    val instance = Retrofit.Builder()
        .baseUrl("https://rms.burdukowsky.tk/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}
