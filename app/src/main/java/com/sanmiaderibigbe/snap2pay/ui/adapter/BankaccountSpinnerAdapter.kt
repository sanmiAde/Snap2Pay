package com.sanmiaderibigbe.snap2pay.ui.adapter

import android.R
import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Spinner
import java.util.ArrayList

object BankaccountSpinnerAdapter {

    val accountType = ArrayList<String>()

    fun initAdapter(context: Context, spinner: Spinner): Spinner {

        accountType.add("Savings account")
        accountType.add("Current account")

        val dataAdapter =  ArrayAdapter<String>(context, R.layout.simple_spinner_item, accountType)
        spinner.adapter = dataAdapter

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        return spinner


    }
}