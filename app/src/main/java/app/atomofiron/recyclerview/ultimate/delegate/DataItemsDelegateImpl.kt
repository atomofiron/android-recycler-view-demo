package app.atomofiron.recyclerview.ultimate.delegate

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.atomofiron.recyclerview.ultimate.utils.UltimateDataFactory
import app.atomofiron.recyclerview.utils.DataItem
import app.atomofiron.recyclerview.ultimate.utils.UltimateItemsCallback

class DataItemsDelegateImpl : DataItemsDelegate {

    override lateinit var adapter: RecyclerView.Adapter<*>
    override var items: MutableList<DataItem> = UltimateDataFactory().getItems()
        private set

    override fun setItems(items: List<DataItem>) {
        /* нежелательно сохранять инстанс, который к нам пришёл,
         * потому что его содержимое может быть изменени там, откуда он пришёл,
         * что приведёт к неконсистентному состоянию данных в этом адаптере.
         * поэтому ма создаём копию списка.
         * поэтому, извне этот список сожет быть доступен только как иммутабельный список (List),
         * что обеспечено интерфейсом DataItemsDelegate, в котором это поле List, а не MutableList.
         */
        val new = ArrayList(items)
        val callback = UltimateItemsCallback(this.items, new)
        this.items = new
        val result = DiffUtil.calculateDiff(callback, true)
        result.dispatchUpdatesTo(adapter)
    }

    override fun removeItem(index: Int): DataItem {
        val item = items.removeAt(index)
        adapter.notifyItemRemoved(index)
        return item
    }

    override fun insertItem(index: Int, item: DataItem) {
        items.add(index, item)
        adapter.notifyItemInserted(index)
    }

    override fun swap(first: Int, second: Int) {
        val temp = items[first]
        items[first] = items[second]
        items[second] = temp
        adapter.notifyItemMoved(first, second)
        when {
            first > second -> adapter.notifyItemMoved(second.inc(), first)
            first < second -> adapter.notifyItemMoved(second.dec(), first)
        }
    }

    override fun replaceItem(index: Int, item: DataItem): DataItem {
        val replaced = items[index]
        items[index] = item
        val areItemsTheSame = replaced.areItemsTheSame(item)
        when {
            areItemsTheSame && replaced.areContentsTheSame(item) -> Unit
            areItemsTheSame -> adapter.notifyItemChanged(index)
            else -> {
                adapter.notifyItemRemoved(index)
                adapter.notifyItemInserted(index)
            }
        }
        return replaced
    }
}