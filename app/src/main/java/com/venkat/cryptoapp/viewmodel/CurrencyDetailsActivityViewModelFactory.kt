package com.venkat.cryptoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CurrencyDetailsActivityViewModelFactory(private val symbol : String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(CurrencyDetailsActivityViewModel::class.java)){
            return CurrencyDetailsActivityViewModel(symbol) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}