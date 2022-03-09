package app.atomofiron.recyclerview.easy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.atomofiron.recyclerview.R

class EasyAdapter : RecyclerView.Adapter<EasyAdapter.EasyViewHolder>() {

    private val items: MutableList<String> = IntArray(25) { it }.map { "Item $it" }.toMutableList()
    private lateinit var recyclerView: RecyclerView

    fun setItems(items: List<String>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EasyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.item_string, parent, false)
        itemView.setOnClickListener {
        }
        return EasyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EasyViewHolder, position: Int) {
        val textView = holder.itemView.findViewById<TextView>(R.id.item_string)
        textView.text = items[position]
    }

    override fun getItemCount(): Int = items.size

    class EasyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}