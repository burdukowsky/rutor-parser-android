package tk.burdukowsky.rutorparserandroid

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("search")
    fun getResults(@Query("q") q: String): Call<List<Result>>
}
