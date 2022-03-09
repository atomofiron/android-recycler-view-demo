package app.atomofiron.recyclerview.ultimate.data

import app.atomofiron.recyclerview.utils.DataItem

class CardItem(
    val string: String,
    val picture: Int,
    val timestamp: Long,
) : DataItem {

    override val id: Long = string.hashCode().toLong()

    override fun areContentsTheSame(other: DataItem): Boolean {
        // принцип такой же, как в equals()
        return when {
            other !is CardItem -> false
            other.string != string -> false
            other.picture != picture -> false
            other.timestamp != timestamp -> false
            else -> true
        }
    }
}