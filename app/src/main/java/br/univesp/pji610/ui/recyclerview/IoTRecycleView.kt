package br.univesp.pji610.ui.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.univesp.pji610.database.model.IoT_GroupIoT
import br.univesp.pji610.databinding.RecyclerviewIotItemsBinding

class IoTRecycleView(
    private val context: Context,
    ioTs: List<IoT_GroupIoT> = emptyList(),
    var ioTOnClickEvent: (ioT: IoT_GroupIoT) -> Unit = {}
) : RecyclerView.Adapter<IoTRecycleView.ViewHolder>() {

    private val ioTs = ioTs.toMutableList()

    inner class ViewHolder(private val binding: RecyclerviewIotItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var iot: IoT_GroupIoT

        init {
            itemView.setOnClickListener {
                if (::iot.isInitialized) {
                    ioTOnClickEvent(iot)
                }
            }
        }

        fun associateItem(iot: IoT_GroupIoT) {
            this.iot = iot
            val name = binding.recyclerviewIotItemsTextViewName
            val groupName = binding.recyclerviewIotItemsTextViewGroupName
            val humidity = binding.recyclerviewIotItemsTextViewGroupHumidity
            val temperature = binding.recyclerviewIotItemsTextViewGroupTemperature
            val noBreak = binding.recyclerviewIotItemsTextViewGroupNoBreak
            val image = binding.recyclerviewIotItemsTextViewImage


            name.text = iot.ioT.name
            groupName.text = iot.groupIoT.name
            humidity.text = iot.groupIoT.humidity.toString()
            temperature.text = iot.groupIoT.temperature.toString()
            noBreak.text = iot.groupIoT.noBreak.toString()

//            val visibility = if (iot.name != null) {
//                View.VISIBLE
//                image.loadImageFromPath(iot.Path)
//            } else {
//                View.GONE
//            }

            image.visibility = View.GONE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding = RecyclerviewIotItemsBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ioT = ioTs[position]
        holder.associateItem(ioT)
    }

    override fun getItemCount(): Int = ioTs.size

    fun update(ioTs: List<IoT_GroupIoT>) {
        this.ioTs.clear()
        this.ioTs.addAll(ioTs)
        notifyDataSetChanged()
    }

}
