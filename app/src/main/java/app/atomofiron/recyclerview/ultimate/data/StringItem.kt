package app.atomofiron.recyclerview.ultimate.data

import app.atomofiron.recyclerview.utils.DataItem

class StringItem(
    val string: String,
) : DataItem {

    override val id: Long = string.hashCode().toLong()

    override fun areContentsTheSame(other: DataItem): Boolean {
        // принцип такой же, как в equals()
        return when {
            other !is StringItem -> false
            other.string != string -> false
            else -> true
        }
    }
}