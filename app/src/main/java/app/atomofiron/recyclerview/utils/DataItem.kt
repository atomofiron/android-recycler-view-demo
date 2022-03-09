package app.atomofiron.recyclerview.utils

interface DataItem {

    val id: Long

    // позволяет вызывать эти функции, не зная с каким классом мы работаем
    fun areContentsTheSame(other: DataItem): Boolean

    fun areItemsTheSame(other: DataItem): Boolean {
        return id == other.id && this::class == other::class
    }
}