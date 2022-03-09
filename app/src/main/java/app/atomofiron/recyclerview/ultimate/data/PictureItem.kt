package app.atomofiron.recyclerview.ultimate.data

import app.atomofiron.recyclerview.utils.DataItem

class PictureItem(
    val index: Int,
    val picture: Int,
) : DataItem {

    override val id: Long = picture.toLong()

    override fun areContentsTheSame(other: DataItem): Boolean {
        // принцип такой же, как в equals()
        return when {
            other !is PictureItem -> false
            other.index != index -> false
            other.picture != picture -> false
            else -> true
        }
    }
}