package br.univesp.pji610.ui.activity

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import br.univesp.pji610.R
import br.univesp.pji610.databinding.ActivityChartBinding
import br.univesp.pji610.databinding.ActivityIotBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.charts.RadarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import com.github.mikephil.charting.utils.ColorTemplate

class ChartActivity : AppCompatActivity() {
    lateinit var barChart: BarChart

    lateinit var pieChart: PieChart

    lateinit var radarChart: RadarChart

    private val binding by lazy {
        ActivityChartBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)


        setBarChar()
//        setPieChar()
//        setRadarChar()
    }


    private fun setBarChar()
    {

        barChart = binding.chartActivityBarChart

        val list:ArrayList<BarEntry> = ArrayList()

        list.add(BarEntry(100f,100f))
        list.add(BarEntry(101f,101f))
        list.add(BarEntry(102f,102f))
        list.add(BarEntry(103f,103f))
        list.add(BarEntry(104f,104f))
        list.add(BarEntry(105f,105f))

        var barDataSet = BarDataSet(list, "List")

        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS, 255)
        barDataSet.valueTextColor = Color.BLACK

        val barData  = BarData(barDataSet)

        barChart.setFitBars(true)
        barChart.data = barData
        barChart.description.text = "Bar Chart"

        barChart.animateY(2000)
    }


//    private fun setRadarChar()
//    {
//
//        radarChart = binding.chartActivityRadarChart
//
//        val list:ArrayList<RadarEntry> = ArrayList()
//
//        list.add(RadarEntry(100f,100f))
//        list.add(RadarEntry(101f,101f))
//        list.add(RadarEntry(102f,102f))
//        list.add(RadarEntry(103f,103f))
//        list.add(RadarEntry(104f,104f))
//        list.add(RadarEntry(105f,105f))
//
//        var radarDataSet = RadarDataSet(list, "List")
//
//        radarDataSet.setColors(ColorTemplate.MATERIAL_COLORS, 255)
//        radarDataSet.valueTextColor = Color.BLACK
//        radarDataSet.valueTextSize = 14f
//        radarDataSet.lineWidth = 2f
//
//        val pieData  = RadarData(radarDataSet)
//
//        radarChart.data = pieData
//        radarChart.description.text = "Radar Chart"
//
//        radarChart.animateY(2000)
//    }
//
//
//    private fun setPieChar()
//    {
//
//        pieChart = binding.chartActivityPieChart
//
//        val list:ArrayList<PieEntry> = ArrayList()
//
//        list.add(PieEntry(100f,100f))
//        list.add(PieEntry(101f,101f))
//        list.add(PieEntry(102f,102f))
//        list.add(PieEntry(103f,103f))
//        list.add(PieEntry(104f,104f))
//        list.add(PieEntry(105f,105f))
//
//        var pieDataSet = PieDataSet(list, "List")
//
//        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS, 255)
//        pieDataSet.valueTextColor = Color.BLACK
//        pieDataSet.valueTextSize = 15f
//
//        val pieData  = PieData(pieDataSet)
//
//        pieChart.data = pieData
//        pieChart.description.text = "Pie Chart"
//        pieChart.centerText = "List"
//
//        pieChart.animateY(2000)
//    }
}