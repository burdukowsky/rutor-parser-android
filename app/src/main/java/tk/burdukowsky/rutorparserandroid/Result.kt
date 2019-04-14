package tk.burdukowsky.rutorparserandroid

data class Result(
    val title: String,
    val magnet: String,
    val size: String,
    val seeds: Long,
    val leaches: Long
)
