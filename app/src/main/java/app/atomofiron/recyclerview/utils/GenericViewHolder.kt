package app.atomofiron.recyclerview.utils

import android.view.View
import androidx.recyclerview.widget.RecyclerView

// позволяет передавать холдеру модельку с данными,
// не приводя его к конкретному классу
abstract class GenericViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun bind(data: DataItem)
}