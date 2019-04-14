package tk.burdukowsky.rutorparserandroid

object ApiServiceProvider {
    val instance = RetrofitProvider.instance
        .create(ApiService::class.java)
}
