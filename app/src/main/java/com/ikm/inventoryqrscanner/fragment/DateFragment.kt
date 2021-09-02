package com.ikm.inventoryqrscanner.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ikm.inventoryqrscanner.databinding.FragmentDateBinding
import java.util.*

class DateFragment(var listener: DateListener) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentDateBinding
    private var date: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
            date = "$day/${month + 1}/$year"
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