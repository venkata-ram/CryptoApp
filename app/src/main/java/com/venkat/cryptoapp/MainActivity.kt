package com.venkat.cryptoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.venkat.cryptoapp.adapter.CurrencyRecyclerviewAdapter
import com.venkat.cryptoapp.model.CryptoCurrencyItem

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter : CurrencyRecyclerviewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = CurrencyRecyclerviewAdapter{currency:CryptoCurrencyItem ->
            currencyItemClicked(currency)
        }

    }

    private fun currencyItemClicked(currency : CryptoCurrencyItem){
        Log.i("MyTag",currency.symbol)
    }


}