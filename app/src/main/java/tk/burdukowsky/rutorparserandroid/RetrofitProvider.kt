package tk.burdukowsky.rutorparserandroid

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitProvider {
    val instance = Retrofit.Builder()
        .baseUrl("https://rgodxvxfad.ml/rutor-parser/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}
