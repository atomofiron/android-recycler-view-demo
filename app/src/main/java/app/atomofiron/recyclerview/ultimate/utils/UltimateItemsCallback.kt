package app.atomofiron.recyclerview.ultimate.utils

import androidx.recyclerview.widget.DiffUtil
import app.atomofiron.recyclerview.utils.DataItem

class UltimateItemsCallback(
    old: List<DataItem>,
    new: List<DataItem>,
) : DiffUtil.Callback() {

    // делаем копии по причине, описанной в DataItemsDelegateImpl.setItems()
    private val old: List<DataItem> = old.toMutableList()
    private val new: List<DataItem> = new.toMutableList()

    override fun getOldListSize(): Int = old.size

    override fun getNewListSize(): Int = new.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return old[oldItemPosition].areItemsTheSame(new[newItemPosition])
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return old[oldItemPosition].areContentsTheSame(new[newItemPosition])
    }
}