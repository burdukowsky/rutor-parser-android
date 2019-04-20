package tk.burdukowsky.rutorparserandroid

object ErrorConverterProvider {
    val instance = RetrofitProvider.instance
        .responseBodyConverter<ApiError>(ApiError::class.java, arrayOfNulls<Annotation>(0))
}
