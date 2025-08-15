package com.example.pocketmaster.ui.dashboard

import android.R.attr.category
import android.graphics.Color
import android.health.connect.datatypes.units.Percentage
import android.icu.number.NumberFormatter
import java.text.NumberFormat
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.pocketmaster.R
import com.example.pocketmaster.data.model.DashboardState
import com.example.pocketmaster.data.model.ExpenseCategoryData
import com.example.pocketmaster.databinding.FragmentDashboardBinding
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.collections.map

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DashboardViewModel by viewModels()
    private val currencyFormatter = NumberFormat.getCurrencyInstance(Locale("hi","IN"))

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View, savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        setupPieChart()
        observeDashboardStats()
    }

    private fun setupPieChart() {
        binding.pieChart.apply {
            description.isEnabled=false
            isDrawHoleEnabled=true
            setHoleColor(Color.TRANSPARENT)

            holeRadius=58f
            transparentCircleRadius =61f
            setTransparentCircleColor(Color.WHITE)
            setTransparentCircleAlpha(110)

            setUsePercentValues(true)
            setEntryLabelColor(Color.WHITE)
            setEntryLabelTextSize(12f)
            setDrawEntryLabels(false)

            legend.apply{
                isEnabled=true
                orientation= Legend.LegendOrientation.VERTICAL
                horizontalAlignment=Legend.LegendHorizontalAlignment.RIGHT
                verticalAlignment=Legend.LegendVerticalAlignment.CENTER
                setDrawInside(false)
                textSize=12f
                xEntrySpace=10f
                yEntrySpace=0f
                yOffset=0f
                formSize=16f

            }

            animateY(14000, Easing.EaseInOutQuad)


        }
    }

    private fun observeDashboardStats() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.dashboardState.collect { state: DashboardState ->
                updateDashboardUI(state)

            }
        }
    }

    private fun updateDashboardUI(state: DashboardState) {
        binding.apply {
            tvBalance.text = currencyFormatter.format(state.balance)
            tvTotalIncome.text = currencyFormatter.format(state.totalIncome)
            tvTotalExpense.text = currencyFormatter.format(state.totalExpense)

            updatePieChart(state.expenseCategories)

        }


    }

    private fun updatePieChart(categories:List<ExpenseCategoryData>){
        if(categories.isEmpty())return

        val entries :List<PieEntry> = categories.map{category ->
            PieEntry(category.amount.toFloat(),category.category)

        }

        val colors:List<Int> = listOf(
            Color.rgb(64,89,128),
            Color.rgb(149,165,124),
            Color.rgb(217,187,162),
            Color.rgb(191,134,134),
            Color.rgb(179,48,80),
            Color.rgb(193,37,82),
            Color.rgb(255,102,0),
            Color.rgb(245,199,0)
        )

        val dataSet:PieDataSet = PieDataSet(entries, "Expense Categories").apply{
            setColors(colors)
            valueFormatter= PercentFormatter(binding.pieChart)
            valueTextSize=12f
            valueTextColor=Color.WHITE
            xValuePosition= PieDataSet.ValuePosition.INSIDE_SLICE
        }

        val pieData = PieData(dataSet)
        binding.pieChart.apply{
            data=pieData
            invalidate()

        }
    }

    override fun onDestroyView(){
        super.onDestroyView()
        _binding=null
    }
}