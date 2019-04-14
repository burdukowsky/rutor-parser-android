package tk.burdukowsky.rutorparserandroid

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("{query}")
    fun getResults(@Path("query") query: String): Call<List<Result>>

    companion object Factory {
        fun create(): ApiService {
            return RetrofitProvider.instance.create(ApiService::class.java)
        }
    }

}
