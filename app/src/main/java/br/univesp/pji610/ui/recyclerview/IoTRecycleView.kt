package br.univesp.pji610.ui.recyclerview

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.univesp.pji610.database.model.IoT
import br.univesp.pji610.databinding.RecyclerviewIotBinding

class IoTRecycleView(
    private val context: Context,
    var iotOnClickEvent: (ioT: IoT) -> Unit = {},
    ioTs: List<IoT> = emptyList()
) : RecyclerView.Adapter<IoTRecycleView.ViewHolder>() {

    private val ioTs = ioTs.toMutableList()

    inner class ViewHolder(
        private val binding: RecyclerviewIotBinding,
        private val iotOnClickEvent: (ioT: IoT) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var iot: IoT

        init {
            itemView.setOnClickListener {
                if (::iot.isInitialized) {
                    iotOnClickEvent(iot)
                }
            }
        }

        fun associateItem(item: IoT) {
            Log.i("getAllIoTOfUser", "associateItem(): $item")
            this.iot = item
            val name = binding.recyclerviewIotItemsTextViewName
            val groupName = binding.recyclerviewIotItemsTextViewGroupName
            val humidity = binding.recyclerviewIotItemsTextViewGroupHumidity
            val noBreak = binding.recyclerviewIotItemsTextViewGroupNoBreak
            val temperature = binding.recyclerviewIotItemsTextViewGroupTemperature

            name.text = item.name
            groupName.text = item.groupId
            temperature.text =  item.name
            humidity.text =  item.name
            noBreak.text =  item.name

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder =
        ViewHolder(
            RecyclerviewIotBinding
                .inflate(
                    LayoutInflater.from(context)
                ),
            iotOnClickEvent
        )


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.associateItem(ioTs[position])
    }

    override fun getItemCount(): Int = ioTs.size

    fun update(newItems: List<IoT>) {
        notifyItemRangeRemoved(0, this.ioTs.size)
        this.ioTs.clear()
        this.ioTs.addAll(newItems)
        notifyItemInserted(this.ioTs.size)
    }

}
