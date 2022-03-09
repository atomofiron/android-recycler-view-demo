package app.atomofiron.recyclerview.ultimate.api

import app.atomofiron.recyclerview.utils.DataItem

interface UltimateItemListener {
    fun onItemClick(index: Int, item: DataItem)
}