package app.atomofiron.recyclerview.ultimate.delegate

import androidx.recyclerview.widget.RecyclerView
import app.atomofiron.recyclerview.utils.DataItem

interface DataItemsDelegate {

    var adapter: RecyclerView.Adapter<*>
    val items: List<DataItem>

    fun setItems(items: List<DataItem>)
    fun removeItem(index: Int): DataItem
    fun insertItem(index: Int, item: DataItem)
    fun swap(first: Int, second: Int)
    fun replaceItem(index: Int, item: DataItem): DataItem
}