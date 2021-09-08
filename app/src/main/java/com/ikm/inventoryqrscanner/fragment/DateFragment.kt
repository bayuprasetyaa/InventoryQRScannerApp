package com.ikm.inventoryqrscanner.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ikm.inventoryqrscanner.databinding.FragmentDateBinding

class DateFragment(private var listener: DateListener) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentDateBinding
    private var date: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDateBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListener()
    }

    private fun setupListener() {
        binding.calender.setOnDateChangeListener { _, year, month, day ->
            val month1 = month + 1
            val days = day.toString().padStart(2, '0')
            val months = month1.toString().padStart(2, '0')
            date = "$days$months$year"
            Log.e("DateFragment", date)
        }


        binding.btnApply.setOnClickListener {
            listener.onSuccess(date)
            this.dismiss()
        }
    }

    interface DateListener {
        fun onSuccess(date: String)
    }

}