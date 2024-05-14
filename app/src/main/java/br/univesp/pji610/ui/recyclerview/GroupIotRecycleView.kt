package br.univesp.pji610.ui.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.univesp.pji610.database.model.GroupIoT
import br.univesp.pji610.databinding.RecyclerviewIotItemsBinding

class GroupIotRecycleView(
    private val context: Context,
    groupIots: List<GroupIoT> = emptyList(),
    var groupIotOnClickEvent: (group: GroupIoT) -> Unit = {}
) : RecyclerView.Adapter<GroupIotRecycleView.ViewHolder>() {

    private val groupIots = groupIots.toMutableList()

    inner class ViewHolder(private val binding: RecyclerviewIotItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var groupIoT: GroupIoT

        init {
            itemView.setOnClickListener {
                if (::groupIoT.isInitialized) {
                    groupIotOnClickEvent(groupIoT)
                }
            }
        }

        fun associateItem(groupIot: GroupIoT) {
            this.groupIoT = groupIot
            val name = binding.recyclerviewIotItemsTextViewName
            val humidity = binding.recyclerviewIotItemsTextViewGroupHumidity
            val temperature = binding.recyclerviewIotItemsTextViewGroupTemperature
            val noBreak = binding.recyclerviewIotItemsTextViewGroupNoBreak


            name.text = groupIoT.name
            humidity.text = groupIoT.humidity.toString()
            temperature.text = groupIoT.temperature.toString()
            noBreak.text = groupIoT.noBreak.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding = RecyclerviewIotItemsBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val groupIoT = groupIots[position]
        holder.associateItem(groupIoT)
    }

    override fun getItemCount(): Int = groupIots.size

    fun update(groupIoTs: List<GroupIoT>) {
        this.groupIots.clear()
        this.groupIots.addAll(groupIots)
        notifyDataSetChanged()
    }

}
